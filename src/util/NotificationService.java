package util;

import booking.Booking;

public class NotificationService {
	
	public NotificationService() {}

	public String generateBookingReminder(Booking b) {
		
		if (b == null) {
			return "Error: Booking data is missing.";
		}
		
		return "Reminder: Booking " + b.getBookingId() + "\nFacility: " + b.getFacilityId() +
				"\nDate: " + b.getDate() + "\nTime: " + b.getStartTime() + " - " + b.getEndTime() +
				"\nPlease be on time.";
	}

}