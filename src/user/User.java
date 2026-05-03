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
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public abstract String getRole();
	
	// show the student information
	public String getProfileInfo(){ 
		return "\nUser ID: " + userId + 
				"\nName: " + name + 
				"\nRole: " + getRole() +
				"\nContact: " + contactNumber + 
				"\nFaculty/Department: " + facultyOrDepartment;
	}
	
	public void viewProfile() {
		System.out.println(getProfileInfo());
	}
	
}