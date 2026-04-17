package booking;

import java.util.*;

//get current/now date + time
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingManager {
	
	Scanner input = new Scanner(System.in);

	private List<Booking> bookings;
	
	private BookingStorage bookingStorage;
	
	//constructor
	public BookingManager(BookingStorage bookingStorage) {
		
		this.bookings = new ArrayList<>();
		
		this.bookingStorage = bookingStorage;
	}
	
	public void createBooking(Booking booking) {
		// Booking(String bookingId, String userId, String facilityId, TimeSlot timeSlot, String purpose)
		
		BookingStorage b = new BookingStorage("D:\\BookingStorage.txt");
		int[] countB = new int[1];
		Booking[] listB = b.load(countB);
		int length = countB[0];
		
		//output: BXXX , 3int , not enough = fill with 0
		String bookingId = String.format("B%03d", length);
		
		String userId = getUserId();
		
		System.out.println("Enter facilityId(Ex:F001): ");
		String facilityId = input.nextLine();
		
		System.out.println("Enter purpose(academic/co-curricular/official): ");
		String purpose = input.nextLine();
		
		if(check) {
			System.out.println("Booking Created");
		}
		else{
			System.out.println("Invalid Booking");
		}
	}
	
	public void modifyBooking(String bookingId, TimeSlot newSlot) {
		
	}
	
	public void cancelBooking(String bookingId) {
		
	}
	
	public String getStatusByBookingId(String bookingId) {
		
		for(Booking b : bookings) {
			if(b.getBookingId().equals(bookingId)) {
				return b.getStatus();
			}
			else {
				return "NOT FOUND";
			}
		}
	}
	
	public List<Booking> getBookingsByUserId(String userId){
	
		List<Booking> result = new ArrayList<>();
		
		for(Booking b : bookings) {
			if(b.getUserId().equals(userId)) {
				result.add(b);
			}
		return result;
		}
	}
	
	public List<Booking> getUpcomingBookings(){
		
		List<Booking> result = new ArrayList<>();
		
		LocalDateTime now = LocalDateTime.now();
		
		for(Booking b : bookings) {
			//LocalDate.parse("2026-04-14");
			//LocalTime.parse("10:30");
			//LocalDateTime.parse("2026-04-14T10:30");
			LocalDateTime start = LocalDateTime.parse(b.getTimeSlot().getDate() + "T" + b.getTimeSlot().getStartTime());
			
			//current booking within 1 hour
			if(start.isAfter(now) && start.isBefore(now.plusHours(1))) {
				result.add(b);
			}
		}
		return result;
	}
	
	//admin only
	public void approveBooking(String bookingId) {
		
		for(Booking b : bookings) {
			if(b.getBookingId().equals(bookingId)) {
				b.setStatus("APPROVED");
				saveBookings();
				System.out.println("Booking Approved.");
			}
			else {
				System.out.println("Booking　Not Approved.");
			}
		}
	}
	
	//admin only
	public void rejectBooking(String bookingId) {
			
		for(Booking b : bookings) {
			if(b.getBookingId().equals(bookingId)) {
				b.setStatus("REJECTED");
				saveBookings();
				System.out.println("Booking Rejected.");
			}
			else {
				System.out.println("Booking　Not Rejected.");
			}
		}
	}
	
	
	public boolean checkConflict(String facilityId, TimeSlot slot) {
		
		//create object to call load()
		FacilityStorage f = new FacilityStorage("D:\\FacilityStorage.txt");
		
		int[] countF = new int[1];
		
		Facility[] listF = f.load(countF);
		
		boolean found = false;
		
		//check the input facilityId in the storage or not
		for(int i = 0; i < countF[0]; i++) {
			//list[i] = Facility object = can call getFacilityId()
			if(listF[i].getFacilityId().equals(facilityId)) {
				found = true;
				break;
			}//else skip
		}
		
		if(!found) {	//the input facilityId not in the storage = false
			System.out.println("Invalid facilityId!");
			return false;
		}
		else if(found) {
			
			BookingStorage b = new BookingStorage("D:\\BookingStorage.txt");
			
			int[] countB = new int[1];
			
			Booking[] listB = b.load(countB);
			
			
			for(int i =0; i < countB[0]; i++) {
				
				if(listB[i].getFacilityId().equals(facilityId)) {
					
					//same date
					if( (listB[i].getTimeSlot().getDate()).equals(slot.getDate()) ) {
						
						//new start time is before existing end time && new end time is after existing start time
						if( slot.getStartTime().isBefore(listB[i].getTimeSlot().getEndTime()) && slot.getEndTime().isAfter(listB[i].getTimeSlot().getStartTime()) ) {
							
							System.out.println("Time Conflict!");
							return false;
						}
					}
				}
			}
			
			System.out.println("No Conflict");
			return true;
		}
	}
	

	public List<Object> getPeakBookingHours() {
	
		List<String> hoursList = new ArrayList<>();
		List<Integer> countList = new ArrayList<>();
		
		for(Booking b : bookings) {
			
			//LocalTime.parse("10:30");
			LocalTime start = LocalTime.parse(b.getTimeSlot().getStartTime());
			LocalTime end = LocalTime.parse(b.getTimeSlot().getEndTime());
			
			// loop from start until end（not include end）
			while(start.isBefore(end)) {
				
				//cut hour only("10:30" → "10")
				String hour = start.toString().substring(0, 2);
				
				boolean found = false;
				
				for(int i = 0; i < hoursList.size(); i++) {
					
					if(hoursList.get(i).equals(hour)) {
						countList.set(i, countList.get(i) + 1);
						
						found = true;
					}
					else if(!found) {
						hoursList.add(hour);
						
						countList.add(1);
					}
					
					//start hour +1 then continue loop
					start = start.plusHours(1);
				}
			}
			
			List<Object> result = new ArrayList<>();
			
			result.add(hoursList);
			result.add(countList);
			
			return result;
		}
	}
	
	public void generatePeakBookingReport() {
		
		List<Object> data = getPeakBookingHours();
		
		List<String> hoursList = (ArrayList<String>) data.get(0);
		
		List<Integer> countList = (ArrayList<Integer>) data.get(1);
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < hoursList.size(); i++) {
			
			sb.append(hoursList.get(i)).append(":00 --> ").append(countList.get(i)).append(" bookings\n");
		}
		System.out.print(sb.toString());
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
	
	public void loadBookings() {
		//record quantity
		int[] count = new int[1];
		
		//read from storage
		Booking[] arr = /bookingStorage.load(count);
		
		//clear current list
		bookings.clear();
		
		for(int i = 0; i < count[0]; i++) {
			//add to list
			bookings.add(arr[i]);
		}
	}
	
	public void saveBookings() {
		//convert to array
		Booking[] arr = new Booking[bookings.size()];
		
		for(int i = 0; i < bookings.size(); i++) {
			arr[i] = bookings.get(i);
		}
		//save to txt
		bookingStorage.save(arr, bookings.size());
	}
	
}


