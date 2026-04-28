
package facility;

import java.util.Objects;

public class Facility {

	private final String facilityId;
	private String facilityName;
	private String facilityType;
	private String location;
	private String status; // Available or Unavailable

	// constructor
	public Facility(String facilityId, String facilityName, String facilityType, String location, String status) {
		this.facilityId = Objects.requireNonNull(facilityId, "Facility ID cannot be empty");
		this.facilityName = Objects.requireNonNull(facilityName, "Name cannot be empty");
		this.facilityType = Objects.requireNonNull(facilityType, "Type cannot be empty");
		setStatus(status); // use same method to check
		}
	
	// check availability
	public boolean checkAvailability() {
		return "Available".equalsIgnoreCase(status);
		}
	
	// Updates the facility status with validation
	public void updateStatus(String status) {
		setStatus(status);
	}

	// Internal method to validate and assign status
	private void setStatus(String status) {
		if (status == null) {
			throw new IllegalArgumentException("Status cannot be empty");
		}
		if (!status.equalsIgnoreCase("Available") &&
				!status.equalsIgnoreCase("Unavailable")) {
			throw new IllegalArgumentException("Invalid status");
		}
		this.status = status;
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
	
	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return String.format("%s - %s (%s) [%s]", facilityId, facilityName, facilityType, location, status);
	}
}
