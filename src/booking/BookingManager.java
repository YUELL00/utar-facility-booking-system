package booking;

import java.util.*;

//get current/now date + time
import java.time.*;

public class BookingManager{
	
	Scanner input = new Scanner(System.in);

	private List<Booking> bookings;
	
	private List<Facility> facilities;
	
	private BookingStorage bookingStorage;
	
	//constructor
	public BookingManager(BookingStorage bookingStorage) {
		
		this.bookings = new ArrayList<>();
		this.facilities = new ArrayList<>();
		
		this.bookingStorage = bookingStorage;
		
		loadBookings();
		
		loadFacilities();
	}
	
	
	public void createBooking(Booking b) {
	
		/* 写完才发现应该放在main的 (过后删掉)
		
		//bookingId, output: BXXX , 3int , not enough = fill with 0
		String bookingId = String.format("B%03d", bookings.size());
		
		//current userId
		String userId = userManager.getCurrentUser().getUserId();
		
		//facilityId
		System.out.print("\nEnter facilityId(Ex:F001): ");
		String facilityId = input.nextLine();
		
		//timeSlot
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
		
		//purpose
		System.out.print("\nEnter purpose(academic/co-curricular/official): ");
		String purpose = input.nextLine();
		
		//status
		String status = "Pending";
		
		//createdTime
		LocalDateTime createdTime = LocalDateTime.now();
		
		//lastModifiedTime
		LocalDateTime lastModifiedTime = LocalDateTime.now();
		
		Booking booking = new Booking(bookingId, userId, facilityId, timeSlot, 
					purpose, status, createdTime, lastModifiedTime);
		*/
		//List<Booking> bookings
		if( checkConflict(null, b.getFacilityId(), b.getTimeSlot()) ) {
			
			bookings.add(b);
			
			saveBookings();
			
			System.out.println("Booking Created");
		}
		else{
			System.out.println("Created Fail");
		}
	}
	
