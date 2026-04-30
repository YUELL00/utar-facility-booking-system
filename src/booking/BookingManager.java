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
	
	//constructor
	public BookingManager() {
		
		this.bookings = new ArrayList<>();
		this.facilities = new ArrayList<>();
		
		this.bookingStorage = new BookingStorage("booking.txt");
		this.facilityStorage = new FacilityStorage("facilities.txt");
	}
	
	//current userId
	private User currentUser;
	
	public void setCurrentUser(User user) {
		
		this.currentUser = user;
	}
	
	
	public void createBooking() {
		
		String currentUserId = currentUser.getUserId();
		
		//bookingId, output: BXXX , 3int , not enough = fill with 0
		String bookingId = String.format("B%03d", bookings.size() + 1);
		
		//facilityId
		String trueFacilityId = null;
		
		while(true) {
			System.out.print("\nEnter facilityId(Ex:F001): ");
			String facilityId = input.nextLine();
			
			if(checkFacilityId(facilityId)) {
				
				trueFacilityId = facilityId;
				break;
			}
			System.out.println("Please Re-enter: ");
		}
		
		
		//timeSlot
		TimeSlot trueTimeSlot = null;
		
		while(true) {
			try {
					
				System.out.print("\nEnter date(Ex:2026-05-01): ");
				String inputDate = input.nextLine();
				LocalDate date = LocalDate.parse(inputDate);
				
				System.out.print("\nEnter start time(Ex:10:00): ");
				String inputStartTime = input.nextLine();
				LocalTime startTime = LocalTime.parse(inputStartTime);
				
				System.out.print("\nEnter end time(Ex:12:00): ");
				String inputEndTime = input.nextLine();
				LocalTime endTime = LocalTime.parse(inputEndTime);
				
				TimeSlot timeSlot = new TimeSlot(date, startTime, endTime);
				
				if( checkConflict(null, trueFacilityId, timeSlot) ) {
					
					//save true timeSlot
					trueTimeSlot = timeSlot;
					
					// no conflict --> exit loop
					break;
				}
			}
			catch(Exception e) {
				System.out.println("\nInvalid Time!");
			}
			System.out.println("Please Re-enter: ");
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
			
		System.out.println("\nBooking Created.");
		
	}
	
	
	public void modifyBooking() {
		
		String currentUserId = currentUser.getUserId();
	
		//bookingId
		String trueBookingId = null;
		
		while(true) {
			boolean found = false;
			
			System.out.print("\nEnter bookingId(Ex:B001): ");
			String bookingId = input.nextLine();
			
			if(checkBookingId(bookingId)) {
				
				for(Booking b : bookings) {
				
					if(b.getBookingId().equals(bookingId)) {
			
						if(!b.getUserId().equals(currentUserId)) {
							System.out.println("\nNo Privileges to Modify!");
							found = false;
							break;
						}
						else {
							found = true;
						}
	
						if(!b.canModify()) {
							System.out.println("\nCannot Modify!");
							found = false;
							break;
						}
						else {
							found = true;
						}
					}
				}
				
				if(found) {
					trueBookingId = bookingId;
					break;
				}		
				else {
					System.out.println("\nFalse bookingId!");
				}
			}
			System.out.println("Please Re-enter: ");
		
		}
		
		//timeSlot
		TimeSlot trueTimeSlot = null;
		
		while(true) {
			try {
				
				System.out.print("\nEnter new date(Ex:2026-05-02): ");
				String inputDate = input.nextLine();
				LocalDate date = LocalDate.parse(inputDate);
						
				System.out.print("\nEnter new start time(Ex:13:00): ");
				String inputStartTime = input.nextLine();
				LocalTime startTime = LocalTime.parse(inputStartTime);
						
				System.out.print("\nEnter new end time(Ex:15:00): ");
				String inputEndTime = input.nextLine();
				LocalTime endTime = LocalTime.parse(inputEndTime);
				
				TimeSlot timeSlot = new TimeSlot(date, startTime, endTime);
					
				//check new input
				if(checkConflict(trueBookingId, getFacilityIdByBookingId(trueBookingId), timeSlot)) {
				
					trueTimeSlot = timeSlot;
					break;
				}
				else {
					System.out.println("\nModified Fail.");
				}
			}
			catch(Exception e) {
				System.out.println("\nInvalid Time!");
			}
			System.out.println("Please Re-enter: ");
		}	
		
		//purpose
		System.out.print("\nEnter new purpose: ");
		String purpose = input.nextLine();
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(trueBookingId)) {
				
				LocalDateTime lastModifiedTime = LocalDateTime.now().withSecond(0).withNano(0);
							
				b.modifyBooking(trueTimeSlot, purpose, lastModifiedTime);
							
				saveBookings();
				
				System.out.println("\nBooking Modified.");

				break;
			}
		}
	}
		
	
	public void cancelBooking() {
		
		String currentUserId = currentUser.getUserId();
		
		//bookingId
		System.out.print("\nEnter bookingId(Ex:B001): ");
		String bookingId = input.nextLine();
		
		boolean found = false;
		
		for(int i = 0; i < bookings.size(); i++) {
			
			Booking b = bookings.get(i);
			
			if(b.getBookingId().equals(bookingId)) {
				
				found = true;
				
				if(!b.getUserId().equals(currentUserId)) {
					System.out.println("\nNo Privileges to Cancel!");
					break;
				}
					
				if(!b.canCancel()) {
					System.out.println("\nCannot Cancel! (booking already approved)");
					break;
				}
				
				bookings.remove(i);
						
				saveBookings();
						
				System.out.println("\nBooking Cancelled.");
				
				//stop when found bookingId
				break;
			}
		}
		
		if(!found) {
			System.out.println("\nFalse bookingId!");
		}
		
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
	
	public String getStatusByBookingId() {
		
		//bookingId
		System.out.print("\nEnter bookingId(Ex:B001): ");
		String bookingId = input.nextLine();
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(bookingId)) {
				
				return "Status: " + b.getStatus().toString();
				
				//when return occur, (loop + this method) will stop
			}
		}
		//so if loop have return, this method will not run to here
		return "\nFalse bookingId!";
	}
	
	public ArrayList<Booking> getBookingsByUserId(){
	
		//userId
		System.out.print("\nEnter userId(Ex:U001): ");
		String userId = input.nextLine();
		
		ArrayList<Booking> result = new ArrayList<>();
		
		for(Booking b : bookings) {
			
			if(b.getUserId().equals(userId)) {
				
				result.add(b);
			}
		}
		
		if(result.isEmpty()) {
			System.out.println("\nFalse userId!");
		}
		
		return result;
	}
	
	
	public void showAllBookings() {
		
		for(Booking b : bookings) {
			System.out.println(b);
		}
	}
	
	//admin only
	public void approveBooking() {
		
		System.out.print("\nEnter bookingId(Ex:B001): ");
		String bookingId = input.nextLine();
		
		boolean found = false;
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(bookingId)) {
				
				found = true;
				
				b.setStatus(BookingStatus.APPROVED);
				saveBookings();
				System.out.println("\nBooking Approved.");
				
				break;
			}	
		}
		if(!found) {
			System.out.println("\nFalse bookingId!");
		}
	}
	
	//admin only
	public void rejectBooking() {

		System.out.print("Enter bookingId(Ex:B001): ");
		String bookingId = input.nextLine();
		
		boolean found = false;
		
		for(Booking b : bookings) {
			if(b.getBookingId().equals(bookingId)) {
				
				found = true;
				
				b.setStatus(BookingStatus.REJECTED);
				saveBookings();
				System.out.println("Booking Rejected.");
				
				break;
			}
		}
		if(!found) {
			System.out.println("\nFalse bookingId!");
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
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public boolean checkConflict(String bookingId, String facilityId, TimeSlot timeSlot) {
	
		for(Booking b : bookings) {
			
			//just check PENDING & APPROVED
			if(b.getStatus().equals(BookingStatus.REJECTED)) {
				
				continue;	//= skip
			}
			
			//skip when modify, 
			//because new time maybe will conflict the old(actually will be replace)
			if(bookingId != null && !bookingId.isEmpty() && 
					b.getBookingId().equals(bookingId)) {
				continue;	//= skip
			}
			
			//different facility
			if(!b.getFacilityId().equals(facilityId)) {
				continue;
			}
			
			if(b.getFacilityId().equals(facilityId)) {
				
				if(!b.getTimeSlot().overlaps(timeSlot)) {
					System.out.println("\nOK, No Time Conflict.");
					return true;
				}
			}
		}
		
		System.out.println("\nTime Conflict!");
		return false;
		
	}

	
	public ArrayList<Booking> getUpcomingBookings(){
		
		ArrayList<Booking> result = new ArrayList<>();
		
		LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
		
		for(Booking b : bookings) {
			
			LocalDateTime startTime = LocalDateTime.of(
					b.getTimeSlot().getDate(), 
					b.getTimeSlot().getStartTime());
			
			//current booking within 12 hour
			if(startTime.isAfter(now) && 
					startTime.isBefore(now.plusHours(12))) {
				result.add(b);
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
	

	//Map = a refer table (through Key get Value)
	public Map<String, Integer> getPeakBookingHours() {
	
		//HashMap = a statistic table
		Map<String, Integer> hourCount = new HashMap<>();
		
		for(Booking b : bookings) {
			
			LocalTime startTime = b.getTimeSlot().getStartTime();
			LocalTime endTime = b.getTimeSlot().getEndTime();
			
			//loop from start until end（not include end）
			//Ex: "10:00 - 12:00", hour = "10", "11"
			while(startTime.isBefore(endTime)) {
				
				//cut hour only("10:30" → "10")
				String hour = String.format("%02d", startTime.getHour());
				
				//hourCount.getOrDefault(hour, 0) + 1) 
				//= if Map have the hour --> (the hour's count) then + 1
				//= if Map haven't the hour --> (let the hour's count = 0) then + 1
				hourCount.put(hour, hourCount.getOrDefault(hour, 0) + 1);
				
				startTime = startTime.plusHours(1);
			}
		}
		
		return hourCount;
		//Ex: 10 = 5
		//hour = hourCount
	}
	
	
	public void generatePeakBookingReport() {
		
		//call getPeakBookingHours() method to get result
		Map<String, Integer> hourCount = getPeakBookingHours();
		
		//TreeMap = Ascending
		Map<String, Integer> report = new TreeMap<>(hourCount);
		
		System.out.println("\n====== Peak Booking Report ======");
		
		//entry = (Key + Value) , Ex: "10" --> 5
		for(Map.Entry<String, Integer> entry : report.entrySet()) {
			
			System.out.println(entry.getKey() + ":00 --> " + 
			entry.getValue() + " bookings");
			
			//Ex: 10:00 --> 5 bookings
		}
		System.out.println("\n=================================");
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

