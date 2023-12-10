package menu;

import java.util.ArrayList;
import java.util.Scanner;

import query.Query;

public class Menu {
	// Class that displays the menu to the user
	// and bridges the interaction between the user and the other classes.

	static int sect;
	static Boolean failed;
	static String errorMessage;
	static Scanner userInput = new Scanner(System.in);
	static Query query = new Query();
	
	/*
	 * List of Sections:
	 * 1 -> Main Menu
	 * 2 -> Insert Data
	 * 3 -> Show Data
	 * 4 -> Delete Data
	 * 5 -> Exit
	 */
	
	// Clears the terminal screen.
	private static void clearScreen() {
		System.out.print("\033[H\033[2J");  
	    System.out.flush();
	}

	// Generates an error, which will be displayed with printError().
	private static void generateError(String message) {
		failed = true;
		errorMessage = message;
	}
	
	// Prints any error message that might have popped up.
	private static void printError() {
		// Only print error message if failed boolean is on
		if (failed) {
			System.out.println("Previous query failed, reason: " + errorMessage);

			// Reset failed status
			errorMessage = "";
			failed = false;
		}
	}
	
	// Reads an integer in the expected range.
	// Returns the integer if the read was successful, or null otherwise,
	// and sends an error message
	private static Integer readRangedInteger(int lo, int hi, String message) {
		int ret = 0;
		try {
			ret = userInput.nextInt();
		} catch (Exception e) {
			generateError(message);
			return null;
		}
		userInput.nextLine();

		if (ret < lo || ret > hi) {
			generateError(message);
			return null;
		}

		return ret;
	}
	
	// Displays the home menu.
	private static void homeMenu() {
		// Print previous error (if any)
		printError();
		
		// Show home menu
		System.out.println("Hello, what would you like to do?");
		System.out.println("1. Insert Data");
		System.out.println("2. Show Data");
		System.out.println("3. Delete Data");
		System.out.println("4. Exit");
		System.out.print("> ");
		
		// Get user input
		Integer resp = readRangedInteger(1, 4, "Undefined menu section");
		if (resp == null) return;
		
		// Validate input
		if(resp == 1) { // Insert data
			sect = 2;
		} else if(resp == 2) { // Show data
			sect = 3;
		} else if(resp == 3) { // Delete data
			sect = 4;
		} else if (resp == 4) { // Exit program
			sect = 5;
		}
		
		clearScreen();
		
		return;
	}
	
	// Displays the insert data menu.
	private static void insertMenu() {
		// Print previous error (if any)
		printError();
		
		// Ask which table to insert
		System.out.println("Which table to insert?");
		System.out.println("1. User");
		System.out.println("2. Team");
		System.out.print("> ");
		
		// Get user input
		Integer table = readRangedInteger(1, 2, "Invalid table");
		if (table == null) return;
		
		// Create ArrayList of fields 
		ArrayList<String> fields = new ArrayList<String>();
		
		// Declare temporary String for user input
		String tmp;
		
		// Get data from user
		if (table == 1) {
			System.out.print("Add NIM: ");
			tmp = userInput.nextLine();
			fields.add(tmp);

			System.out.print("Add Name: ");
			tmp = userInput.nextLine();
			fields.add(tmp);

			System.out.print("Add Team: ");
			tmp = userInput.nextLine();
			fields.add(tmp);

		} else if (table == 2) {
			System.out.print("Add Team Name: ");
			tmp = userInput.nextLine();
			fields.add(tmp);

		} else {
			generateError("Invalid table");
			return;
		}
		
		if (!query.insert(table, fields)) {
			generateError("Error occured during insertion");
		} else {
			// Display success message
			System.out.println("Data successfully inserted");
		}
		
		System.out.println("Press Enter to continue");
		System.out.print("> ");
		String buf = userInput.nextLine();

		clearScreen();
		
		// Back to Main Menu
		sect = 1;
		
		return;
	}
	
	// Displays the show data menu.
	private static void showMenu() {
		// Print previous error (if any)
		printError();
		
		// Ask which table to show
		System.out.println("Which table to show?");
		System.out.println("1. User");
		System.out.println("2. Team");
		System.out.print("> ");
		
		// Get table
		Integer table = readRangedInteger(1, 2, "Invalid table");
		if (table == null) return;
		
		// Ask if the user wants to filter by some special condition
		System.out.println("Want to filter by condition?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		System.out.print("> ");
		
		// Get condition status
		Integer stat = readRangedInteger(1, 2, "Invalid filter choice");
		if (stat == null) return;
		
		String cond = null;
		
		if (stat == 1) {
			System.out.println("Add condition (seperate by semicolon ';')");
			System.out.println("Valid formats include:");
			System.out.println("[field];[operator];[value]");
			System.out.println("[field];[operator];[value];join;[table name]");
			System.out.print("> ");
			cond = userInput.nextLine();
		}
		
		if (!query.show(table, cond)) {
			generateError("Error occured during display");
		}

		System.out.println("Press Enter to continue");
		System.out.print("> ");
		String buf = userInput.nextLine();

		clearScreen();

		// Back to Main Menu
		sect = 1;

		return;
	}
	
	// Shows the delete data menu.
	public static void deleteMenu() {
		// Print previous error (if any)
		printError();
		
		// Ask which table to show
		System.out.println("Which table to delete?");
		System.out.println("1. User");
		System.out.println("2. Team");
		System.out.print("> ");
		
		// Get table
		Integer table = readRangedInteger(1, 2, "Invalid table");
		if (table == null) return;
		
		String key_data;
		
		if (table == 1) {
			System.out.println("Enter User's NIM: ");
			System.out.print("> ");
			key_data = userInput.nextLine();
		} else {
			System.out.println("Enter Team Name: ");
			System.out.print("> ");
			key_data = userInput.nextLine();
		}
		
		if (!query.delete(table, key_data)) {
			generateError("Error occurred during deletion");
		} else {
			// Display success message
			System.out.println("Data successfully deleted");
		}

		System.out.println("Press Enter to continue");
		System.out.print("> ");
		String buf = userInput.nextLine();

		clearScreen();
		
		// Back to Main Menu
		sect = 1;
		
		return;
	}
	
	// Displays the correct menu section while the user has not exited the program
	public static void runMenu() {
		sect = 1;
		failed = false;
		while (sect != 5) {
			// Show the correct menu section
			if (sect == 1) {
				homeMenu();
			} else if (sect == 2) {
				insertMenu();
			} else if (sect == 3) {
				showMenu();
			} else if (sect == 4) {
				deleteMenu();
			} else {
				// Should not happen in normal circumstances
				System.err.println("Invalid sect variable on Menu function");
				break;
			}
		}
		System.out.println("Thank you for your visit!");
	}

	// An instance of the class is not supposed to be needed
	// as all members are static
	private Menu() {

	}
	
}
