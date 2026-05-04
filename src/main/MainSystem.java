package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import user.*;
import facility.*;
import booking.*;
import maintenance.*;
import util.*;

public class MainSystem {

	private UserManager userManager;
	private FacilityManager facilityManager;
	private BookingManager bookingManager;
	private MaintenanceManager maintenanceManager;
	private NotificationService notificationService;
	
	protected User currentUser;

	private Scanner scanner;

	public MainSystem() {
		userManager = new UserManager();
		facilityManager = new FacilityManager();
		bookingManager = new BookingManager();
		maintenanceManager = new MaintenanceManager();
		notificationService = new NotificationService();
		scanner = new Scanner(System.in);
	}

	// System start
	public void startSystem() {

		// load all data
		userManager.loadUsers();
		facilityManager.loadFacilities();
		bookingManager.loadBookings();
		bookingManager.loadFacilities();
		maintenanceManager.loadReports();
		
		System.out.println("======== System Started ========");
	
		while (true) {
			displayWelcomeMenu();
			int choice = getMenuChoice();
			
			switch (choice) {
				case 1:
					login();
					if (currentUser == null) {
						break;
					}
					checkUpcomingBookings();
					handleUserMenu(currentUser);
					break;
					
				case 2:
					handleRegister();
					break;
					
				case 0:
					exitSystem();
					System.exit(0);
					break;
					
				default:
					System.out.println("Invalid choice.");
			}
		}
	}
	
	// Welcome Menu
	private void displayWelcomeMenu() {

		System.out.println("\n============ Welcome ===========");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("0. Exit");
	}
	
	// Login
	private void login() {
		while (true) {

			System.out.print("User ID (Enter 0 to back): ");
			String userId = scanner.nextLine();
				
			if (userId.equals("0")) {
				System.out.println("Exit login...");
				return;
			}
					
			System.out.print("Password: ");
			String password = scanner.nextLine();

			User user = userManager.loginUser(userId, password);

			if (user != null) {
				currentUser = user;
					
				//booking.getCurrentUser
				bookingManager.setCurrentUser(user);
				maintenanceManager.setCurrentUser(user);
	
				System.out.println("Login successful.");
				break;
			} else {
				System.out.println("Invalid credentials. Try again.");
			}
		}
	}
		
	// Register
	private void handleRegister() {

		System.out.println("\n======= Register Account =======");
		
		// ===== Role =====
		String role;
		while (true) {
			System.out.println("Note: Admin accounts cannot be registered manually, ");
			System.out.println("Only Student and Staff accounts can be registered, ");
			System.out.println("Please enter 'Student' or 'Staff' to select the role. \n");
			System.out.print("Role: ");
			role = scanner.nextLine();
			
			if (role.equals("0")) {
				System.out.println("Exit regisThey are created and managed by the system administrator.tration.");
				return;
			}
			
			String err = userManager.validateRole(role);
			if (err != null) {
				System.out.println(err);
				continue;
			}
			role = userManager.normalizeRole(role);
			break;
		}
		
		// ===== User ID =====
		
		String userId;
		while (true) {
			System.out.print("User ID (Enter 0 to back): ");
			userId = scanner.nextLine();
			
			if (userId.equals("0")) {
				System.out.println("Exit registration...");
				return;
			}
			
			String err = userManager.validateUserId(userId, role);
			if (err != null) {
				System.out.println(err);
				continue;
			}
			
			break;
		}

		// ===== Password =====
		String password;
		
		while (true) {
			System.out.print("Password (Enter 0 to back): ");
			password = scanner.nextLine();
			
			if (password.equals("0")) {
				System.out.println("Exit registration...");
				return;
			}
			
			String err = userManager.validatePassword(password);
			if (err != null) {
				System.out.println(err);
				continue;
			}
			
			break;
		}
		
		// ===== Name =====
		String name;
		while (true) {
			System.out.print("Name: ");
			name = scanner.nextLine();
	
			if (name == null || name.trim().isEmpty()) {
				System.out.println("Name cannot be empty.");
				continue;
			}
			break;
		}
		
		String faculty;
		while (true) {
			System.out.print("Faculty/Department: ");
			faculty = scanner.nextLine();
	
			if (faculty == null || faculty.trim().isEmpty()) {
				System.out.println("Faculty/Department cannot be empty.");
				continue;
			}
			break;
		}
		
		// ===== Programme (only for Student) =====
		String programme = null;
		
		if (role.equals("Student")) {
			while (true) {
				System.out.print("Programme: ");
				programme = scanner.nextLine();

				String err = userManager.validateProgramme(role, programme);
				if (err != null) {
					System.out.println(err);
					continue;
				}
				break;
			}
		}
		
		// ===== Contact =====
		String contact;
		while (true) {
			System.out.print("Contact Number: ");
			contact = scanner.nextLine();
			
			String err = userManager.validateContact(contact);
			if (err != null) {
				System.out.println(err);
				continue;
				}
			break;
		}
		
		// ===== Final Create =====
		boolean success = userManager.registerUser(userId, password, name, faculty, 
													contact, role, programme);
		if (!success) {
			System.out.println("Registration failed.");
		}
	}

	

