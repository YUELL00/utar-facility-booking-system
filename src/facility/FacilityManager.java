package facility;

import java.util.*;
import booking.TimeSlot;

public class FacilityManager {
	private List<Facility> facilities;
	private FacilityStorage facilityStorage;
	
	public FacilityManager() {
		facilities = new ArrayList<>();
		facilityStorage = new FacilityStorage("facilities.txt");
		loadFacilities();
		}
	
	// load facilities
	public void loadFacilities() {
		Facility[] list = facilityStorage.load();
		
		facilities.clear();
		
		if (list != null) {
			Collections.addAll(facilities, list);
			}
		}
	
	public void saveFacilities() {
		facilityStorage.save(facilities.toArray(new Facility[0]), facilities.size());
		
	}
	
	// search facility
	public List<Facility> searchFacilities(String date, String timeSlot, String type) {
		
		List<Facility> result = new ArrayList<>();
		
		for (Facility f : facilities) {
			
			// type filter
			if (type != null && !type.isEmpty() &&
					!type.equalsIgnoreCase(f.getType())) {
				continue;
				}
			
			// availability filter
			if (!f.checkAvailability()) {
				continue;
				
			}
			
			result.add(f);
			
		}
		
		// display result
		if (result.isEmpty()) {
			System.out.println("No available facilities found.");
			} else {
				result.forEach(System.out::println);
				}
		return result;
		}

	// get all available
	public List<Facility> getAvailableFacilities() {
		List<Facility> result = new ArrayList<>();
		
		for (Facility f : facilities) {
			if (f.checkAvailability()) {
				result.add(f);
				}
			}

		return result;
		}

	// update facility availability
	public void updateFacilityAvailability(String facilityId, String status) {

		for (Facility f : facilities) {

			if (f.getFacilityId() != null &&
					f.getFacilityId().equals(facilityId)) {
				
				f.updateStatus(status);
				saveFacilities();
				System.out.println("Facility updated.");
				return;
				}
			}
		
		System.out.println("Facility not found.");
		}

	// generate report
	public String generateUtilizationReport() {
		
		int total = facilities.size();
		int available = 0;
		
		for (Facility f : facilities) {
			if (f.checkAvailability()) {
				available++;
				}
			}
		
		return String.format(
				"Total Facilities: %d\nAvailable: %d\nUnavailable: %d",
				total, available, total - available);
		}
	
}