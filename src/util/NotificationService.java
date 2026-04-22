package util;

import booking.Booking;

public class NotificationService {
	
	public NotificationService() {}

	public String generateBookingReminder(Booking b) {
		
		if (b == null) {
			return "Error: Booking data is missing.";
		}
		
		String bookingId = b.getBookingId();
		String facilityId = b.getFacilityId();
		String date = b.getDate();
		String startTime = b.getStartTime();
		
		String message = "Reminder: Booking " + bookingId + " for facility " + 
						facilityId + " is scheduled on " + date + " at " + startTime + ".";
		
		return message;
	}

}