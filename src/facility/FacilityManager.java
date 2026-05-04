package facility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import booking.BookingManager;
import booking.TimeSlot;
import maintenance.MaintenanceManager;
import storage.FacilityStorage;

public class FacilityManager {
	private List<Facility> facilities;
	private FacilityStorage facilityStorage;
	
	public FacilityManager() {
		facilities = new ArrayList<>();
		facilityStorage = new FacilityStorage("facilities.txt");
		}
	
	// load facilities
	public void loadFacilities() {
		List<Facility> list = facilityStorage.load();
		
		facilities.clear();
		
		if (list != null) {
			facilities.addAll(list);
			}
		}
	
	public void saveFacilities() {
	    facilityStorage.save(new ArrayList<>(facilities));
	}
	
	public List<Facility> findFacilities(String typeInput) {
		List<Facility> result = new ArrayList<>();
		
		for (Facility f : facilities) {
			if (typeInput.equals("1") || f.getFacilityType().equalsIgnoreCase(typeInput)) {
				result.add(f);
			}
		}
		return result;
	}
	
	public enum AvailabilityStatus {
		AVAILABLE,
		BOOKED,
		UNDER_MAINTENANCE
	}
	
	public AvailabilityStatus checkAvailability(Facility f, TimeSlot ts, BookingManager bookingManager, MaintenanceManager maintenanceManager) {

		if (maintenanceManager.isUnderMaintenance(f.getFacilityId(), ts)) {
			return AvailabilityStatus.UNDER_MAINTENANCE;
		}
		
		if (bookingManager.isBooked(f.getFacilityId(), ts)) {
			return AvailabilityStatus.BOOKED;
		}
		
		return AvailabilityStatus.AVAILABLE;
	}
	
	public boolean isAvailable(Facility f, TimeSlot ts, BookingManager bookingManager, MaintenanceManager maintenanceManager) {

		// booking conflict
		if (bookingManager.isBooked(f.getFacilityId(), ts)) {
			return false;
		}

		// maintenance conflict
		if (maintenanceManager.isUnderMaintenance(f.getFacilityId(), ts)) {
			return false;
		}

		return true;
	}
	
	// search facility
	public List<Facility> searchFacilities(String date, LocalTime start, LocalTime end, String type, 
											BookingManager bookingManager, MaintenanceManager maintenanceManager) {
	
		TimeSlot ts = new TimeSlot(LocalDate.parse(date), start, end);
		
		List<Facility> result = new ArrayList<>();

		for (Facility f : facilities) {
			
			// type filter
			if (type != null && !type.isEmpty() && !type.equalsIgnoreCase(f.getFacilityType())) {
				continue;
			}

			// check avaibility
			if (!isAvailable(f, ts, bookingManager, maintenanceManager)) {
	            continue;
	        }
	        result.add(f);
	    }
	    return result;
	}

	// get all available
	public List<Facility> getAvailableFacilities(TimeSlot ts, BookingManager bookingManager, MaintenanceManager maintenanceManager) {
		List<Facility> result = new ArrayList<>();
		
		for (Facility f : facilities) {
			if (isAvailable(f, ts, bookingManager, maintenanceManager)) {
				result.add(f);
			}
		}

		return result;
	}

	// generate report
	public String generateUtilizationReport(TimeSlot ts, BookingManager bookingManager, MaintenanceManager maintenanceManager) {
		
		int total = facilities.size();
		int available = 0;
		
		for (Facility f : facilities) {
			if (isAvailable(f, ts, bookingManager, maintenanceManager)) {
				available++;
			}
		}
		
		return String.format(
				"Total Facilities: %d\nAvailable: %d\nUnavailable: %d",
				total, available, total - available);
	}
	
}