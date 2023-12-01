package connection;

import java.io.*;  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Connection {
	
	private static final Connection instance = new Connection();
	
	private final String folderPath = "database/";

	public final String userFile  = "user.csv";
	public final String teamsFile = "teams.csv";
	
	// private constructor to avoid client application using the constructor
	// based on Singleton principle
	private Connection() {}

	public static Connection getInstance() {
		return instance;
	}
	
	public ArrayList<ArrayList<String>> readCsv(String fileName){
		ArrayList<ArrayList<String>> resData = new ArrayList<ArrayList<String>>();
		
		String filePath = this.folderPath+fileName;
		
		File f = new File(filePath);
		
		try (Scanner sc = new Scanner(f)){
			while(sc.hasNextLine()) {
				String row = sc.nextLine();
				ArrayList<String> rowData = new ArrayList<String>(Arrays.asList(row.split(",")));
				resData.add(rowData);
			}
		} catch (FileNotFoundException fe) {
			System.err.println("File " + f.getPath() + " was not found.");
		} catch (Exception e) {
			System.err.println("Something went wrong with reading " + f.getPath());
			System.err.println(e.getMessage());
		}
		
		return resData;
	}
	
	public void writeCsv(String fileName, ArrayList<String> rowData) {
		String filePath = this.folderPath+fileName;
		
		try(FileWriter fw = new FileWriter(filePath, true)){
			BufferedWriter out = new BufferedWriter(fw);
			for(int i = 0; i < rowData.size(); i++) {
				out.write(rowData.get(i));
				if(i != rowData.size()-1) {
					out.write(",");
				}
			}
			out.newLine();
			out.close();
		} catch(Exception e) {
			System.err.println("Something went wrong with writing to " + filePath);
			System.err.println(e.getMessage());
		}
		
	}
		
}
