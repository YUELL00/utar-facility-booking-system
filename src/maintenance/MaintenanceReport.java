package maintenance;

import java.time.LocalDate;

public class MaintenanceReport {
	
	private String reportId;
	public String getReportId() {
		return reportId;
	}
	
	private String facilityId;
	public String getFacilityId() {
		return facilityId;
	}
	
	private String reportedByUserId;
	public String getReportedByUserId() {
		return reportedByUserId;
	}
	
	private String assignedByUserId;
	public String getAssignedByUserId() {
		return assignedByUserId;
	}
	
	private String description;
	public String getDescription() {
		return description;
	}
	
	private LocalDate reportDate;
	public LocalDate getReportDate() {
		return reportDate;
	}
	
	private LocalDate startDate;
	public LocalDate getStartDate() {
		return startDate;
	}
	
	private LocalDate endDate;
	public LocalDate getEndDate() {
		return endDate;
	}
	
	private String status;
	public String getStatus() {
		return status;
	}
	
	private String priority;
	public String getPriority() {
		return priority;
	}
	
	//constructor
	public MaintenanceReport(String reportId, String facilityId, String reportedByUserId, String assignedByUserId, 
			String description, LocalDate reportDate, LocalDate startDate, LocalDate endDate, String status, String priority) {
		this.reportId = reportId;
		this.facilityId = facilityId;
		this.reportedByUserId = reportedByUserId;
		this.assignedByUserId = assignedByUserId;
		this.description = description;
		this.reportDate = reportDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.priority = priority;
	}
	
	public void updateTaskStatus(String status) {
		this.status = status;
	}
	
	public void assignTo(String userId) {
		//WIP
		this.assignedByUserId = userId;
	}
	
	public boolean isCompleted() { //WIP 
		if("COMPLETED".equals(status)) {
			return true;
		} else {
			return false;
		}
	}
	
}