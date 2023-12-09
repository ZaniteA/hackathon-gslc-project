package menu;

import java.util.Scanner;

public class Menu {
	static int sect;
	static Boolean failed;
	static String errorMessage;
	static Scanner userInput = new Scanner(System.in);
	
	/*
	 * List of Section:
	 * 1 -> Main Menu
	 * 2 -> Insert Data
	 * 3 -> Show Data
	 * 4 -> Exit
	 * 
	 */
	
	private static void clearScreen() {
		System.out.print("\033[H\033[2J");  
	    System.out.flush();
	}
	
	private static void printError() {
		// Only print error message if faile boolean is on
		if(failed) {
			System.out.println("Previous query failed, reason: " + errorMessage);
			errorMessage = "";
			failed = false;
		}
	}
	
	private static void homeMenu() {
		// Print previous error (if any)
		printError();
		
		// Show home menu
		System.out.println("Hello, what would you like to do?");
		System.out.println("1. Insert Data");
		System.out.println("2. Show Data");
		System.out.println("3. Exit");
		System.out.print("> ");
		
		// Get user input
		int resp = userInput.nextInt();
		userInput.nextLine();
		
		// Validate input
		if(resp == 1) { // Insert data
			sect = 2;
		} else if(resp == 2) { // Show data
			sect = 3;
		} else if(resp == 3) { // Exit program
			sect = 4;
		} else { // Unidentified input
			errorMessage = "Undefined menu section";
			failed = true;
		}
		
		return;
	}
	
	private static void insertMenu() {
		// Print previous error (if any)
		printError();
		
		// Ask which table to insert
		System.out.println("Which table to insert?");
		System.out.println("1. User");
		System.out.println("2. Team");
		System.out.print("> ");
		
		// Get user input
		int table = userInput.nextInt();
		userInput.nextLine();
		
		if(table == 1) {
			System.out.print("Add Name: ");
			String name = userInput.nextLine();
			System.out.print("Add NIM: ");
			String nim = userInput.nextLine();
			System.out.print("Add Team: ");
			String team = userInput.nextLine();
		} else if(table == 2) {
			System.out.print("Add Team Name: ");
			String teamName = userInput.nextLine();
		} else {
			failed = true;
			errorMessage = "Invalid table";
			return;
		}
		
		// TODO: Do the insertion
		
		// Success Message
		System.out.println("Data successfully inserted");
		
		// Back to Main Menu
		sect = 1;
		
		return;
	}
	
	private static void showMenu() {
		// Print previous error (if any)
		printError();
		
		// Ask which table to show
		System.out.println("Which table to show?");
		System.out.println("1. User");
		System.out.println("2. Team");
		System.out.print("> ");
		
		// Get table
		int table = userInput.nextInt();

		if(table != 1 && table != 2) {
			failed = true;
			errorMessage = "Invalid Table";
			return;
		}
		
		// Ask if there user want some special condition
		System.out.println("Want to filter by condition?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		System.out.print("> ");
		
		// Get condition status
		int stat = userInput.nextInt();
		userInput.nextLine();
		
		String cond = "";
		
		if(stat == 1) {
			System.out.println("Add condition (seperate by semicolon ';')");
			System.out.print("> ");
			cond = userInput.nextLine();
		}
		
		// TODO: Show the table
		
		
		// Back to Main Menu
		sect = 1;
		
		return;
	}
	
	public static void runMenu() {
		sect = 1;
		failed = false;
		while(sect != 4) {
			// Show the correct menu section
			if(sect == 1) {
				homeMenu();
			} else if(sect == 2) {
				insertMenu();
			} else if(sect == 3) {
				showMenu();
			} else {
				System.err.println("Invalid sect variable on Menu function");
				break;
			}
		}
		System.out.println("Thank you for your visit!");
	}

	private Menu() {
	}
}