	// Main menu
	public void displayMainMenu() {
	
		System.out.println("\n=========== Main Menu ==========");
		System.out.println("1. View Profile");
		System.out.println("2. Facility Search");
		System.out.println("3. Booking");
		System.out.println("4. Maintenance");
		System.out.println("5. Reports");
		System.out.println("6. Logout");
		System.out.println("0. Exit");
	}

	public int getMenuChoice() {

		System.out.print("Enter choice: ");
		String input = scanner.nextLine();
		
		try {
			return Integer.parseInt(input);
		}
		catch(Exception e) {
			return -1;
		}

	}

	// User menu handler
	public void handleUserMenu(User user) {

		while (true) {
	
			displayMainMenu();
			int choice = getMenuChoice();
		
			switch (choice) {
		
				case 1:
				handleViewProfile();
				break;

				case 2:
				handleFacilitySearch();
				break;

				case 3:
				handleBookingMenu(user);
				break;

				case 4:
				handleMaintenanceMenu(user);
				break;
			
				case 5:
				handleReportsMenu();
				
				if(currentUser.getRole().equals("Admin")) {
					handleAdminMaintenanceReport();
				}
				break;
			
				case 6:
				logout();
				return;
			
				case 0:
				exitSystem();
				System.exit(0);
				break;
			
				default:
				System.out.println("Invalid choice.");
			}
		}
	}
	
	private void pause(String message) {
	    System.out.print(message);
	    scanner.nextLine();
	}
	
	private void handleViewProfile() {
		while (true) {
			System.out.println("\n======= My Profile =======");
			currentUser.viewProfile();
			
			System.out.println("\nEnter 1 to Edit Contact.");
			System.out.println("Enter 2 to Reset Password.");
			System.out.println("Enter 0 to Back.");
			
			int choice = getMenuChoice();
			
			switch (choice) {
				case 1:
					editContact();
					break;
				case 2:
					resetPassword();
					break;
				case 0:
					return;
				default:
					System.out.println("Invalid choice.");
			}
		}
	}
	
	private void editContact() {
		while (true) {
			System.out.print("Enter new contact (0 to cancel): ");
			String newContact = scanner.nextLine();
			
			if (newContact.equals("0")) {
				return;
			}
			
			String err = userManager.validateContact(newContact);
			if (err != null) {
				System.out.println(err);
				continue;
			}
			
			userManager.updateUser(currentUser, newContact);
			break;
		}
	}
	
	private void resetPassword() {

		// Validate current password
		System.out.print("Enter current password: ");
		String oldPassword = scanner.nextLine();

		if (!currentUser.getPassword().equals(oldPassword)) {
			System.out.println("Incorrect password.");
			return;
		}

		// Validate new password
		String newPassword;
		String confirmPassword;

		while (true) {
			System.out.print("Enter new password: ");
			newPassword = scanner.nextLine();
	
			String err = userManager.validatePassword(newPassword);
			if (err != null) {
				System.out.println(err);
				continue;
			}
	
			System.out.print("Confirm new password: ");
			confirmPassword = scanner.nextLine();
	
			if (!newPassword.equals(confirmPassword)) {
				System.out.println("Passwords do not match.");
			continue;
			}
			
			if (newPassword.equals(oldPassword)) {
				System.out.println("New password cannot be same as old password.");
				continue;
			}
	
			break;
		}

		// Update
		userManager.updatePassword(currentUser, newPassword);
		
		System.out.println("Password updated successfully.");
	}

