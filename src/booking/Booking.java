package booking;

// get current/now date + time
import java.time.LocalDateTime;

public class Booking {

	private String bookingId;
	private String userId;
	private String facilityId;
	private TimeSlot timeSlot;
	private String purpose;
	private String status;
	private LocalDateTime createdTime;
	private LocalDateTime lastModifiedTime;
	
	//constructor
	public Booking(String bookingId, String userId, String facilityId, TimeSlot timeSlot, 
			String purpose, String status, LocalDateTime createdTime, LocalDateTime lastModifiedTime) {
		this.bookingId = bookingId;
		this.userId = userId;
		this.facilityId = facilityId;
		this.timeSlot = timeSlot;
		this.purpose = purpose;
		this.status = status;
		this.createdTime = createdTime;
		this.lastModifiedTime = lastModifiedTime;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public boolean canModify() {
		if(status.equals("Pending")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean canCancel() {
		
		// get current/now date + time
		LocalDateTime now = LocalDateTime.now();
		
		// get booking date + time
		LocalDateTime start = LocalDateTime.of(timeSlot.getDate(), timeSlot.getStartTime());
		
		// now date + time > booking start time
		if(now.isAfter(start)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void modifyBooking(TimeSlot timeSlot, String purpose, LocalDateTime lastModifiedTime) {
		this.timeSlot = timeSlot;
		this.purpose = purpose;
		this.lastModifiedTime = lastModifiedTime;
	}
	
	
	//getter
	public String getBookingId() {
		return bookingId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getFacilityId() {
		return facilityId;
	}
	
	public LocalDateTime getDate() {
	    return timeSlot.getDate();
	}

	public LocalDateTime getStartTime() {
	    return timeSlot.getStartTime();
	}

	public LocalDateTime getEndTime() {
	    return timeSlot.getEndTime();
	}
	
	public TimeSlot getTimeSlot() {
		return timeSlot;
	}
	
	public String getPurpose() {
		return purpose;
	}
	
	public String getStatus() {
		return status;
	}
	
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	
	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}
	
}