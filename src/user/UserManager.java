package user;

import java.util.ArrayList;
import util.Validator;
import storage.UserStorage;

public class UserManager {

	private static volatile ArrayList<User> users;
	private UserStorage userStorage;
	
	public UserManager(){
		users = new ArrayList<>();
		userStorage = new UserStorage("users.txt");
		loadUsers();
	}
	
	public String normalizeRole(String role) {
		if (role == null) return null;
		role = role.trim().toLowerCase();
		switch (role) {
			case "student":
				return "Student";
			case "staff":
				return "Staff";
			default:
				return null;
		}
	}
	
	public String validateRole(String role) {
		String normalized = normalizeRole(role);
		
		if (normalized == null) {
			return "Invalid role.";
		}
		return null;
	}
	
	public String validateUserId(String userId, String role) {
		if (!Validator.validateUserId(userId)) {
			return "Invalid User ID format.";
		}
		
		char prefix = userId.charAt(0);
		if (role.equalsIgnoreCase("Student") && prefix != 'U') {
			return "Student ID must start with 'U'.";
		}
		if (role.equalsIgnoreCase("Staff") && prefix != 'S') {
			return "Staff ID must start with 'S'.";
		}
		
		if (getUserById(userId) != null) {
			return "User ID already exists.";
		}
		
		return null;
	}
	
	public String validatePassword(String password) {
		if (!Validator.validatePassword(password)) {
			return "Password must be at least 8 characters and include uppercase, lowercase, digit, and special character.";
			}
		return null;
	}
	
	public String validateContact(String contact) {
		if (!Validator.validateContactNumber(contact)) {
			return "Invalid contact number. Must be 10 or 11-digit Malaysian phone number.";
		}
		return null;
	}
	
	public String validateProgramme(String role, String programme) {
		if (role.equalsIgnoreCase("Student")) {
			if (programme == null || programme.isEmpty()) {
				return "Programme is required.";
			}
		}
		return null;
	}
	
	public boolean registerUser(String userId, String password, String name,
							String faculty, String contact, String role, String programme){
		
		if (getUserById(userId) != null) {
			System.out.println("User ID already exists.");
			return false;
	    }
		
		User newUser;
		
		if(role.equalsIgnoreCase("Student")) {
			newUser = new Student(userId, password, name, faculty, contact, programme);
		} else if(role.equalsIgnoreCase("Staff")) {
			newUser = new Staff(userId, password, name, faculty, contact);
			
		} else {
			System.out.println("Invalid role.");
			return false;
		}
		
		// 4. Store
		users.add(newUser);
		saveUsers();
		System.out.println("User registered successfully.");
		return true;
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
			return;
		}
		
		user.setContactNumber(newContact);
		saveUsers();
		System.out.println("Profile updated.");
	}
	
	public void updatePassword(User user, String newPassword){

		if(user == null) {
			System.out.println("User not found.");
			return;
		}
		
		user.setPassword(newPassword);
		saveUsers();
	}
	
	public void loadUsers(){
		users.clear();
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