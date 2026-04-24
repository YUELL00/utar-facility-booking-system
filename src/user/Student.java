package user;

public class Student extends User{

	private String programme;
	
	public Student(String userId, String password, String name, 
				   String faculty, String contactNumber, String programme){
		super(userId, password, name, faculty, contactNumber);
		this.programme = programme;
	}
	
	public String getProgramme(){
		return programme;
	}

	@Override
	public String getProfileInfo() {
		return super.getProfileInfo() + "\nProgramme: " + programme;
	}
}
