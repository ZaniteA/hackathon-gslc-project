package connection;

import java.io.*;  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Connection {
	// Class that reads, writes, and clears CSV files in database.
	// Uses the Singleton principle.
	
	private static final Connection instance = new Connection();
	
	private final String folderPath = "hackathon/database/";

	public final String userFile  = "user.csv";
	public final String teamsFile = "teams.csv";
	
	// Private constructor to avoid client application using the constructor
	// based on Singleton principle
	private Connection() {}

	// Call this to retrieve the singleton instance
	public static Connection getInstance() {
		return instance;
	}
	
	// Reads a CSV file with name `fileName`, parses it,
	// and returns it as an ArrayList of ArrayList of Strings.
	// The header is not returned.
	public ArrayList<ArrayList<String>> readCsv(String fileName) {
		ArrayList<ArrayList<String>> resData = new ArrayList<ArrayList<String>>();
		
		String filePath = this.folderPath + fileName;
		File f = new File(filePath);

		Boolean first_row = true;
		
		try (Scanner sc = new Scanner(f)){
			while (sc.hasNextLine()) {
				String row = sc.nextLine();
				if (first_row) {
					first_row = false;
				} else {
					ArrayList<String> rowData = new ArrayList<String>(Arrays.asList(row.split(",")));
					resData.add(rowData);
				}
			}
		} catch (FileNotFoundException fe) {
			// If file was not found
			System.err.println("File " + f.getPath() + " was not found.");
		} catch (Exception e) {
			// If an error occurred while reading the file
			System.err.println("Something went wrong when reading " + f.getPath());
			System.err.println(e.getMessage());
		}
		
		return resData;
	}
	
	// Writes a single line to the end of the CSV file with file name `fileName`.
	// If there was no file with the name of `fileName`, a new file will be created
	// with that single line as content.
	public void writeCsv(String fileName, ArrayList<String> rowData) {
		String filePath = this.folderPath+fileName;
		
		try (FileWriter fw = new FileWriter(filePath, true)) {
			BufferedWriter out = new BufferedWriter(fw);
			for (int i = 0; i < rowData.size(); i++) {
				out.write(rowData.get(i));
				if (i != rowData.size()-1) {
					out.write(",");
				}
			}
			out.newLine();
			out.close();
		} catch (Exception e) {
			// If an error occurred while writing to the file
			System.err.println("Something went wrong when writing to " + filePath);
			System.err.println(e.getMessage());
		}
	}

	// Clears the CSV file with file name `fileName`.
	// If there was no file with the name of `fileName`, a new empty file will be created.
	public void clearCsv(String fileName) {
		String filePath = this.folderPath + fileName;
		
		try (FileWriter fw = new FileWriter(filePath)) {
			BufferedWriter out = new BufferedWriter(fw);
			out.close();
		} catch (Exception e) {
			// If an error occurred while clearing the file
			System.err.println("Something went wrong when clearing " + filePath);
			System.err.println(e.getMessage());
		}
	}
		
}
