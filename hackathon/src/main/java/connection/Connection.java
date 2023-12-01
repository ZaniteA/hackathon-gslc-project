package connection;

import java.io.*;  
import java.util.ArrayList;
import java.util.Scanner;

public class Connection {
	
	private static final Connection instance = new Connection();
	
	private String folderPath = "database/";
	
	// private constructor to avoid client application using the constructor
	// based on Singleton principle
	private Connection() {}

	public static Connection getInstance() {
		return instance;
	}
	
	public ArrayList<String[]> readCsv(String fileName){
		ArrayList<String[]> resData = new ArrayList<String[]>();
		
		String filePath = this.folderPath+fileName;
		
		File f = new File(filePath);
		
		try (Scanner sc = new Scanner(f)){
			while(sc.hasNextLine()) {
				String row = sc.nextLine();
				String[] rowData = row.split(",");
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
	
	public void writeCsv(String fileName, String[] rowData) {
		String filePath = this.folderPath+fileName;
		
		try(FileWriter fw = new FileWriter(filePath, true)){
			BufferedWriter out = new BufferedWriter(fw);
			for(int i = 0; i < rowData.length; i++) {
				out.write(rowData[i]);
				if(i != rowData.length-1) {
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
