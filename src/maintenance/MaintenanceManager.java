package maintenance;

import java.time.*;
import java.util.*;
import storage.*;
import user.*;
import java.util.*;
import java.time.LocalDate;
import storage.MaintenanceStorage;

public class MaintenanceManager {
	Scanner input = new Scanner(System.in);
	
	private MaintenanceStorage maintenanceStorage;
	
	private ArrayList<MaintenanceReport> reports;
	
	private int size;
	
	//constructor
	public MaintenanceManager(){
		this.reports = new ArrayList<>();
		
		this.maintenanceStorage = new MaintenanceStorage("D:\\MaintenanceStorage.txt");
		
		loadReports();
	}
	
	public void createMaintenanceReport() { //feedback
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter the facility Id (Ex: F001): ");
		String facilityId = sc.nextLine();

		System.out.println("Enter the issue description: ");
		String description = sc.nextLine();

		// generate reportId
		String reportId = "R" + (size + 1);
		String userId = "U001";

		LocalDate reportDate = LocalDate.now();
		
		MaintenanceReport report = new MaintenanceReport(reportId, facilityId, userId, null, 
				description, reportDate, null, null, "PENDING", "LOW");
		reports.add(report);
		size++;
	}
	
	public void assignMaintenance(String reportId, String userId) {//WIP
		MaintenanceReport report = null;
		int i = 0;  
		while(i < size) {  
			report = reports.get(i); 
			if(report.getReportId().equals(reportId)) { 
				//report.assignTo(userId);
				report.updateTaskStatus("IN_PROGRESS");
				reports.set(i, report);
				break;
			}else { 
				i++;
			} 
		} 
	}
	
	public void updateMaintenanceStatus(String reportId, String status) {
		MaintenanceReport report = null;
		int i = 0;  
		while(i < size) {  
			report = reports.get(i); 
			if(report.getReportId().equals(reportId)) { 
				report.updateTaskStatus(status);
				reports.set(i, report);
				break;
			}else { 
				i++;
			} 
		} 
	}
	
	public ArrayList<MaintenanceReport> getMaintenanceHistory(){
		return reports;
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

		 //count
		int assign = 0;
		int inProgress = 0;
		int complete = 0;
		int reject = 0;
		
		int choice;
		//Choose Time Range
		System.out.println("===Time Range===");
		System.out.println("1. Monthly");
		System.out.println("2. Semester");
		System.out.println("3. Yearly");
		do {
			System.out.println("Enter Your Choice: ");
			choice = input.nextInt();
			switch(choice) {
			case 1:
				
			case 2:
			
			case 3:
				
			}
		}while(choice <=1 && choice >=3);
		
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
		//WIP

	}
	
	public void loadReports() {
		
		reports.clear();
		
		reports = maintenanceStorage.load();
		
	}
	
	public void saveReports() {
		maintenanceStorage.save(reports);
	}

}