package util;

import booking.Booking;
import booking.TimeSlot;

public class NotificationService {
	
	public NotificationService() {}

	public String generateBookingReminder(Booking b) {
		
		if (b == null) {
			return "Error: Booking data is missing.";
		}
		
		TimeSlot ts = b.getTimeSlot();
		
		if (ts == null) {
			return "Error: TimeSlot missing.";
		}
		
		return "Reminder: Booking " + b.getBookingId() + "\nFacility: " + b.getFacilityId() +
				"\nDate: " + ts.getDate() + "\nTime: " + ts.getStartTime() + " - " + ts.getEndTime() +
				"\nPlease be on time.";
	}

}