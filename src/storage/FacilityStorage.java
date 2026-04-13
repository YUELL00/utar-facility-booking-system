package storage;

import java.io.*;

public class FacilityStorage {

    private String filePath;

    public FacilityStorage(String filePath) {
        this.filePath = filePath;
    }

    public Facility[] load(int[] count) {
        Facility[] list = new Facility[100];
        count[0] = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);

                list[count[0]] = new Facility(
                        p[0], p[1], p[2], p[3]
                );

                count[0]++;
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading facilities.");
        }

        return list;
    }

    public void save(Facility[] list, int count) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

            for (int i = 0; i < count; i++) {
                Facility f = list[i];

                bw.write(f.getFacilityId() + "," +
                         f.getFacilityName() + "," +
                         f.getFacilityType() + "," +
                         f.getLocation());
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving facilities.");
        }
    }
}