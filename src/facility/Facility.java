
package facility;

import java.util.Objects;

public class Facility {

	private final String facilityId;
	private String facilityName;
	private String facilityType;
	private String location;

	// constructor
	public Facility(String facilityId, String facilityName, String facilityType, String location) {
		this.facilityId = Objects.requireNonNull(facilityId, "Facility ID cannot be empty");
		this.facilityName = Objects.requireNonNull(facilityName, "Name cannot be empty");
		this.facilityType = Objects.requireNonNull(facilityType, "Type cannot be empty");
	}

	// getters
	public String getFacilityId() { 
		return facilityId; 
	}
	
	public String getFacilityName() {
		return facilityName;
	}
	
	public String getFacilityType() {
		return facilityType;
	}
	
	public String getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return String.format("%s - %s (%s) [%s]", facilityId, facilityName, facilityType, location);
	}
}
