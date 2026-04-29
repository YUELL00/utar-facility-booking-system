package booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TimeSlot {

	private final LocalDate date;
	private final LocalTime startTime;
	private final LocalTime endTime;

	// constructor
	public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
		this.date = Objects.requireNonNull(date, "Date cannot be empty");
		this.startTime = Objects.requireNonNull(startTime, "Start time cannot be empty");
		this.endTime = Objects.requireNonNull(endTime, "End time cannot be empty");

		if (!startTime.isBefore(endTime)) {
			throw new IllegalArgumentException("Start time must be before end time");
		}
	}

	// check overlap
	public boolean overlaps(TimeSlot timeSlot) {
		
		//take today date
		LocalDate now = LocalDate.now();
		
		if (timeSlot == null) {
			return true;
		}
		else if( timeSlot.getDate().isBefore(now) || ! timeSlot.getStartTime().isBefore(timeSlot.getEndTime()) ) {
			System.out.println("Invalid Time Slot");
			return true;
		}
		else {
			return false;
		}

	}

	// getters
	public LocalDate getDate() { 
		return date; 
	}
	
	public LocalTime getStartTime() { 
		return startTime; 
	}
	
	public LocalTime getEndTime() { 
		return endTime; 
	}
	
}