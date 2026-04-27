package user;

public class Admin extends User{

	public Admin(String userId, String password, String name, 
			String department, String contactNumber){
		super(userId, password, name, department, contactNumber);
	}
	
	@Override
	public String getRole() {
		return this.getClass().getSimpleName();
	}
	
}