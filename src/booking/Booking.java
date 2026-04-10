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
	
}
