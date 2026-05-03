package util;

import java.time.LocalDate;
import java.time.LocalTime;

public class Validator {

	// User Validation
	
	public static boolean validateUserId(String userId) {
		if (userId == null || userId.length() != 4)
			return false;
		
		char first = userId.charAt(0);
		if (first != 'U' && first != 'S' && first != 'A') 
			return false;
		
		for (int i = 1; i < 4; i++) {
			if (!Character.isDigit(userId.charAt(i))) 
				return false;
		}
		return true;
	}

	public static boolean validatePassword(String password) {
		if (password == null || password.length() < 8) {
			return false;
		}
		
		boolean hasUpper = false;
		boolean hasLower = false;
		boolean hasDigit = false;
		boolean hasSpecial = false;
		
		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) {
				hasUpper = true;
			}
			else if (Character.isLowerCase(c)) {
				hasLower = true;
			}
			else if (Character.isDigit(c)) {
				hasDigit = true;
			}
			else {
				hasSpecial = true;
			}
		}
		
		return hasUpper && hasLower && hasDigit && hasSpecial;
	}

	public static boolean validateContactNumber(String contact) {
		if (contact.length() != 10 && contact.length() != 11) 
			return false;
		
		for (int i = 0; i < contact.length(); i++) {
			if (!Character.isDigit(contact.charAt(i))) return false;
		}
			
		return true;
	}
		
	// Date & Time Validation

	public static boolean validateDate(String date) {
		try {
			LocalDate.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean validateTime(String time) {
		try {
			LocalTime.parse(time);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean validateTimeSlot(String startTime, String endTime) {
		try {
			LocalTime start = LocalTime.parse(startTime);
			LocalTime end = LocalTime.parse(endTime);
			
			return start.isBefore(end);
		} catch (Exception e) {
			return false;
		}
	}

	// Email (optional)
		
	public static boolean validateEmail(String email) {
		if (email == null || !email.contains("@")) return false;
		if (!email.contains(".")) return false;
		return true;
	}
		
}