package maintenance;

import java.time.*;
import java.util.*;
import java.time.LocalDate;

import storage.MaintenanceStorage;

public class MaintenanceManager {
	private ArrayList<MaintenanceReport> reports;
	private int size;
	private MaintenanceStorage maintenanceStorage;
	
	public MaintenanceManager() {
		reports = new ArrayList<>();
		maintenanceStorage = new MaintenanceStorage("maintenance.txt");
		size = 0;
	}
	
	public void createMaintenanceReport() { //WIP
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
		//WIP
		//isCompleted
		return reports;
	}
	
	public void getFrequentIssues() {
		//WIP
	}
	
	public void generateMaintenanceReport() { //generate maintenance performance report
		//WIP
	}
	
	public void generateFrequentIssuesReport() { 
		//WIP
	}
	
	public void loadReports() { //WIP
		ArrayList<MaintenanceReport> loaded = maintenanceStorage.load();
		for(MaintenanceReport r : loaded) {
			reports.add(r);
			size++;
		}
	}
	
	public void saveReports() {
		maintenanceStorage.save(reports);
	}


}