package util;

public class Validator {

	// User Validation
	
	public static boolean validateUserId(String userId) {
		if (userId == null || userId.length() < 2)
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
		return password != null && password.length() >= 6;
	}

	public static boolean validateContactNumber(String contact) {
		if (contact == null || contact.length() < 10) 
			return false;
		
		for (int i = 0; i < contact.length(); i++) {
			if (!Character.isDigit(contact.charAt(i))) return false;
		}
			
		return true;
	}
		
	// Date & Time Validation

	public static boolean validateDate(String date) {
		// format: yyyy-MM-dd
		if (date == null || date.length() != 10)
			return false;

		if (date.charAt(4) != '-' || date.charAt(7) != '-')
			return false;
	
		try {
			int year = Integer.parseInt(date.substring(0, 4));
			int month = Integer.parseInt(date.substring(5, 7));
			int day = Integer.parseInt(date.substring(8, 10));
			if (month < 1 || month > 12) return false;
			if (day < 1 || day > 31) return false;
		} catch (Exception e) {
			return false;
		}
			
		return true;
	}

	public static boolean validateTime(String time) {
		// format: HH:mm
		if (time == null || time.length() != 5)
			return false;
	
		if (time.charAt(2) != ':')
			return false;

		try {
			int hour = Integer.parseInt(time.substring(0, 2));
			int minute = Integer.parseInt(time.substring(3, 5));
			if (hour < 0 || hour > 23) return false;
			if (minute < 0 || minute > 59) return false;
		} catch (Exception e) {
				return false;
		}
	
		return true;
	}

	public static boolean validateTimeSlot(String startTime, String endTime) {
		if (!validateTime(startTime) || !validateTime(endTime))
			return false;
	
		if (startTime.equals(endTime))
			return false;
		
		return startTime.compareTo(endTime) < 0;
	}

	// Email (optional)
		
	public static boolean validateEmail(String email) {
		if (email == null || !email.contains("@")) return false;
		if (!email.contains(".")) return false;
		return true;
	}
		
}