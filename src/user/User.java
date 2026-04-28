package user;

public abstract class User {

	private String userId;
	private String password;
	private String name;
	private String facultyOrDepartment;
	private String contactNumber;
	
	public User(String userId, String password, String name,
				String facultyOrDepartment, String contactNumber){
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.facultyOrDepartment = facultyOrDepartment;
		this.contactNumber = contactNumber;
	}
	
	public String getUserId(){
		return userId;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getName(){
		return name;
	}
	
	public String getFacultyOrDepartment(){
		return facultyOrDepartment;
	}
	
	public String getContactNumber(){
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber){
		this.contactNumber = contactNumber;
	}
	
	public abstract String getRole();
	
	// show the student information
	public String getProfileInfo(){ 
		return "User ID: " + userId + 
				"\nName: " + name + 
				"\nFaculty/Department: " + facultyOrDepartment + 
				"\nContact: " + contactNumber + 
				"\nRole: " + getRole();
	}
	
}