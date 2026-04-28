package storage;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import booking.Booking;
import booking.TimeSlot;

public class BookingStorage extends BaseStorage{

	public BookingStorage(String filePath) {
    	super(filePath);
    }

	public ArrayList<Booking> load() {
		ArrayList<Booking> list = new ArrayList<>();

        try {
        	Scanner sc = new Scanner(new File(filePath));

        	while(sc.hasNextLine()){
        		String line = sc.nextLine();
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
				LocalDateTime createdTime;
				LocalDateTime lastModifiedTime;
				
				// parse createdTime
				try {
					createdTime = LocalDateTime.parse(p[8]);
				} catch (Exception e) {
					createdTime = null;
				}

				// parse lastModifiedTime
				try {
					lastModifiedTime = LocalDateTime.parse(p[9]);
				} catch (Exception e) {
					lastModifiedTime = null;
				}

                // Composition
				TimeSlot slot = new TimeSlot(date, startTime, endTime);
                // Object Creation
				Booking booking = new Booking(bookingId, userId, facilityId,
						slot, purpose, status, createdTime, lastModifiedTime);

				list.add(booking);
        	}
        	
        	sc.close();
        }
        catch (Exception e) {
            System.out.println("Error loading bookings.");
        }

        return list;
    }

    public void save(ArrayList<Booking> list){
        try {
        	PrintWriter writer = new PrintWriter(filePath);

        	for (Booking b : list) {
                
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

                writer.println(line);
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving bookings.");
        }
    }
}