	// Facility
	private void handleFacilitySearch() {
		System.out.println("\n======= Facility Search =======");
		List<Facility> selectedList;
		
		// choose type
		while (true) {
			
			System.out.print("Enter facility type (or 1 to view list): ");
			String input = scanner.nextLine();
			
			List<Facility> result = facilityManager.findFacilities(input);
			
			if (result.isEmpty()) {
				System.out.println("No facility found.");
				continue;
			}
			
			if (input.equals("1")) {
				printFacilityList(result);
				continue;
			}
			
			printFacilityList(result);
			selectedList = result;
			break;
		}
		
		// keyin type
		
		String dateStr;
		LocalDate date;
		
		while (true) {
			System.out.print("Date (yyyy-mm-dd): ");
			dateStr = scanner.nextLine();
			
			if (!Validator.validateDate(dateStr)) {
				System.out.println("Invalid date format.");
				continue;
			}
			
			date = LocalDate.parse(dateStr);
			break;
		}
		
		String startStr;
		String endStr;
		LocalTime start;
		LocalTime end;
		while (true) {
			System.out.print("Start time (HH:mm): ");
			startStr = scanner.nextLine();
			    
			System.out.print("End time (HH:mm): ");
			endStr = scanner.nextLine();
				
			if (!Validator.validateTimeSlot(startStr, endStr)) {
				System.out.println("Invalid time slot.");
				continue;
			}
			
			start = LocalTime.parse(startStr);
			end = LocalTime.parse(endStr);
			break;
		}
		
		TimeSlot ts = new TimeSlot(date, start, end);
		
		// check facility
		System.out.println("\nAvailable Facilities:");
		
		boolean found = false;
		
		for (Facility f : selectedList) {
			
			if (!f.checkAvailability()) continue;
			if (bookingManager.isBooked(f.getFacilityId(), ts)) continue;
			
			System.out.println("- " + f.getFacilityName());
			found = true;
		}
		
		if (!found) {
			System.out.println("No available facilities for this time slot.");
		}
	}
	
	private void printFacilityList(List<Facility> list) {
		System.out.println("\n===== Facility List =====");
		
		System.out.printf("%-5s %-8s %-25s %-15s\n","No.", "ID", "Name", "Type");
		System.out.println("------------------------------------------");
		
		int i = 1;
		for (Facility f : list) {
			System.out.printf("%-5d %-8s %-25s %-15s\n", i++, f.getFacilityId(), 
								f.getFacilityName(), f.getFacilityType());
		}
	}

	// Booking
	private void handleBookingMenu(User user) {

		while(true) {
		
			System.out.println("\n========= Booking Menu =========");
			System.out.println("1. Create Booking");
			System.out.println("2. Modify Booking");
			System.out.println("3. Cancel Booking");
			System.out.println("4. Show My Bookings");
			
			//check admin role
			if(user.getRole().equals("Admin")) {
				
				System.out.println("5. Show All Bookings");
				System.out.println("6. Approve Booking");
				System.out.println("7. Reject Booking");
			}
			System.out.println("0. Back");
		
			int choice = getMenuChoice();
		
			switch (choice) {
		
				case 1:
					System.out.println("\nCreating booking...");
					bookingManager.createBooking();
					break;
			
				case 2:
					System.out.println("\nModify Booking...");
					bookingManager.modifyBooking();
					break;
			
				case 3:
					System.out.println("\nCancel Booking...");
					bookingManager.cancelBooking();
					break;
				
				case 4:
					System.out.println("\nShow My Bookings...\n");
					bookingManager.showMyBookings(currentUser);
					break;
					
				case 5:
					if(user.getRole().equals("Admin")) {
						System.out.println("\nShow All Bookings...\n");
						bookingManager.showAllBookings();
						break;
					}
					else {
						break;
					}
				
				case 6:
					if(user.getRole().equals("Admin")) {
						System.out.println("\nApprove Booking...");
						bookingManager.approveBooking();
						break;
						}
					else {
						break;
					}
					
				case 7:
					if(user.getRole().equals("Admin")) {
						System.out.println("\nReject Booking...");
						bookingManager.rejectBooking();
						break;
					}
					else {
						break;
					}
				
				case 0:
					return;	//back to main menu
					
				default:
					System.out.println("Invalid Choice!");
					System.out.println("Please choose again: ");
			}
		}
	}

