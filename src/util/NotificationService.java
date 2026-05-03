package util;

import booking.Booking;
import booking.BookingStatus;
import booking.TimeSlot;

public class NotificationService {
	
	public NotificationService() {}

	public String generateBookingReminder(Booking b) {
		
		if (b == null) 
			return "Error: Booking data is missing.";

		TimeSlot ts = b.getTimeSlot();
		
		if (b.getStatus() == BookingStatus.REJECTED) {
			return "\nBooking Rejected: \nBooking: " + b.getBookingId() + 
					"\nFacility: " + b.getFacilityId() +
					"\nDate: " + ts.getDate() + "\nTime: " + ts.getStartTime() + " - " + ts.getEndTime() +
					"\nYour booking request has been rejected.";
		}
		
		if (b.getStatus() == BookingStatus.APPROVED) {
			return "\nReminder: \nBooking: " + b.getBookingId() + 
					"\nFacility: " + b.getFacilityId() +
					"\nDate: " + ts.getDate() + "\nTime: " + ts.getStartTime() + " - " + ts.getEndTime() +
					"\nPlease be on time.";
		}

		return null;
	}
}