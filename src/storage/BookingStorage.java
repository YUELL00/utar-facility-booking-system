package storage;

import java.io.*;

public class BookingStorage {

    private String filePath;

    public BookingStorage(String filePath) {
        this.filePath = filePath;
    }

    public Booking[] load(int[] count) {
        Booking[] list = new Booking[200];
        count[0] = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);

                TimeSlot slot = new TimeSlot(p[3], p[4], p[5]);

                list[count[0]] = new Booking(
                        p[0], p[1], p[2],
                        slot, p[6], p[7], p[8], p[9]
                );

                count[0]++;
            }

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
                TimeSlot t = b.getTimeSlot();

                bw.write(b.getBookingId() + "," +
                         b.getUserId() + "," +
                         b.getFacilityId() + "," +
                         t.getDate() + "," +
                         t.getStartTime() + "," +
                         t.getEndTime() + "," +
                         b.getPurpose() + "," +
                         b.getStatus() + "," +
                         b.getCreatedTime() + "," +
                         b.getLastModifiedTime());
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving bookings.");
        }
    }
}