	// Maintenance
	private void handleMaintenanceMenu(User user) {

		System.out.println("\n======= Maintenance Menu =======");
		System.out.println("1. Reporting Issue");
		System.out.println("2. View History");
		if(user.getRole().equals("Admin")) {
			System.out.println("3. Assign Maintenance Task");
			System.out.println("4. Update Maintenance Task");
		}
		
		System.out.println("0. Back");
	
		int choice = getMenuChoice();
	
		switch (choice) {
	
			case 1:
			System.out.println("Creating maintenance report...");
			handleCreateMaintenanceReport();
			break;
		
			case 2:
			handleViewMaintenanceHistory(user);
			break;
			
			case 3:
			maintenanceManager.assignMaintenance();
			break;
			
			case 4:
			maintenanceManager.updateMaintenanceStatus();
			
			default:
			break;
		}
	}

	// Reports
	private void handleReportsMenu() {
		
		List<String> facilityIds = bookingManager.getUserFacilityIds(currentUser);
		List<Integer> counts = bookingManager.getCounts();

		System.out.println("\n===== Facility Usage Report =====\n");

		if (facilityIds.isEmpty()) {
			System.out.println("No booking records found.");
			return;
		}

		System.out.printf("%-12s %-20s %-10s\n", "Facility ID", "Facility Name", "Numbers of booking");
		System.out.println("------------------------------------------------");

		int total = 0;

		for (int i = 0; i < facilityIds.size(); i++) {

			String id = facilityIds.get(i);
			int count = counts.get(i);
	
			Facility f = bookingManager.getFacilityById(id);
			String name;

			if (f != null) {
				name = f.getFacilityName();
			} else {
				name = "Unknown";
			}
			
			System.out.printf("%-12s %-20s %-10d\n", id, name, count);
			total += count;
		}

		// Summary
		System.out.println("\n===== Summary =====");
		System.out.println("Total Bookings: " + total);

		// type analysis
		List<String> types = bookingManager.getUserFacilityTypes(currentUser);
		List<Integer> typeCounts = bookingManager.getTypeCounts();

		String topType = null;
		int max = 0;

		for (int i = 0; i < types.size(); i++) {
			if (typeCounts.get(i) > max) {
				max = typeCounts.get(i);
				topType = types.get(i);
			}
		}

		double percentage;
		if (total == 0) {
			percentage = 0;
		} else {
			percentage = max * 100.0 / total;
		}

		System.out.println("Most Used Facility Type: " + topType);
		System.out.println("Numbers of booking of most used facility type: " + max);
		System.out.println("Propostion of total booking for most used facility type: " + percentage + "%");
		System.out.println("This report only generate from APPROVED bookings.");
	}
	
	private void handleCreateMaintenanceReport() {

		System.out.println("\n=== Reporting Issues ===");
		System.out.println("1. Maintenance Issues");
		System.out.println("0. Back");

		int choice = getMenuChoice();

		if (choice == 0)
			return;

		if (choice != 1) {
			System.out.println("Invalid choice.");
			return;
		}

		// facility ID
		String facilityId;

		while (true) {
			System.out.println("Enter Facility ID: ");
			System.out.println("(Enter 1 to show facility list, enter 0 to back)");
			facilityId = scanner.nextLine();
			
			if (facilityId.equals("0"))
				return;
			
			List<Facility> result = facilityManager.findFacilities(facilityId);
			
			if (facilityId.equals("1")) {
				printFacilityList(result);
				continue;
			}
	
			if (maintenanceManager.checkFacilityId(facilityId))
				break;
		}

		// Description
		System.out.print("Enter description: ");
		String description = scanner.nextLine();

		// priority
		String priority;
		while (true) {
			System.out.print("Priority (LOW/MEDIUM/HIGH): ");
			priority = scanner.nextLine().toUpperCase();
			
			if (priority.equals("LOW") || priority.equals("MEDIUM") || priority.equals("HIGH")) 
				break;
			
			System.out.println("Invalid priority.");
		}

		// ===== Create Report =====
		maintenanceManager.createMaintenance(facilityId, currentUser.getUserId(), description, priority);

		System.out.println("Maintenance report created.");
	}
	
