package user;

public class UserManager {

	private User[] users;
	private int size;
	private UserStorage userStorage;
	
	public UserManager(){
		userStorage = new UserStorage();
		users=new User[100];
		size=0;
		loadUsers();
	}
	
	public void registerUser(User user){
		if(getUserById(user.getUserId()) != null){
			System.out.println("User ID already exists.");
			return;
		}
		users[size]=user;
		size++;
		saveUsers();
		System.out.println("User registered successfully.");
	}
	
	public User loginUser(String userId,String password){
		for(int i=0;i<size;i++){
			if(users[i].getUserId().equals(userId) && 
					users[i].getPassword().equals(password)){
				return users[i];
			}
		}
		return null;
	}

	public User getUserById(String userId){
		for(int i=0;i<size;i++){
			if(users[i].getUserId().equals(userId)){
				return users[i];
			}
		}
		return null;
	}
	
	public void updateUser(User user,String newContact){
		user.setContactNumber(newContact);
		saveUsers();
		System.out.println("Profile updated.");
	}
	
	public void loadUsers(){
		User[] loaded=userStorage.load();
		for(int i=0;i<loaded.length;i++){
			if(loaded[i]!=null){
				users[size]=loaded[i];
				size++;
			}
		}
	}

	public void saveUsers(){
		userStorage.save(users,size);
	}
	
}
