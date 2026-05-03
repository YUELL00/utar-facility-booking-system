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
		
				String reportId=p[0];
				String facilityId=p[1];
				String reportedBy=p[2];
				String assignedTo=p[3];
				String description=p[4];
				LocalDate reportDate = LocalDate.parse(p[5]);
				LocalDate startDate = p[6].equals("NULL") || p[6].isEmpty()
						? null
						: LocalDate.parse(p[6]);
				LocalDate endDate = p[7].equals("NULL") || p[7].isEmpty()
						? null
						: LocalDate.parse(p[7]);
				MaintenanceStatus status = MaintenanceStatus.valueOf(p[8].toUpperCase());
				String priority=p[9];
				
				MaintenanceReport report = new MaintenanceReport(reportId,facilityId,reportedBy,assignedTo,
											description,reportDate,startDate,endDate,status,priority);
				
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

				
				String line = r.getReportId()+","+
				r.getFacilityId()+","+
				r.getReportedByUserId()+","+
				r.getAssignedByUserId()+","+
				r.getDescription()+","+
				r.getReportDate()+","+
				(r.getStartDate() == null ? "NULL" : r.getStartDate()) + ","+
				(r.getEndDate() == null ? "NULL" : r.getEndDate()) + ","+
				r.getStatus()+","+
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