	private void handleViewMaintenanceHistory(User user) {

		List<MaintenanceReport> list = maintenanceManager.getMaintenanceHistory(user);

		System.out.println("\n===== Maintenance History =====");

		if (list.isEmpty()) {
			System.out.println("No records found.");
			return;
		}

		System.out.printf("%-10s %-10s %-15s %-30s\n", "ReportID", "Facility", "Date", "Description");
		System.out.println("---------------------------------------------------------------");

		for (MaintenanceReport r : list) {
			System.out.printf("%-10s %-10s %-15s %-30s\n", r.getReportId(),
							r.getFacilityId(), r.getReportDate(), r.getDescription());
		}
	}
	
	private void handleAdminMaintenanceReport() {

		System.out.println("\n===== Admin Facility Utilization Report =====");
	
		List<String> facilityIds = bookingManager.getAllFacilityIds();
		List<Integer> counts = bookingManager.getCounts();
		List<Double> hours = bookingManager.getHours();
		List<String> peakHours = bookingManager.getPeakHoursPerFacility(facilityIds);
	
		if (facilityIds.isEmpty()) {
			System.out.println("No data.");
			return;
		}
	
		YearMonth now = YearMonth.now();
		int totalHours = now.lengthOfMonth() * 24;
	
		System.out.printf("%-10s %-20s %-13s %-13s %-13s\n", "Facility", "Facility", "Numbers of", "Utilization", "Peak");
		System.out.printf("%-10s %-20s %-13s %-13s %-13s\n", "ID", "Name", "booking", "Rate", "Hour");
		System.out.println("----------------------------------------------------------");
	
		double maxRate = 0;
		String maxFacility = null;
	
		for (int i = 0; i < facilityIds.size(); i++) {
	
			String id = facilityIds.get(i);
			int count = counts.get(i);
			double h = hours.get(i);
			String peak = peakHours.get(i);
		
			Facility f = bookingManager.getFacilityById(id);
			String name = (f != null) ? f.getFacilityName() : "Unknown";
		
			double rate = (h / totalHours) * 100;
		
			System.out.printf("%-10s %-20s %-13d %-9.2f%% %-13s\n", id, name, count, rate, "    " + peak+":00");
		
			if (rate > maxRate) {
				maxRate = rate;
				maxFacility = name;
			}
		}

		// Summary
		System.out.println("\n===== Summary =====");
	
		// most used type
		List<String> types = bookingManager.getAllFacilityTypes();
		List<Integer> typeCounts = bookingManager.getTypeCounts();
	
		String topType = null;
		int max = 0;
	
		for (int i = 0; i < types.size(); i++) {
			if (typeCounts.get(i) > max) {
				max = typeCounts.get(i);
				topType = types.get(i);
			}
		}

		System.out.println("Most Frequently Used Facility Type: " + topType);
		System.out.println("Facility with Highest Utilization: " + maxFacility);
		System.out.println("This report only generate from APPROVED bookings.");
		System.out.println("=============================================");
	}

	// Notification
	public void checkUpcomingBookings() {

		for (Booking b : bookingManager.getUpcomingBookings(currentUser)) {
			String msg = notificationService.generateBookingReminder(b);
			
			if(msg != null) {
				System.out.println(msg);
			}
		}
	}

	// Log out
	public void logout() {

		currentUser = null;
		System.out.println("Logged out.");
	}

	// Exit
	public void exitSystem() {

		userManager.saveUsers();
		facilityManager.saveFacilities();
		bookingManager.saveBookings();
		maintenanceManager.saveReports();
	
		System.out.println("System exited. Data saved.");
	}
}