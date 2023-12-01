package main;

import java.util.ArrayList;

import connection.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        
        Connection conn = Connection.getInstance();
        
        String[] data = {"69", "1TirtAnd2Birds"};
        conn.writeCsv("teams.csv", data);
        
        ArrayList<String[]> userData = conn.readCsv("teams.csv");
        
        for(String[] rowData : userData) {
        	for(int i = 0; i < rowData.length; i++) {
        		System.out.print(rowData[i] + " ");
        	}
        	System.out.println();
        }
    }
}