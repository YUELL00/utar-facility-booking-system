package main;

import java.time.LocalTime;
import java.util.List;
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
			System.out.println("Please select either Student or Staff. \n");
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

	public void displayReportsMenu() {

		System.out.println("\n========= Reports Menu =========");
		System.out.println("1. Facility Utilization");
		System.out.println("2. Peak Booking");
		System.out.println("3. Maintenance Report");
		System.out.println("0. Back");
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
				handleMaintenanceMenu();
				break;
			
				case 5:
				handleReportsMenu();
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
			
			System.out.println("\nEnter 1 to Edit Contact.\nEnter 0 to Back.");
			
			int choice = getMenuChoice();
			
			switch (choice) {
				case 1:
					editContact();
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

	// Facility
	private void handleFacilitySearch() {

		System.out.print("Date (yyyy-mm-dd): ");
		String date = scanner.nextLine();

		System.out.print("Start time (HH:mm): ");
		LocalTime start = LocalTime.parse(scanner.nextLine());

		System.out.print("End time (HH:mm): ");
		LocalTime end = LocalTime.parse(scanner.nextLine());

		System.out.print("Type: ");
		String type = scanner.nextLine();

		List<Facility> result = facilityManager.searchFacilities(date, start, end, type, bookingManager);

		if (result.isEmpty()) {
			System.out.println("No available facilities found.");
		} else {
			result.forEach(System.out::println);
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
					// 简化：只调用 manager 
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
	private void handleMaintenanceMenu() {

		System.out.println("\n======= Maintenance Menu =======");
		System.out.println("1. Create Report");
		System.out.println("2. View History");
		System.out.println("0. Back");
	
		int choice = getMenuChoice();
	
		switch (choice) {
	
			case 1:
			System.out.println("Creating maintenance report...");
			break;
		
			case 2:
			maintenanceManager.getMaintenanceHistory();
			break;
		
			default:
			break;
		}
	}

	// Reports
	private void handleReportsMenu() {

		while (true) {
	
			displayReportsMenu();
			int choice = getMenuChoice();
		
			switch (choice) {
		
				case 1:
				System.out.println(facilityManager.generateUtilizationReport());
				break;
			
				case 2:
				bookingManager.generatePeakBookingReport();
				break;
			
				case 3:
				maintenanceManager.generateMaintenanceReport();
				break;
			
				case 0:
				return;
			
				default:
				System.out.println("Invalid choice.");
			}
		}
	}

	// Notification
	public void checkUpcomingBookings() {

		for (Booking b : bookingManager.getUpcomingBookings()) {
			String msg = notificationService.generateBookingReminder(b);
			System.out.println(msg);
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