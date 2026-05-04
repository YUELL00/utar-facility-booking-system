package storage;

import java.io.*;
import java.util.*;

import booking.BookingStatus;

import java.time.LocalDate;

import maintenance.MaintenanceReport;
import maintenance.MaintenanceStatus;

public class MaintenanceStorage extends BaseStorage{

	public MaintenanceStorage(String filePath){
		super(filePath);
	}
	
	public ArrayList<MaintenanceReport> load(){
		ArrayList<MaintenanceReport> list = new ArrayList<>();
		
		try{
			Scanner sc = new Scanner(new File(filePath));
			
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				String[] p = line.split(",",-1);
				
				if(p.length < 10){
					continue;
				}
		
				String reportId = p[0];
				String facilityId = p[1];
				String reportedBy = p[2];
				String assignedTo = p[3];
				String technicianName = p[4];
				String description = p[5];
				LocalDate reportDate = LocalDate.parse(p[6]);
				LocalDate startDate;
				if (p[7].equals("null") || p[7].equals("NULL") || p[7].isEmpty()) {
					startDate = null;
				} else {
					startDate = LocalDate.parse(p[7]);
				}
				LocalDate endDate;
				if (p[8].equals("null") || p[8].equals("NULL") || p[8].isEmpty()) {
					endDate = null;
				} else {
					endDate = LocalDate.parse(p[8]);
				}
				MaintenanceStatus status = MaintenanceStatus.valueOf(p[9].toUpperCase());
				String priority=p[10];
				
				MaintenanceReport report = new MaintenanceReport(reportId,facilityId,reportedBy,assignedTo,
											technicianName,description,reportDate,startDate,endDate,status,priority);
				
				list.add(report);
			}
	
			sc.close();
		}
		catch(Exception e){
			System.out.println("Error loading maintenance file.");
		}
		
		return list;
	}
	
	public void save(ArrayList<MaintenanceReport> list){
		try{
			PrintWriter writer = new PrintWriter(filePath);
			
			for(MaintenanceReport r : list){

				String startDateStr;
				if (r.getStartDate() == null) {
					startDateStr = "NULL";
				} else {
					startDateStr = r.getStartDate().toString();
				}

				String endDateStr;
				if (r.getEndDate() == null) {
				endDateStr = "NULL";
				} else {
					endDateStr = r.getEndDate().toString();
				}

				String technicianName;
				if (r.getTechnicianName() == null || r.getTechnicianName().isEmpty()) {
					technicianName = "NULL";
				} else {
					technicianName = r.getTechnicianName();
				}

				String line =
				r.getReportId() + "," +
				r.getFacilityId() + "," +
				r.getReportedByUserId() + "," +
				r.getAssignedByUserId() + "," +
				technicianName + "," +
				r.getDescription() + "," +
				r.getReportDate() + "," +
				startDateStr + "," +
				endDateStr + "," +
				r.getStatus() + "," +
				r.getPriority();
				
				writer.println(line);
			}
				
			writer.close();
		}
		catch(Exception e){
			System.out.println("Error saving maintenance file.");
		}
	}
}