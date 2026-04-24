package user;

public class Staff extends User{
	
	public Staff(String userId, String password, String name, 
			String facultyOrDepartment, String contactNumber){
		super(userId, password, name, facultyOrDepartment, contactNumber);
	}

	@Override
	public String getRole() {
		return this.getClass().getSimpleName();
	}

}