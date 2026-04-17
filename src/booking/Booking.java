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
	private String createdTime;
	private String lastModifiedTime;
	
	//constructor
	public Booking(String bookingId, String userId, String facilityId, TimeSlot timeSlot, String purpose) {
		this.bookingId = bookingId;
		this.userId = userId;
		this.facilityId = facilityId;
		this.timeSlot = timeSlot;
		this.purpose = purpose;
	}
	
	//constructor
	public Booking(String facilityId, TimeSlot timeSlot, String purpose) {
		this.facilityId = facilityId;
		this.timeSlot = timeSlot;
		this.purpose = purpose;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public boolean canModify() {
		if(status == "Pending") {
			return true;
		}
		else {
			return false;
		}
	}
	
	// get current/now date + time
	LocalDateTime now = LocalDateTime.now();
	
	public boolean canCancel() {
		// now date + time > booking start time
		if(now.isAfter(timeSlot)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void modifyBooking(TimeSlot newTimeSlot, String newPurpose) {
		timeSlot = newTimeSlot;
		purpose = newPurpose;
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
	
	public TimeSlot getTimeSlot() {
		return timeSlot;
	}
	
	public String getPurpose() {
		return purpose;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getCreatedTime() {
		return createdTime;
	}
	
	public String getLastModifiedTime() {
		return lastModifiedTime;
	}
	
}