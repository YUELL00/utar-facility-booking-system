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
		this.reports = new ArrayList<>();
		this.facilities = new ArrayList<>();
		
		this.maintenanceStorage = new MaintenanceStorage("maintenance.txt");
		this.facilityStorage = new FacilityStorage("facilities.txt");
		
		loadReports();
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
	
	public void createMaintenanceReport() { //feedback
		String currentUserId = currentUser.getUserId();
		LocalDate reportDate = null, startDate = null, endDate = null;
		String facilityId, description;
		int choice;
		
		System.out.println("===Issue Type===");
		System.out.println("1. Feedback");
		System.out.println("2. Maintenance");
		System.out.println("0. Back");
		
		do {
			System.out.println("Choose the issue type you want to report: ");
			choice = input.nextInt();
			switch(choice) {
			case 1: //feedback
				String feedbackId = String.format("FB%03d", reports.size() + 1);
				
				while(true) {
					System.out.println("Enter the facility Id (Ex: F001): ");
					facilityId = input.nextLine();
					if(!(checkFacilityId(facilityId))) {
						System.out.println("Please Re-enter: ");
					}else {
						break;
					}
				}
				
				System.out.println("Enter the issue description: ");
				description = input.nextLine();
				
				reportDate = LocalDate.now();
				MaintenanceReport feedback = new MaintenanceReport(feedbackId, facilityId, currentUserId, description, reportDate);
				System.out.println("Feedback created successfully");
				break;
				
			case 2: //Maintenance issue
				String reportId = String.format("R%03d", reports.size() + 1);
				
				while(true) {
					System.out.println("Enter the facility Id (Ex: F001): ");
					facilityId = input.nextLine();
					if(!(checkFacilityId(facilityId))) {
						System.out.println("Please Re-enter: ");
					}else {
						break;
					}
				}
				
				System.out.println("Enter the issue description: ");
				description = input.nextLine();
				
				reportDate = LocalDate.now();
				MaintenanceStatus status = MaintenanceStatus.PENDING;
				MaintenanceReport report = new MaintenanceReport(reportId, facilityId, currentUserId, "NULL", description, reportDate, startDate, endDate, status, "LOW");
				reports.add(report);
				saveReports();
				break;
			
			case 0:
				return;
				
			default:
				System.out.println("Invalid Input");
				break;
			}
		}while(choice < 0 && choice > 2);
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
			
						found = true;
						break;
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
			choice = input.nextInt();
			switch(choice) {
			case 1:
				report.updateTaskStatus(MaintenanceStatus.APPROVED);
				break;
			case 2:
				report.updateTaskStatus(MaintenanceStatus.REJECTED);
				break;
			case 0:
				return;
			default:
				System.out.println("Invalid Input");
				break;
			}
		}while(choice < 0 && choice > 2);
				
		report.assignTo(currentUserId);
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
		do {
			System.out.println("Enter report id (ex:R001): ");
			reportId = input.nextLine();
			if(checkReportId(reportId)) {
				for(MaintenanceReport r : reports) {
					if(r.getReportId().equals(reportId)) {
						report = r;
						break;
					}else {
						i++;
					}
				}
			}else {
				System.out.println("Please Re-enter: ");
			}
		}while(!checkReportId(reportId));
		
		
	}
	
	public void getMaintenanceHistory(User currentUser){
		
		int i = 0;
		if(currentUser.getRole().equals("Admin")) {
			for(MaintenanceReport r : reports) {  
				System.out.println(r);
				i++;
			}
		}else {
			//find by using user id
			for(MaintenanceReport r : reports) {
				if(r.getReportedByUserId().equals(currentUser.getUserId())) {
					System.out.println(r);
					i++;
				}
			}
		}
		if(i == 0) {
			System.out.println("\nThere is no any report yet.");
		}else {
			System.out.println("\nAll report are shown.");
		}
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
		
		System.out.println("Frequent Issues:");

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