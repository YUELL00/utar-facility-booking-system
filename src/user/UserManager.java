package user;

import java.util.ArrayList;

import util.Validator;
import storage.UserStorage;

public class UserManager {

	private ArrayList<User> users;
	private UserStorage userStorage;
	
	public UserManager(){
		userStorage = new UserStorage("users.txt");
		users = new ArrayList<>();
		loadUsers();
	}
	
	public void registerUser(User user){
		/*  Function:
		 *  1. Check whether the user ID already exists
		 *  2. Create a new user record in memory
		 *  3. Update the data to persistent storage
		 *  */
		if(user == null) {
			System.out.println("Invalid user data.");
			return;
		}
		
		// validate userId
		if(!Validator.validateUserId(user.getUserId())) {
			System.out.println("Invalid User ID format.");
			return;
		}
		
		// validate password
		if(!Validator.validatePassword(user.getPassword())) {
			System.out.println("Password must be at least 6 characters.");
			return;
		}
		
		// validate contact
		if(!Validator.validateContactNumber(user.getContactNumber())) {
			System.out.println("Invalid contact number.");
			return;
		}
		
		// duplicate check
		if(getUserById(user.getUserId()) != null){
			System.out.println("User ID already exists.");
			return;
		}
		
		users.add(user);
		saveUsers();
		System.out.println("User registered successfully.");
	}
	
	public User loginUser(String userId,String password){
		// Check the authentication
		for(User u : users){
			if(u.getUserId().equals(userId) && u.getPassword().equals(password)){
				return u;
			}
		}
		return null;
	}

	public User getUserById(String userId){
		// Retrieves a user by their unique user ID through the in-memory user array
		// Returns the first matching User object, stops once a match is found
		for(User u : users){
			if(u.getUserId().equals(userId)){
				return u;
			}
		}
		return null;
	}
	
	public void updateUser(User user, String newContact){
		// Updates the contact number of a given user
		if(user == null) {
			System.out.println("User not found.");
			return;
		}
		
		if(!Validator.validateContactNumber(newContact)) {
			System.out.println("Invalid contact number.");
			return;
		}
		
		user.setContactNumber(newContact);
		saveUsers();
		System.out.println("Profile updated.");
	}
	
	public void loadUsers(){
		// load all users to memory
		ArrayList<User> loaded = userStorage.load();
		for(User u : loaded){
			if(u != null){
				users.add(u);
			}
		}
	}

	public void saveUsers(){
		userStorage.save(users);
	}
	
}