	public void modifyBooking(String bookingId, TimeSlot timeSlot, String purpose) {
		
		/* main
		//bookingId
		System.out.print("\nEnter bookingId(Ex:B001): ");
		String bookingId = input.nextLine();
		
		//timeSlot
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
		
		//purpose
		System.out.print("\nEnter new purpose(academic/co-curricular/official): ");
		String purpose = input.nextLine();
		
		*/
		
		//current userId
		String userId = userManager.getCurrentUser().getUserId();
		
		boolean found = false;
		
		//List<Booking> bookings
		//Booking b = bookings.get(i);
		//b.modifyBooking(...) can actually modify object in bookings
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(bookingId)) {
				
				found = true;
				
				if(!b.getUserId().equals(userId)) {
					System.out.println("No Privileges to Modify");
					break;
				}

				if(!b.canModify()) {
					System.out.println("Cannot Modify");
					break;
				}
				
				//check new input
				if(!checkConflict(b.getBookingId(), b.getFacilityId(), timeSlot)) {
					System.out.println("Modified Fail");
					break;
				}
				
				LocalDateTime lastModifiedTime = LocalDateTime.now();
							
				b.modifyBooking(timeSlot, purpose, lastModifiedTime);
							
				saveBookings();
							
				System.out.println("Booking Modified");
				
				//stop for loop when found bookingId
				break;
			}
		}
		
		if(!found) {
			System.out.println("False bookingId");
		}
		
	}
	
	public void cancelBooking(String bookingId) {
		
		/* main
		//bookingId
		System.out.print("\nEnter bookingId(Ex:B001): ");
		String bookingId = input.nextLine();
		*/
		
		//current userId
		String userId = userManager.getCurrentUser().getUserId();
		
		boolean found = false;
		
		for(int i = 0; i < bookings.size(); i++) {
			
			Booking b = bookings.get(i);
			
			if(b.getBookingId().equals(bookingId)) {
				
				found = true;
				
				if(!b.getUserId().equals(userId)) {
					System.out.println("No Privileges to Cancel");
					break;
				}
					
				if(!b.canCancel()) {
					System.out.println("Cannot Cancel");
					break;
				}
				
				bookings.remove(i);
						
				saveBookings();
						
				System.out.println("Booking Cancelled");
				
				//stop when found bookingId
				break;
			}
		}
		
		if(!found) {
			System.out.println("False bookingId");
		}
		
	}
	
	public String getStatusByBookingId(String bookingId) {
		
		/* main
		//bookingId
		System.out.print("\nEnter bookingId(Ex:B001): ");
		String bookingId = input.nextLine();
		*/
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(bookingId)) {
				
				return "Status: " + b.getStatus();
				
				//when return occur, (loop + this method) will stop
			}
		}
		//so if loop have return, this method will not run to here
		return "False bookingId";
	}
	
	public List<Booking> getBookingsByUserId(String userId){
	
		/* main
		//userId
		System.out.print("\nEnter userId(Ex:U001): ");
		String userId = input.nextLine();
		*/
		
		List<Booking> result = new ArrayList<>();
		
		for(Booking b : bookings) {
			
			if(b.getUserId().equals(userId)) {
				
				result.add(b);
			}
		}
		
		if(result.isEmpty()) {
			System.out.println("False userId");
		}
		
		return result;
	}
	
	//admin only
	public void approveBooking(String bookingId) {
		
		boolean found = false;
		
		for(Booking b : bookings) {
			
			if(b.getBookingId().equals(bookingId)) {
				
				found = true;
				
				b.setStatus("APPROVED");
				saveBookings();
				System.out.println("Booking Approved");
				
				break;
			}	
		}
		if(!found) {
			System.out.println("False bookingId");
		}
	}
	
	//admin only
	public void rejectBooking(String bookingId) {
		
		boolean found = false;
		
		for(Booking b : bookings) {
			if(b.getBookingId().equals(bookingId)) {
				
				found = true;
				
				b.setStatus("REJECTED");
				saveBookings();
				System.out.println("Booking Rejected");
				
				break;
			}
		}
		if(!found) {
			System.out.println("False bookingId");
		}
	}
	
	
	public boolean checkConflict(String bookingId, String facilityId, TimeSlot timeSlot) {
		
		//avoid 12:00 ~ 10:00 / 12:00 ~ 12:00
		if( ! timeSlot.getStartTime().isBefore(timeSlot.getEndTime()) ) {
			System.out.println("Invalid Time Slot");
			return false;
		}
		
		boolean found = false;
		
		//check the input facilityId in the storage or not
		for(Facility f : facilities) {
			
			if(f.getFacilityId().equals(facilityId)) {
				
				found = true;
				break;
			}//else skip
		}
		
		if(!found) {	//the input facilityId not in the storage = false
			System.out.println("False facilityId");
			return false;
		}
		
		//else if(found) {
		for(Booking b : bookings) {
			
			//skip when modify, 
			//because new time maybe will conflict the old(will be replace)
			if(bookingId != null && !bookingId.isEmpty() && 
					b.getBookingId().equals(bookingId)) {
				continue;	//= skip
			}
				
			if(b.getFacilityId().equals(facilityId)) {
					
				//same date
				if( (b.getTimeSlot().getDate()).equals(timeSlot.getDate()) ) {
						
					//new start time is before existing end time && new end time is after existing start time
					if( timeSlot.getStartTime().isBefore(b.getTimeSlot().getEndTime()) && 
							timeSlot.getEndTime().isAfter(b.getTimeSlot().getStartTime()) ) {
							
						System.out.println("Time Conflict");
						return false;
					}
				}
			}
		}
			
		System.out.println("No Conflict");
		return true;
		
	}
	
	
	public List<Booking> getUpcomingBookings(){
		
		List<Booking> result = new ArrayList<>();
		
		LocalDateTime now = LocalDateTime.now();
		
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
		
		//create object to call load()
		FacilityStorage f = new FacilityStorage("D:\\FacilityStorage.txt");
		
		int[] countF = new int[1];
		
		Facility[] listF = f.load(countF);
		
		facilities.clear();
		
		for(int i = 0; i < countF[0]; i++) {
			facilities.add(listF[i]);
		}
	}
	
	
	public void loadBookings() {
		
		int[] countB = new int[1];
		
		//array <-- file
		Booking[] listB = bookingStorage.load(countB);
		
		bookings.clear();
		
		//List<Booking> bookings
		//list <-- array
		for(int i = 0; i < countB[0]; i++) {
			bookings.add(listB[i]);
		}
	}
	
	public void saveBookings() {
		
		//new array
		Booking[] arr = new Booking[bookings.size()];
		
		//List<Booking> bookings
		//array <-- list
		for(int i = 0; i < bookings.size(); i++) {
			arr[i] = bookings.get(i);
		}
		
		//save array to txt(because file storage is array)
		bookingStorage.save(arr, bookings.size());
	}
	
}


