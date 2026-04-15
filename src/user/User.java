package user;

public abstract class User {

	protected String userId;
	protected String password;
	protected String name;
	protected String facultyOrDepartment;
	protected String contactNumber;
	
	public User(String userId,String password,String name
			,String facultyOrDepartment,String contactNumber){
		this.userId=userId;
		this.password=password;
		this.name=name;
		this.facultyOrDepartment=facultyOrDepartment;
		this.contactNumber=contactNumber;
	}
	
	public String getUserId(){
		return userId;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setContactNumber(String contactNumber){
		this.contactNumber=contactNumber;
	}
	
	public void viewProfile(){
		System.out.println("User ID: "+userId);
		System.out.println("Name: "+name);
		System.out.println("Faculty/Department: "+facultyOrDepartment);
		System.out.println("Contact: "+contactNumber);
		System.out.println("Role: "+this.getClass().getSimpleName());
	}
	
}