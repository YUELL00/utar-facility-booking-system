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
		
		MaintenanceReport report = new MaintenanceReport(reportId, facilityId, userId, "UNASSIGNED",
									description, reportDate, null, null, MaintenanceStatus.PENDING, priority);
		
		reports.add(report);
		saveReports();
	}
	
	public void assignMaintenance() {//WIP
		MaintenanceReport report = null;
		int i = 0;  
		String currentUserId = currentUser.getUserId();
		int choice;
		String reportId;
		while(true) {
			boolean found = false;
			
			System.out.print("Enter report id(Ex:R001)/(Enter 0 to exit): ");
			reportId = input.nextLine();
			
			if (reportId.equals("0")) {
				System.out.println("\nExit...");
				return;
			}
			
			if(checkReportId(reportId)) {
				
				for(MaintenanceReport r : reports) {
				
					if(r.getReportId().equals(reportId)) {
						report = r;
						found = true;
						break;
					}else {
						i++;
					}
				}
				
				if(!found) {
					System.out.println("No report found");
					break;
				}			
			}
		}
		
		//Approve or Reject the Maintenance task
		System.out.println("1. Approve");
		System.out.println("2. Reject");
		do {
			System.out.println("Your choice: ");
			choice = Integer.parseInt(input.nextLine());
			switch(choice) {
			case 1:
				report.updateTaskStatus(MaintenanceStatus.APPROVED);
				report.assignTo(currentUserId);
				break;
			case 2:
				report.updateTaskStatus(MaintenanceStatus.REJECTED);
				report.assignTo(currentUserId);
				break;
			case 0:
				return;
			default:
				System.out.println("Invalid Input");
				break;
			}
		} while(choice < 0 && choice > 2);
				
		
		reports.set(i, report);
				
		saveReports();
		System.out.println("Tasks assigned successfully");
		//end
	}
	
	public void updateMaintenanceStatus() {
		MaintenanceReport report = null;
		int i = 0;  
		String currentUserId = currentUser.getUserId();
		int choice;
		String reportId;
		LocalDate startDate = null;
		while(true) {
			boolean found = false;
			
			System.out.print("Enter report id(Ex:R001)/(Enter 0 to exit): ");
			reportId = input.nextLine();
			
			if (reportId.equals("0")) {
				System.out.println("\nExit...");
				return;
			}
			
			if(checkReportId(reportId)) {
				
				for(MaintenanceReport r : reports) {
				
					if(r.getReportId().equals(reportId)) {
						report = r;
						found = true;
						break;
					}else {
						i++;
					}
				}
				
				if(!found) {
					System.out.println("No report found");
					break;
				}			
			}
		}
		System.out.print("\n====================");
		System.out.print("1. In Progress");
		System.out.print("2. Complete");
		System.out.print("0. Back");
		
		do {
			System.out.println("Choose the status: ");
			choice = Integer.parseInt(input.nextLine());
			switch(choice) {
			case 1:
				report.updateTaskStatus(MaintenanceStatus.IN_PROGRESS);
				while(true){
					System.out.print("\nEnter new date(Ex:2026-05-02): ");
					String inputDate = input.nextLine();
					
					try {
						
						startDate = LocalDate.parse(inputDate);
						
						if(startDate.isBefore(LocalDate.now())) {
							System.out.println("\nInvalid Date! (Past)");
							System.out.println("Please Re-enter: ");
							continue;	//restart from while loop header
						}
						break;
					}
					catch(Exception e) {
						System.out.println("\nFalse Date Format!");
						System.out.println("Please Re-enter: ");
					}
				}
				report.setStartDate(startDate);
				reports.set(i, report);
				break;
				
			case 2:
				report.updateTaskStatus(MaintenanceStatus.COMPLETED);
				report.setEndDate(LocalDate.now());
				reports.set(i, report);
				break;
			case 0:
				return;
			default:
				System.out.println("Invalid Input");
				break;
			}
		} while(choice < 0 && choice > 2);
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
	
	public void loadReports() {
		
		reports.clear();
		
		reports = maintenanceStorage.load();
		
	}
	
	public void saveReports() {
		maintenanceStorage.save(reports);
	}

}