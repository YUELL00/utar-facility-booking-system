package booking;

import java.util.*;
import java.time.*;//get current/now date + time
import user.User;
import facility.Facility;
import storage.*;

public class BookingManager{
	
	Scanner input = new Scanner(System.in);
	private ArrayList<Booking> bookings;
	private ArrayList<Facility> facilities;
	private BookingStorage bookingStorage;
	private FacilityStorage facilityStorage;
	private User currentUser;
	private List<Integer> tempCounts;
	private List<Integer> tempTypeCounts;
	private List<Double> tempHours;
	
	//constructor
	public BookingManager() {
		
		this.bookings = new ArrayList<>();
		this.facilities = new ArrayList<>();
		
		this.bookingStorage = new BookingStorage("booking.txt");
		this.facilityStorage = new FacilityStorage("facilities.txt");
		
	}
	
	//current userId
	
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}

	public boolean isBooked(String facilityId, TimeSlot timeSlot) {
		for (Booking b : bookings) {
			if (!b.getFacilityId().equals(facilityId)) continue;
			
			if (b.getStatus() != BookingStatus.APPROVED && b.getStatus() != BookingStatus.PENDING)
				continue;
			
			if (b.getTimeSlot().overlaps(timeSlot)) {
				return true;
			}
		}
		return false;
	}
	
	// (case 1) 
	public void createBooking() {
		
		String currentUserId = currentUser.getUserId();
		
		//bookingId, output: BXXX , 3int , not enough = fill with 0
		String bookingId = String.format("B%03d", bookings.size() + 1);
		
		//facilityId
		String trueFacilityId = null;
		
		while(true) {
			System.out.print("\nEnter facilityId(Ex:F001)/(Enter 0 to exit): ");
			String facilityId = input.nextLine();
			
			if (facilityId.equals("0")) {
				System.out.println("\nExit Creation.");
				return;
			}
			
			if(checkFacilityId(facilityId)) {
				
				trueFacilityId = facilityId;
				break;
			}
			System.out.println("Please Re-enter: ");
		}
		
		
		//timeSlot
		//must declare outside loops (cannot pass data if declare inside loops)
		LocalDate date = null;
		LocalTime startTime = null;
		LocalTime endTime = null;
		TimeSlot trueTimeSlot = null;
		
		while(true) {
			
			//check date
			while(true) {
				System.out.print("\nEnter date(Ex:2026-05-01): ");
				String inputDate = input.nextLine();
				
				try {
					
					date = LocalDate.parse(inputDate);
					
					if(date.isBefore(LocalDate.now())) {
						System.out.println("\nInvalid Date! (Past)");
						System.out.println("Please Re-enter: ");
						continue;	//restart from while loop header
					}
					break;
				}
				catch(Exception e) {
					System.out.println("\nFalse Date Format!");
					System.out.println("Please Re-enter: ");
				}
			}
			
			//check start time
			while(true) {
				System.out.print("\nEnter start time(Ex:10:00): ");
				String inputStartTime = input.nextLine();
				
				try {
					startTime = LocalTime.parse(inputStartTime);
					
					LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
					
					if(startDateTime.isBefore(LocalDateTime.now())) {
						System.out.println("\nInvalid Time! (Past)");
						System.out.println("Please Re-enter: ");
						continue;
					}
					break;
				}
				catch(Exception e) {
					System.out.println("\nFalse Time Format!");
					System.out.println("Please Re-enter: ");
				}
			}
			
			//check end time
			while(true) {			
				System.out.print("\nEnter end time(Ex:12:00): ");
				String inputEndTime = input.nextLine();
				
				try {
					endTime = LocalTime.parse(inputEndTime);
					
					LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
					
					if(endDateTime.isBefore(LocalDateTime.now())) {
						System.out.println("\nInvalid Time! (Past)");
						System.out.println("Please Re-enter: ");
						continue;
					}else if(endTime.isBefore(startTime)) {
						System.out.println("\nInvalid Time! (end time cannot before start time)");
						System.out.println("Please Re-enter: ");
						continue;
					}
					break;
				}
				catch(Exception e) {
					System.out.println("\nFalse Time Format!");
					System.out.println("Please Re-enter: ");
				}
			}
			
			
			try {
				TimeSlot timeSlot = new TimeSlot(date, startTime, endTime);
				
				if( checkConflict(null, trueFacilityId, timeSlot) ) {
					
					//save true timeSlot
					trueTimeSlot = timeSlot;
					
					// no conflict --> exit loop
					break;
				}
			}
			catch(Exception e) {
				System.out.println("\nFalse Time Format!");
				System.out.println("Please Re-enter: ");
			}
		}	
		
		
		//purpose
		System.out.print("\nEnter purpose: ");
		String purpose = input.nextLine();
		
		//status
		BookingStatus status = BookingStatus.PENDING;
		
		//createdTime
		LocalDateTime createdTime = LocalDateTime.now().withSecond(0).withNano(0);
		
		//lastModifiedTime
		LocalDateTime lastModifiedTime = LocalDateTime.now().withSecond(0).withNano(0);
		
		Booking b = new Booking(bookingId, currentUserId, trueFacilityId, trueTimeSlot, 
					purpose, status, createdTime, lastModifiedTime);
			
		bookings.add(b);
			
		saveBookings();
			
		System.out.println("\nBooking " + bookingId + " Created Successfully.");
		
	}
	
	// (case 2) 
	public void modifyBooking() {
		
		String currentUserId = currentUser.getUserId();
		
		//bookingId
		String trueBookingId = null;
		
		while(true) {
			boolean found = false;
			boolean privilege = false;
			
			System.out.print("\nEnter bookingId(Ex:B001)/(Enter 0 to exit): ");
			String bookingId = input.nextLine();
			
			if (bookingId.equals("0")) {
				System.out.println("\nExit Modification.");
				return;
			}
			
			if(checkBookingId(bookingId)) {
				
				for(Booking b : bookings) {
				
					if(b.getBookingId().equals(bookingId)) {
			
						found = true;
						
						if(!b.getUserId().equals(currentUserId)) {
							System.out.println("\nNo Privileges to Modify " + bookingId + "!");
							break;
						}
						else {
							privilege = true;
						}
						
						if(!b.canStatus()) {
							System.out.println("\nCannot Modify! (booking " + bookingId + " has already been " 
						+ b.getStatus().toString().toLowerCase() + ")");
							privilege = false;
							break;
						}
						else {
							privilege = true;
						}
						
						if(!b.canTime()) {
							System.out.println("\nCannot Modify! (booking " + bookingId + " has already passed)");
							privilege = false;
							break;
						}
						else {
							privilege = true;
						}
						
						break;
					}
				}
				
				if(!privilege) {
					return;	// stop this whole method
				}
				
				if(found) {
					trueBookingId = bookingId;
					break;
				}			
			}
		}
			
		
		//timeSlot
		//must declare outside loops (cannot pass data if declare inside loops)
		LocalDate date = null;
		LocalTime startTime = null;
		LocalTime endTime = null;
		TimeSlot trueTimeSlot = null;
		
		while(true) {
			
			//check date
			while(true) {
				System.out.print("\nEnter new date(Ex:2026-05-02): ");
				String inputDate = input.nextLine();
				
				try {
					
					date = LocalDate.parse(inputDate);
					
					if(date.isBefore(LocalDate.now())) {
						System.out.println("\nInvalid Date! (Past)");
						System.out.println("Please Re-enter: ");
						continue;	//restart from while loop header
					}
					break;
				}
				catch(Exception e) {
					System.out.println("\nFalse Date Format!");
					System.out.println("Please Re-enter: ");
				}
			}
			
			//check start time
			while(true) {
				System.out.print("\nEnter new start time(Ex:13:00): ");
				String inputStartTime = input.nextLine();
				
				try {
					startTime = LocalTime.parse(inputStartTime);
					
					LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
					
					if(startDateTime.isBefore(LocalDateTime.now())) {
						System.out.println("\nInvalid Time! (Past)");
						System.out.println("Please Re-enter: ");
						continue;
					}
					break;
				}
				catch(Exception e) {
					System.out.println("\nFalse Time Format!");
					System.out.println("Please Re-enter: ");
				}
			}
			
			//check end time
			while(true) {			
				System.out.print("\nEnter new end time(Ex:15:00): ");
				String inputEndTime = input.nextLine();
				
				try {
					endTime = LocalTime.parse(inputEndTime);
					
					LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
					
					if(endDateTime.isBefore(LocalDateTime.now())) {
						System.out.println("\nInvalid Time! (Past)");
						System.out.println("Please Re-enter: ");
						continue;
					}else if(endTime.isBefore(startTime)) {
						System.out.println("\nInvalid Time! (end time cannot before start time)");
						System.out.println("Please Re-enter: ");
						continue;
					}
					break;
				}
				catch(Exception e) {
					System.out.println("\nFalse Time Format!");
					System.out.println("Please Re-enter: ");
				}
			}
			
			
			try {
				TimeSlot timeSlot = new TimeSlot(date, startTime, endTime);
				
				//check new input
				if(checkConflict(trueBookingId, getFacilityIdByBookingId(trueBookingId), timeSlot)) {
				
					trueTimeSlot = timeSlot;
					break;
				}
			}
			catch(Exception e) {
				System.out.println("\nFalse Time Format!");
				System.out.println("Please Re-enter: ");
			}
		}		
		
		//purpose
		System.out.print("\nEnter new purpose: ");
		String purpose = input.nextLine();
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(trueBookingId)) {
				
				LocalDateTime lastModifiedTime = LocalDateTime.now().withSecond(0).withNano(0);
							
				b.modifyBooking(trueTimeSlot, purpose, lastModifiedTime);
							
				saveBookings();
				
				System.out.println("\nBooking " + trueBookingId + " Modified Successfully.");

				break;
			}
		}
	}
		
	// (case 3) 
	public void cancelBooking() {
		
		String currentUserId = currentUser.getUserId();
		
		//bookingId
		String trueBookingId;
		
		while(true) {
			
			boolean privilege = false;
			boolean found = false;
			
			System.out.print("\nEnter bookingId(Ex:B001)/(Enter 0 to exit): ");
			String bookingId = input.nextLine();
			
			if (bookingId.equals("0")) {
				System.out.println("\nExit Cancellation.");
				return;
			}
			
			if(checkBookingId(bookingId)) {
			
				for(Booking b : bookings) {
					
					if(b.getBookingId().equals(bookingId)) {
						
						found = true;
						
						if(!b.getUserId().equals(currentUserId)) {
							System.out.println("\nNo Privileges to Cancel " + bookingId + "!");
							break;
						}
						else {
							privilege = true;
						}
						
						if(!b.canStatus()) {
							System.out.println("\nCannot Cancel! (booking " + bookingId + " has already been " 
						+ b.getStatus().toString().toLowerCase() + ")");
							privilege = false;
							break;
						}
						else {
							privilege = true;
						}
						
						if(!b.canTime()) {
							System.out.println("\nCannot Cancel! (booking " + bookingId + " has already passed)");
							privilege = false;
							break;
						}
						else {
							privilege = true;
						}
						
						break;
					}
				}
				
				if(!privilege) {
					return;	// stop this whole method
				}
				
				if(found) {
					trueBookingId = bookingId;
					break;
				}
			}
		}
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(trueBookingId)) {
				
				b.setStatus(BookingStatus.CANCELLED);
				
				saveBookings();
				
				System.out.println("\nBooking " + trueBookingId + " Cancelled Successfully.");

				break;
			}
		}
	}
	
	// (case 4)
	public void showMyBookings(User currentUser){
		
		int i = 0;
		
		for(Booking b : bookings) {
			
			if(b.getUserId().equals(currentUser.getUserId())) {
				i++;
				System.out.println(b);
			}
		}
		
		if(i == 0) {
			System.out.println("\nYou do not have any booking yet.");
		}
		else {
			System.out.println("\nAll your bookings are shown.");
		}
	}

	
	//admin only (case 5) 
	public void showAllBookings() {
		
		for(Booking b : bookings) {
			System.out.println(b);
		}
		System.out.println("\nAll bookings are shown.");
	}
	
	//admin only (case 6)
	public void approveBooking() {
		
		//bookingId
		String trueBookingId;
		
		while(true) {
			
			boolean privilege = false;
			boolean found = false;
			
			System.out.print("\nEnter bookingId(Ex:B001)/(Enter 0 to exit): ");
			String bookingId = input.nextLine();
			
			if (bookingId.equals("0")) {
				System.out.println("\nExit Approval.");
				return;
			}
			
			if(checkBookingId(bookingId)) {
			
				for(Booking b : bookings) {
					
					if(b.getBookingId().equals(bookingId)) {
						
						found = true;
						
						if(!b.canStatus()) {
							System.out.println("\nCannot Approved! (booking " + bookingId + " has already been " 
						+ b.getStatus().toString().toLowerCase() + ")");
							
							break;
						}
						else {
							privilege = true;
						}
						
						if(!b.canTime()) {
							System.out.println("\nCannot Approved! (booking " + bookingId + " has already passed)");
							privilege = false;
							break;
						}
						else {
							privilege = true;
						}
						
						break;
					}
				}
				
				if(!privilege) {
					return;	// stop this whole method
				}
				
				if(found) {
					trueBookingId = bookingId;
					break;
				}
			}
		}
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(trueBookingId)) {
				
				b.setStatus(BookingStatus.APPROVED);
				
				saveBookings();
				
				System.out.println("\nBooking " + trueBookingId + " Approved.");

				break;
			}
		}
	}
	
	//admin only (case 7)
	public void rejectBooking() {

		//bookingId
		String trueBookingId;
		
		while(true) {
			
			boolean privilege = false;
			boolean found = false;
			
			System.out.print("\nEnter bookingId(Ex:B001)/(Enter 0 to exit): ");
			String bookingId = input.nextLine();
			
			if (bookingId.equals("0")) {
				System.out.println("\nExit Rejection.");
				return;
			}
			
			if(checkBookingId(bookingId)) {
			
				for(Booking b : bookings) {
					
					if(b.getBookingId().equals(bookingId)) {
						
						found = true;
						
						if(!b.canStatus()) {
							System.out.println("\nCannot Rejected! (booking " + bookingId + " has already been " 
						+ b.getStatus().toString().toLowerCase() + ")");
							
							break;
						}
						else {
							privilege = true;
						}
						
						if(!b.canTime()) {
							System.out.println("\nCannot Rejected! (booking " + bookingId + " has already passed)");
							privilege = false;
							break;
						}
						else {
							privilege = true;
						}
						
						break;
					}
				}
				
				if(!privilege) {
					return;	// stop this whole method
				}
				
				if(found) {
					trueBookingId = bookingId;
					break;
				}
			}
		}
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(trueBookingId)) {
				
				b.setStatus(BookingStatus.REJECTED);
				
				saveBookings();
				
				System.out.println("\nBooking " + trueBookingId + " Rejected.");

				break;
			}
		}
	}
	
	
	public boolean checkFacilityId(String facilityId) {
		
		boolean found = false;
		
		//check the input facilityId in the storage or not
		for(Facility f : facilities) {
			
			if(f.getFacilityId().equals(facilityId)) {
				
				found = true;
				break;
			}
		}
		
		if(!found) {	//the input facilityId not in the storage = false
			System.out.println("\nFalse facilityId!");
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public boolean checkBookingId(String bookingId) {
		
		boolean found = false;
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(bookingId)) {
				
				found = true;
				break;
			}
		}
		
		if(!found) {
			System.out.println("\nFalse bookingId!");
			System.out.println("Please Re-enter: ");
			return false;
		}
		else {
			return true;
		}
		
	}
	
	
	public boolean checkConflict(String bookingId, String facilityId, TimeSlot timeSlot) {
	
		for(Booking b : bookings) {
			
			//just check PENDING & APPROVED
			if(b.getStatus().equals(BookingStatus.REJECTED) || b.getStatus().equals(BookingStatus.CANCELLED)) {
				
				continue;	//= skip this b, loops next again
			}
			
			//skip when modify, 
			//because new time maybe will conflict the old(actually will be replace)
			if(bookingId != null && b.getBookingId().equals(bookingId)) {
				continue;	//= skip
			}
			
			//skip when different facility
			if(!b.getFacilityId().equals(facilityId)) {
				continue;
			}
			
			//check when same facility
			if(b.getFacilityId().equals(facilityId)) {
				
				if(b.getTimeSlot().overlaps(timeSlot)) {
					System.out.println("\nTime Conflict!");
					System.out.println("Please Re-enter: ");
					return false;
				}
			}
		}
		
		System.out.println("\nNo Time Conflict Detected");
		return true;
		
	}

	
	public String getFacilityIdByBookingId(String bookingId) {
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(bookingId)) {
				
				return b.getFacilityId();
				
				//when return occur, (loop + this method) will stop
			}
		}
		
		//if not found
		return null;
	}
	
	
	public int getUserBookingStatistics(String userId) {
		
		int count = 0;
		
		for(Booking b : bookings) {
			if(b.getUserId().equals(userId)) {
				count++;
			}
		}
		return count;
	}
	
	public ArrayList<Booking> getUpcomingBookings(User user){
		
		ArrayList<Booking> result = new ArrayList<>();
		
		LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
		
		for(Booking b : bookings) {
			
			// user filter
			if (!b.getUserId().equals(user.getUserId()))
				continue;
			
			// if cancelled
			if (b.getStatus() == BookingStatus.CANCELLED)
				continue;
			
			// if rejected
			if (b.getStatus() == BookingStatus.REJECTED) {
				result.add(b);
				continue;
			}
			
			// if approved
			if (b.getStatus() == BookingStatus.APPROVED) {
			
				// time validation
				LocalDateTime startTime = LocalDateTime.of(
						b.getTimeSlot().getDate(), 
						b.getTimeSlot().getStartTime());
				
				//current booking within 12 hour
				if(startTime.isAfter(now) && 
						startTime.isBefore(now.plusHours(12))) {
					result.add(b);
				}
			}
		}
		
		//Ascending order
		result.sort( (b1, b2) -> {
			LocalDateTime t1 = LocalDateTime.of(
					b1.getTimeSlot().getDate(), 
					b1.getTimeSlot().getStartTime());
			
			LocalDateTime t2 = LocalDateTime.of(
					b2.getTimeSlot().getDate(), 
					b2.getTimeSlot().getStartTime());
			
			//is t1 earlier than t2?
			return t1.compareTo(t2);
		} );
		
		return result;
	}
	
	public List<Booking> getUserBookings(User user) {
	    List<Booking> result = new ArrayList<>();

	    for (Booking b : bookings) {
	        if (b.getUserId().equals(user.getUserId())) {
	            result.add(b);
	        }
	    }
	    return result;
	}
	
	public List<String> getUserFacilityIds(User user) {

		List<String> facilityIds = new ArrayList<>();
		List<Integer> counts = new ArrayList<>();

		for (Booking b : bookings) {

			if (!b.getUserId().equals(user.getUserId()))
				continue;

			if (b.getStatus() == BookingStatus.CANCELLED ||
					b.getStatus() == BookingStatus.REJECTED)
				continue;

			String facilityId = b.getFacilityId();
			
			int index = facilityIds.indexOf(facilityId);

			if (index == -1) {
				facilityIds.add(facilityId);
				counts.add(1);
			} else {
				counts.set(index, counts.get(index) + 1);
			}
		}
		this.tempCounts = counts;
		return facilityIds;
	}
	
	public List<Integer> getCounts() {
		return tempCounts;
	}
	
	public Facility getFacilityById(String facilityId) {
		for (Facility f : facilities) {
			if (f.getFacilityId().equals(facilityId)) {
				return f;
			}
		}
		return null;
	}

	public List<String> getUserFacilityTypes(User user) {
		
		List<String> types = new ArrayList<>();
	    List<Integer> counts = new ArrayList<>();

		for (Booking b : bookings) {
			
			if (!b.getUserId().equals(user.getUserId()))
				continue;
			
			if (b.getStatus() == BookingStatus.CANCELLED || b.getStatus() == BookingStatus.REJECTED)
				continue;

			Facility f = getFacilityById(b.getFacilityId());
			if (f == null) 
				continue;
			
			String type = f.getFacilityType();
			
			int index = types.indexOf(type);
			
			if (index == -1) {
				types.add(type);
				counts.add(1);
			} else {
				counts.set(index, counts.get(index) + 1);
			}
		}
		
		this.tempTypeCounts = counts;
		return types;
	}
	
	public List<Integer> getTypeCounts() {
		return tempTypeCounts;
	}
	
	public List<String> getAllFacilityIds() {

		List<String> facilityIds = new ArrayList<>();
		List<Integer> counts = new ArrayList<>();
		List<Double> totalHours = new ArrayList<>();
		
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

		for (Booking b : bookings) {

			if (b.getStatus() == BookingStatus.CANCELLED || b.getStatus() == BookingStatus.REJECTED)
				continue;
	
			String id = b.getFacilityId();
			
			LocalDateTime start = LocalDateTime.of(b.getTimeSlot().getDate(), b.getTimeSlot().getStartTime());
			LocalDateTime end = LocalDateTime.of(b.getTimeSlot().getDate(), b.getTimeSlot().getEndTime());
	
			if (end.isBefore(monthStart) || start.isAfter(monthEnd)) {
				continue;
			}

			LocalDateTime actualStart;
			if (start.isBefore(monthStart)) {
				actualStart = monthStart;
			} else {
				actualStart = start;
			}
			LocalDateTime actualEnd;
			if (end.isAfter(monthEnd)) {
				actualEnd = monthEnd;
			} else {
				actualEnd = end;
			}
			double hours = Duration.between(actualStart, actualEnd).toMinutes() / 60.0;
			
			int index = facilityIds.indexOf(id);
			
			// calculate hours
			if (index == -1) {
				facilityIds.add(id);
				counts.add(1);
				totalHours.add(hours);
			} else {
				counts.set(index, counts.get(index) + 1);
				totalHours.set(index, totalHours.get(index) + hours);
			}
		}

		this.tempCounts = counts;
		this.tempHours = totalHours;
		return facilityIds;
	}
	
	public List<Double> getHours() {
		return tempHours;
	}
	
	public List<String> getAllFacilityTypes() {

		List<String> types = new ArrayList<>();
		List<Integer> counts = new ArrayList<>();

		for (Booking b : bookings) {

			if (b.getStatus() == BookingStatus.CANCELLED || b.getStatus() == BookingStatus.REJECTED)
				continue;
	
			Facility f = getFacilityById(b.getFacilityId());
			if (f == null) 
				continue;
	
			String type = f.getFacilityType();
			int index = types.indexOf(type);

			if (index == -1) {
				types.add(type);
				counts.add(1);
			} else {
				counts.set(index, counts.get(index) + 1);
			}
		}

		this.tempTypeCounts = counts;
		return types;
	}
	
	public List<String> getPeakHoursPerFacility(List<String> facilityIds) {

		List<String> peakHours = new ArrayList<>();

		for (String facilityId : facilityIds) {

			List<String> hours = new ArrayList<>();
			List<Integer> counts = new ArrayList<>();

			for (Booking b : bookings) {

				if (b.getStatus() == BookingStatus.CANCELLED || b.getStatus() == BookingStatus.REJECTED)
					continue;

				if (!b.getFacilityId().equals(facilityId))
					continue;

				LocalTime start = b.getTimeSlot().getStartTime();
				LocalTime end = b.getTimeSlot().getEndTime();

				// loop hour by hour
				while (start.isBefore(end)) {
	
					String hour = String.format("%02d", start.getHour());
	
					int index = hours.indexOf(hour);
	
					if (index == -1) {
						hours.add(hour);
						counts.add(1);
					} else {
						counts.set(index, counts.get(index) + 1);
					}
		
					start = start.plusHours(1);
				}
			}
	
			String peakHour = "-";
			int max = 0;
	
			for (int i = 0; i < hours.size(); i++) {
				if (counts.get(i) > max) {
					max = counts.get(i);
					peakHour = hours.get(i);
				}
			}
			peakHours.add(peakHour);
		}
		return peakHours;
	}
	
	public void loadFacilities() {
		facilities.clear();
		facilities = facilityStorage.load();
	}

	public void loadBookings() {
		bookings.clear();
		bookings = bookingStorage.load();
	}

	public void saveBookings() {
		bookingStorage.save(bookings);
	}
	
	
}

