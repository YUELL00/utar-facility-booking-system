package maintenance;

import java.time.*;
import java.util.*;
import storage.*;
import user.*;
public class MaintenanceManager {
	private ArrayList<MaintenanceReport> reports;
	private int size;
	private MaintenanceStorage maintenanceStorage;
	Scanner input = new Scanner(System.in);
	
	public void createMaintenanceReport() { //feedback
		String status, choice, description;
		String reportId = String.format("R%03d", size);
		String userId = UserManager.getCurrentUser().getUserId();
		MaintenanceReport report;
		
		//get the current date
		LocalDate now = LocalDate.now();
		String reportDate = now.toString();
		
		System.out.println("Enter the facility Id (Ex: F001): ");
		String facilityId = input.nextLine();
		System.out.println("===Issue Type===");
		System.out.println("1. Feedback");
		System.out.println("2. Maintenance");
		do {
			System.out.println("Choose the issue type you want to report: ");
			choice = input.nextLine();
			switch(choice) {
			case "1":
				System.out.println("Enter the issue description: ");
				description = input.nextLine();
				
				report = new MaintenanceReport(reportId, facilityId, userId, description, reportDate);
				System.out.println("Feedback created successfully");
				break;
				
			case "2":
				System.out.println("Enter the issue description: ");
				description = input.nextLine();
				
				report = new MaintenanceReport(reportId, facilityId, userId, "NULL", description, reportDate, "NULL", "NULL", "PENDING");
				reports.add(report);
				size++;
				saveReports();
				System.out.println("Report created successfully");
				break;
				
			default:
				System.out.println("Invalid Input");
				break;
			}
		}while(!(choice.equals("1")) && !(choice.equals("2")));
		//end
	}
	
	public void assignMaintenance(String reportId, String userId) {//WIP
		MaintenanceReport report = null;
		String status = null;
		String choice;
		int i = 0;
		for(MaintenanceReport r : reports) {
			if(r.getReportId().equals(reportId)) {
				report = r;
				break;
			} else {
				i++;
			}
		}
		
		//Approve or Reject the Maintenance task
		System.out.println("1. Approve");
		System.out.println("2. Reject");
		do {
			System.out.println("Your choice: ");
			choice = input.nextLine();
			switch(choice) {
			case "1":
				status = "APPROVED";
				break;
			case "2":
				status = "REJECTED";
				break;
			default:
				System.out.println("Invalid Input");
				break;
			}
		}while(!(choice.equals("1")) && !(choice.equals("2")));
		
		report.assignTo(userId);
		report.updateTaskStatus(status);
		reports.set(i, report);
			
		saveReports();
		System.out.println("Tasks assigned successfully");
		//end
	}
	
	public void updateMaintenanceStatus(String reportId, String status) {//WIP
		MaintenanceReport report = null;
		int i = 0;
		for(MaintenanceReport r : reports) {
			if(r.getReportId().equals(reportId)) {
				report = r;
				break;
			} else {
				i++;
			}
		}
		if(status == "ASSIGNED"){
			System.out.println("Enter the start date (ex: yyyy-mm-dd): ");
			String startDate = input.nextLine();
			
			report.updateTaskStatus(status);
			report.setStartDate(startDate);
		}else if(status == "IN_PROGRESS") {
			report.updateTaskStatus(status);
		}else if(status == "COMPLETED") {
			LocalDate now = LocalDate.now();
			String endDate = now.toString();
			
			report.updateTaskStatus(status);
			report.setEndDate(endDate);
		}
		
		reports.set(i, report);
		System.out.println("Tasks status updated");
	}
	
	public ArrayList<MaintenanceReport> getMaintenanceHistory(){
		String userId = UserManager.getCurrentUser().getUserId();
		ArrayList<MaintenanceReport> history = new ArrayList<>();
		for(MaintenanceReport r : reports) {  
			//find by using user id
			if(r.getReportedByUserId().equals(userId)) {
				history.add(r);
			}
			if(history.size() == 0) {
				System.out.println("No any history report. ");
			}
			return history;
		}
		//end
	}
	
	public void getFrequentIssues() {//WIP
	    Map<String, Integer> wordCount = new HashMap<>();
	    
	    Set<String> stopWords = Set.of("the", "is", "and", "a", "to", "of", "in", "on");
	    
		for(MaintenanceReport r : reports) {
			String desc = r.getDescription().toLowerCase();
	        
	        String[] words = desc.split("\\W+"); // 按非字母分割
	        
	        for (String word : words) {
	            if (word.isEmpty() || stopWords.contains(word)) {
	            	continue;
	            }

	            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
	        }
		}
		
		System.out.println("Frequent Issues:");

		for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
		        System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}
	
	public void generateMaintenanceReport() { //generate maintenance performance report
		int assign = 0;
		int inProgress = 0;
		int complete = 0;
		int reject = 0; //count
		for(MaintenanceReport r : reports) {
			switch(r.getStatus()) {
			case "ASSIGNED":
				assign++;
				break;
			case "IN_PROGRESS":
				inProgress++;
				break;
			case "COMPLETED":
				complete++;
				break;
			case "REJECTED":
				reject++;
				break;
			}
		}
		System.out.println("=== Maintenance Preformance Report ===");
		System.out.println("Assigned Task: " + assign);
		System.out.println("Rejected Task: " + reject);
		System.out.println("Task In Progress: " + inProgress);
		System.out.println("Completed Task: " + complete);
		
		getFrequentIssues();
	}
	
	
	public void loadReports() { //WIP
		int[] countB = new int[1];
		
		//array <-- file
		MaintenanceReport[] listB = MaintenanceStorage.load(countB);
		
		reports.clear();
		
		for(int i = 0; i < countB[0]; i++) {
			reports.add(listB[i]);
			size++;
		}
	}
	public void saveReports() {
		MaintenanceReport[] arr = new MaintenanceReport[size];
		
		for(int i = 0; i < size; i++) {
			arr[i] = reports.get(i);
		}
		
		maintenanceStorage.save(reports,size);
	}


}
