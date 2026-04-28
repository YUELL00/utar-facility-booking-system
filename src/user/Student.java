package user;

public class Student extends User{

	private String programme;
	
	public Student(String userId, String password, String name,
			String facultyOrDepartment, String contactNumber, String programme){
		super(userId, password, name, facultyOrDepartment, contactNumber);
		this.programme = programme;
	}
	
	public String getProgramme() {
		return programme;
	}
	
	@Override
	public String getRole() {
		return this.getClass().getSimpleName();
	}
	
}