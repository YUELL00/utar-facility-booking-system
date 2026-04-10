package user;

public class Staff extends User{
	
	private String department;
	public Staff(String userId, String password, String name, 
			String department, String contactNumber){
		super(userId,password,name,department,contactNumber);
		this.department=department;
	}
	
	public String getDepartment(){
		return department;
	}

}