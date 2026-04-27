package storage;

import java.io.*;
import java.util.*;

import maintenance.MaintenanceReport;

public class MaintenanceStorage extends BaseStorage{

	public MaintenanceStorage(String filePath){
		super(filePath);
	}
	
	public ArrayList<MaintenanceReport> load(){
		ArrayList<MaintenanceReport> list = new MaintenanceReport<>();
		
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
				String reportDate=p[5];
				String startDate=p[6];
				String endDate=p[7];
				String status=p[8];
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
			PrintWriter writer=new PrintWriter(filePath);
			
			for(MaintenanceReport r : list){

				
				String line = r.getReportId()+","+
				r.getFacilityId()+","+
				r.getReportedByUserId()+","+
				r.getAssignedToUserId()+","+
				r.getDescription()+","+
				r.getReportDate()+","+
				r.getStartDate()+","+
				r.getEndDate()+","+
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