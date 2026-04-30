package main;

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
		maintenanceManager.loadReports();
	
		System.out.println("=== System Started ===");
	
		while (true) {
			displayWelcomeMenu();
			int choice = getMenuChoice();
			
			switch (choice) {
				case 1:
					login();
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
		System.out.println("\n=== Welcome ===");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("0. Exit");
	}
	
	// Register
	private void handleRegister() {
		System.out.println("\n=== Register Account ===");
		
		System.out.println("User ID: ");
		String userID = scanner.nextLine();
		
		System.out.println("Password: ");
		String password = scanner.nextLine();
		
		System.out.println("Name: ");
		String name = scanner.nextLine();
		
		System.out.println("Faculty/Department: ");
		String faculty = scanner.nextLine();
		
		System.out.println("Contact Number: ");
		String contact = scanner.nextLine();
		
		System.out.println("Role (Student/Staff): ");
		String role = scanner.nextLine();
		
		String programme = null;

		if(role.equalsIgnoreCase("Student")) {
			System.out.print("Programme: ");
			programme = scanner.nextLine();
		}
		
		userManager.registerUser(userID, password, name, faculty, contact, role, programme);
	}

	// Login
	private void login() {
		while (true) {
	
			System.out.print("User ID: ");
			String userId = scanner.nextLine();
		
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

	// Main menu
	public void displayMainMenu() {
		System.out.println("\n=== Main Menu ===");
		System.out.println("1. View Profile");
		System.out.println("2. Facility Search");
		System.out.println("3. Booking");
		System.out.println("4. Maintenance");
		System.out.println("5. Reports");
		System.out.println("6. Logout");
		System.out.println("0. Exit");
	}

	public void displayReportsMenu() {
		System.out.println("\n=== Reports Menu ===");
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
	
			checkUpcomingBookings();
		
			displayMainMenu();
			int choice = getMenuChoice();
		
			switch (choice) {
		
				case 1:
				user.viewProfile();
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
	
	

	// Facility
	private void handleFacilitySearch() {

		System.out.print("Date: ");
		String date = scanner.nextLine();
	
		System.out.print("Time Slot: ");
		String timeSlot = scanner.nextLine();
	
		System.out.print("Type: ");
		String type = scanner.nextLine();
	
		facilityManager.searchFacilities(date, timeSlot, type);
	}

	// Booking
	private void handleBookingMenu(User user) {

		System.out.println("\n=== Booking Menu ===");
		System.out.println("1. Create Booking");
		System.out.println("2. Modify Booking");
		System.out.println("3. Cancel Booking");
		if(user.getRole().equalsIgnoreCase("Admin")) {
			System.out.println("4. Show All Bookings");
			System.out.println("5. Approve Booking");
			System.out.println("6. Reject Booking");
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
			if(user.getRole().equalsIgnoreCase("Admin")) {
				System.out.println("\nShow All Bookings...");
				bookingManager.showAllBookings();
			}
			else {
				break;
			}
			
			case 5:
			if(user.getRole().equalsIgnoreCase("Admin")) {
				System.out.println("\nApprove Booking...");
				bookingManager.approveBooking();;
				}
			else {
				break;
			}
				
			case 6:
			if(user.getRole().equalsIgnoreCase("Admin")) {
				System.out.println("\nReject Booking...");
				bookingManager.rejectBooking();;
			}
			else {
				break;
			}
				
			default:
			break;
		}
	}

	// Maintenance
	private void handleMaintenanceMenu() {

		System.out.println("\n=== Maintenance Menu ===");
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