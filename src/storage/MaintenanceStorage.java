package storage;

import java.io.*;
import java.util.ArrayList;

public class MaintenanceStorage extends BaseStorage {

    public MaintenanceStorage(String filePath) {
        super(filePath);
    }

    public MaintenanceReport[] load(int[] count){
    	MaintenanceReport[] list=new MaintenanceReport[200];
    	count[0]=0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);

                if(p.length<10){
                	continue;
                }
                
                String reportId = p[0];
                String facilityId = p[1];
                String reportedBy = p[2];
                String assignedTo = p[3];
                String description = p[4];
                String reportDate = p[5];
                String startDate = p[6];
                String endDate = p[7];
                String status = p[8];
                String priority = p[9];

                MaintenanceReport report=new MaintenanceReport(reportId,facilityId,reportedBy,assignedTo,
                								description,reportDate,startDate,endDate,status,priority);

                list[count[0]]=report;
                count[0]++;
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading maintenance file.");
        }

        return list;
    }

    public void save(MaintenanceReport[] list,int count){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

            for(int i=0;i<count;i++){
            	MaintenanceReport r=list[i];

            	if(r==null){
            		continue;
            	}

            	String line=r.getReportId()+","+
            	r.getFacilityId()+","+
            	r.getReportedByUserId()+","+
            	r.getAssignedToUserId()+","+
            	r.getDescription()+","+
            	r.getReportDate()+","+
            	r.getStartDate()+","+
            	r.getEndDate()+","+
            	r.getStatus()+","+
            	r.getPriority();

                bw.write(line);
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving maintenance file.");
        }
    }
}