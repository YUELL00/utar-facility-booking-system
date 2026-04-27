package user;

public class Student extends User{

	public Student(String userId, String password, String name,
			String facultyOrDepartment, String contactNumber, String programme){
		super(userId, password, name, facultyOrDepartment, contactNumber);
	}
	
	@Override
	public String getRole() {
		return this.getClass().getSimpleName();
	}
	
}