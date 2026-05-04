package maintenance;

import java.time.*;
import java.util.*;

import facility.*;
import storage.*;
import user.*;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.*;
import storage.MaintenanceStorage;

public class MaintenanceManager {
	Scanner input = new Scanner(System.in);
	
	private MaintenanceStorage maintenanceStorage;
	
	private FacilityStorage facilityStorage;
	
	private ArrayList<MaintenanceReport> reports;
	
	private ArrayList<Facility> facilities;
	
	
	//constructor
	public MaintenanceManager(){
		this.maintenanceStorage = new MaintenanceStorage("maintenance.txt");
		this.facilityStorage = new FacilityStorage("facilities.txt");
		this.reports = new ArrayList<>();
		this.facilities = new ArrayList<>();
		
		loadReports();
		loadFacilities();
	}
	
	public void loadFacilities() {
		facilities.clear();
		facilities = facilityStorage.load();
	}
	
	private User currentUser;
	public void setCurrentUser(User user) {
			
		this.currentUser = user;
	}
	
	public boolean checkFacilityId(String facilityId) {
		
		boolean found = false;
		
		//check the input facilityId in the storage or not
		for(Facility f : facilities) {
			
			if(f.getFacilityId().equals(facilityId)) {
				
				found = true;
				break;
			}
		}
		
		if(!found) {	//the input facilityId not in the storage = false
			System.out.println("\nFalse facilityId!");
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public boolean checkReportId(String reportId) {
			
		boolean found = false;
			
			//check the input reportId in the storage or not
			for(MaintenanceReport r : reports) {
				
				if(r.getReportId().equals(reportId)) {
					
					found = true;
					break;
				}
			}
			
			if(!found) {	//the input facilityId not in the storage = false
				System.out.println("No such a ReportId is found");
				return false;
			}
			else {
				return true;
			}
		
	}
	
	public void createMaintenance(String facilityId, String userId, String description, String priority) { 
		String reportId = String.format("R%03d", reports.size() + 1);
		LocalDate reportDate = LocalDate.now();
		
		MaintenanceReport report = new MaintenanceReport(reportId, facilityId, userId, "UNASSIGNED", "UNASSIGNED", 
											description, reportDate, null, null, MaintenanceStatus.PENDING, priority);
	
		reports.add(report);
		saveReports();
	}
	
	public List<MaintenanceReport> getPendingReports() {
		List<MaintenanceReport> list = new ArrayList<>();
		for (MaintenanceReport r : reports) {
			if (r.getStatus() == MaintenanceStatus.PENDING) {
				list.add(r);
			}
		}
		return list;
	}
	
	public MaintenanceReport findReportById(String reportId) {
		for (MaintenanceReport r : reports) {
			if (r.getReportId().equals(reportId)) {
				return r;
			}
		}
		return null;
	}
	
	public void processAssignment(MaintenanceReport report, String adminId, String technician, boolean isApproved) {
		if (isApproved) {
			report.assignTo(adminId, technician);
			report.updateTaskStatus(MaintenanceStatus.IN_PROGRESS);
			} else {
				report.assignTo(adminId, "N/A");
				report.updateTaskStatus(MaintenanceStatus.REJECTED);
			}
		saveReports();
	}
	
	public boolean completeMaintenance(String reportId) {
		for (MaintenanceReport r : reports) {
			if (r.getReportId().equals(reportId)) {
				if (r.getStatus() != MaintenanceStatus.IN_PROGRESS) {
					return false;
				}
				r.updateTaskStatus(MaintenanceStatus.COMPLETED);
				r.setEndDate(LocalDate.now());

				saveReports();
				return true;
			}
		}
		return false;
	}
	
	public List<MaintenanceReport> getInProgressReports() {
		List<MaintenanceReport> list = new ArrayList<>();
		for (MaintenanceReport r : reports) {
			if (r.getStatus() == MaintenanceStatus.IN_PROGRESS) {
				list.add(r);
			}
		}
		return list;
	}
	
	public List<MaintenanceReport> getMaintenanceHistory(User user) {	
		List<MaintenanceReport> result = new ArrayList<>();
		
		if (user.getRole().equals("Admin")) {
			result.addAll(reports);
		} else {
			for (MaintenanceReport r : reports) {
				if (r.getReportedByUserId().equals(user.getUserId())) {
					result.add(r);
				}
			}
		}
		return result;
	}
	
	public void getFrequentIssues() {//WIP
		Map<String, Integer> issueCount = new HashMap<>();
	    
		Set<String> stopWords = Set.of("the", "a", "an");
	    
		for (MaintenanceReport r : reports) {
		    String desc = r.getDescription().toLowerCase();
		    String[] words = desc.split("\\W+");

		    for (String word : words) {
		        if (!stopWords.contains(word)) {
		            issueCount.put(word, issueCount.getOrDefault(word, 0) + 1);
		            break;
		        }
		    }
		
		}
		
		System.out.println("\nFrequent Issues:");

		for (Map.Entry<String, Integer> entry : issueCount.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}
	
	public void generateMaintenanceReport() { //generate maintenance performance report
		double i = 0.0;
		double days = 0.0;
		double average = 0.0;
		Map<String, Integer> facilityCount = new HashMap<>();
		
		for(MaintenanceReport r : reports) {
			if(!r.getStatus().equals(MaintenanceStatus.REJECTED)) {
				facilityCount.put(r.getFacilityId(), facilityCount.getOrDefault(r, 0) + 1);
			}
		}
		
		for(MaintenanceReport r : reports) {
			if(r.isCompleted()) {
				days += ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate());
				i++;
			}
		}
		
		average = days / i;
		
		System.out.println("=== Maintenance Performance Report ===\n");
		System.out.println("Number of Maintenance Cases per Facilitiy: ");
		for (Map.Entry<String, Integer> entry : facilityCount.entrySet()) {
	        System.out.println(entry.getKey() + " : " + entry.getValue());
	    }
		
		getFrequentIssues();
		
		System.out.println("\nTotal Maintenance Cases: " + reports.size());
		System.out.println("\nAverage Repair Time (day): " + average);
		
		System.out.println("\n=================================");
	}
	
	public List<Integer> getMaintenanceCounts(List<String> facilityIds) {
		List<Integer> counts = new ArrayList<>();
		for (String id : facilityIds) {
			int count = 0;
			for (MaintenanceReport r : reports) {
				if (!r.getFacilityId().equals(id))
					continue;
				if (r.getStatus() == MaintenanceStatus.REJECTED)
					continue;
				
				count++;
			}
			counts.add(count);
		}
		return counts;
	}
	
	public List<Double> getAverageRepairTime(List<String> facilityIds) {

		List<Double> avgList = new ArrayList<>();

		for (String id : facilityIds) {
			double totalDays = 0;
			int count = 0;

			for (MaintenanceReport r : reports) {

				if (!r.getFacilityId().equals(id))
					continue;

				if (r.getStatus() != MaintenanceStatus.COMPLETED)
					continue;

				if (r.getStartDate() == null || r.getEndDate() == null)
					continue;

				long days = ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate());

				totalDays += days;
				count++;
			}

			if (count == 0) {
				avgList.add(0.0);
			} else {
				avgList.add(totalDays / count);
			}
		}

		return avgList;
	}
	
	public void loadReports() {
		
		reports.clear();
		
		reports = maintenanceStorage.load();
		
	}
	
	public void saveReports() {
		maintenanceStorage.save(reports);
	}

}