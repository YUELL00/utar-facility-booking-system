package storage;

import java.io.*;
import java.util.ArrayList;

public class MaintenanceStorage extends BaseStorage {

    public MaintenanceStorage(String filePath) {
        super(filePath);
    }

    public ArrayList<MaintenanceReport> load() {
        ArrayList<MaintenanceReport> list = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);

                String reportId = p[0];
                String facilityId = p[1];
                String reportedBy = p[2];
                String assignedTo = p[3];
                String desc = p[4];
                String reportDate = p[5];
                String startDate = p[6];
                String endDate = p[7];
                String status = p[8];
                String priority = p[9];

                MaintenanceReport r = new MaintenanceReport(
                        reportId, facilityId, reportedBy, assignedTo,
                        desc, reportDate, startDate, endDate, status, priority
                );

                list.add(r);
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading maintenance file.");
        }

        return list;
    }

    public void save(ArrayList<MaintenanceReport> list) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

            for (MaintenanceReport r : list) {
                String line = r.getReportId() + "," +
                              r.getFacilityId() + "," +
                              r.getReportedByUserId() + "," +
                              r.getAssignedToUserId() + "," +
                              r.getDescription() + "," +
                              r.getReportDate() + "," +
                              r.getStartDate() + "," +
                              r.getEndDate() + "," +
                              r.getStatus() + "," +
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