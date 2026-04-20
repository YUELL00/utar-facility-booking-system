package storage;

import java.io.*;
import java.util.*;

import booking.Booking;

public class FacilityStorage extends BaseStorage{

	public FacilityStorage(String filePath) {
		super(filePath);
		}

	public ArrayList<Facility> load() {
		ArrayList<Facility> list = new ArrayList<>();

		try {
        	Scanner sc=new Scanner(new File(filePath));

        	while(sc.hasNextLine()){
        		String line=sc.nextLine();
        		String[] p=line.split(",",-1);

        		if(p.length<4){
        			continue;
        		}
	
	        	String facilityId=p[0];
	        	String facilityName=p[1];
	        	String facilityType=p[2];
	        	String location=p[3];
	
	        	Facility facility = new Facility(facilityId, facilityName, facilityType, location);
	
	        	list.add(facility);
	        }
	        sc.close();
        }
        catch(Exception e){
        	System.out.println("Error loading facilities.");
        }

		return list;
	}

	public void save(ArrayList<Facility> list) {
		try {
			PrintWriter writer=new PrintWriter(filePath);

			for(Facility f : list){

				String line = f.getFacilityId() + "," + f.getFacilityName()+"," +
							f.getFacilityType() + "," + f.getLocation();
				writer.println(line);
			}

			writer.close();
		}
		catch(Exception e){
			System.out.println("Error saving facilities.");
		}
	}
}