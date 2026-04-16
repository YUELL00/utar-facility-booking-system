package storage;

import java.io.*;

public class BookingStorage extends BaseStorage{

    private String filePath;

    public BookingStorage(String filePath) {
    	super(filePath);
    }

    public Booking[] load(int[] count) {
        Booking[] list = new Booking[200];
        count[0] = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);

                // Validation
                if (p.length < 10) {
                	continue; // skip invalid line
                }

                // Mapping
				String bookingId = p[0];
				String userId = p[1];
				String facilityId = p[2];
				String date = p[3];
				String startTime = p[4];
				String endTime = p[5];
				String purpose = p[6];
				String status = p[7];
				String createdTime = p[8];
				String lastModifiedTime = p[9];

                // Composition
				TimeSlot slot = new TimeSlot(date, startTime, endTime);
                // Object Creation
				Booking booking = new Booking(bookingId, userId, facilityId,
						slot, purpose, status, createdTime, lastModifiedTime);

				list[count[0]] = booking;
				count[0]++;

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading bookings.");
        }

        return list;
    }

    public void save(Booking[] list, int count) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

            for (int i = 0; i < count; i++) {
                Booking b = list[i];
                
                if (b == null) 
                	continue;
                
                TimeSlot t = b.getTimeSlot();

                String line = b.getBookingId() + "," +
							b.getUserId() + "," +
							b.getFacilityId() + "," +
							t.getDate() + "," +
							t.getStartTime() + "," +
							t.getEndTime() + "," +
							b.getPurpose() + "," +
							b.getStatus() + "," +
							b.getCreatedTime() + "," +
							b.getLastModifiedTime();

                bw.write(line);
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving bookings.");
        }
    }
}