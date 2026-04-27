package maintenance;

import java.time.*;
import java.util.*;
public class MaintenanceManager {
	private ArrayList<MaintenanceReport> reports;
	private int size;
	private MaintenanceStorage maintenanceStorage;
	
	public void createMaintenanceReport() { //WIP
		System.out.println("Enter the facility Id (Ex: F001): ");
		String facilityId = new Scanner(System.in).nextLine();
		System.out.println("Enter the issue description: ");
		String description = new Scanner(System.in).nextLine();
		
		//get the current date
		LocalDate now = LocalDate.now();
		String reportDate = now.toString();
		MaintenanceReport report = new MaintenanceReport(reportId, facilityId, userId, "NULL", description, reportDate, "NULL", "NULL", "PENDING", );
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
	
	public MaintenanceReport getMaintenanceHistory(){
		//WIP
		//isCompleted
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
		maintenanceStorage.save(reports,size);
	}


}