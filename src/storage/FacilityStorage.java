package storage;

import java.io.*;

public class FacilityStorage extends BaseStorage{

    private String filePath;

    public FacilityStorage(String filePath) {
    	super(filePath);
    }

    public Facility[] load(int[] count) {
        Facility[] list = new Facility[100];
        count[0] = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);

                if(p.length<4){
                	continue;
                }

                String facilityId=p[0];
                String facilityName=p[1];
                String facilityType=p[2];
                String location=p[3];

               	Facility facility = new Facility(facilityId,facilityName,
               										facilityType,location);

                list[count[0]]=facility;
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

                if(f==null){
                	continue;
                }

                String line=f.getFacilityId()+","+
                f.getFacilityName()+","+
                f.getFacilityType()+","+
                f.getLocation();

                bw.write(line);
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving facilities.");
        }
    }
}