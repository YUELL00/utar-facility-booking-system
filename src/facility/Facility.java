package facility;

public class Facility {
	
	package facility;

	import java.util.Objects;

	public class Facility {

	    private final String facilityId;
	    private String name;
	    private String type;
	    private String status; // Available or Unavailable

	    // constructor
	    public Facility(String facilityId, String name, String type, String status) {
	        this.facilityId = Objects.requireNonNull(facilityId, "Facility ID cannot be empty");
	        this.name = Objects.requireNonNull(name, "Name cannot be empty");
	        this.type = Objects.requireNonNull(type, "Type cannot be empty");
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
	    public String getFacilityId() { return facilityId; }
	    public String getName() { return name; }
	    public String getType() { return type; }
	    public String getStatus() { return status; }

	    @Override
	    public String toString() {
	        return String.format("%s - %s (%s) [%s]", facilityId, name, type, status);
	    }
	}
	
}
