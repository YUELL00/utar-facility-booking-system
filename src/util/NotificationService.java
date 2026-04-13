package util;

public class NotificationService {

	public static String generateBookingReminder(String bookingId, String facilityId, 
														String date, String startTime) {
		
		String message = "Reminder: Booking " + bookingId + " for facility " + 
						facilityId + " is scheduled on " + date + " at " + startTime + ".";
		
		return message;
	}

}