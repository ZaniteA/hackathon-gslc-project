package main;

import java.util.ArrayList;

import connection.Connection;
import menu.Menu;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
//        
//        Connection conn = Connection.getInstance();
//        
//        
//        ArrayList<String> data = new ArrayList<String>();
//        data.add("69");
//        data.add("1TirtAnd2Birds");
//        conn.writeCsv("teams.csv", data);
//        
//        ArrayList<ArrayList<String>> userData = conn.readCsv("teams.csv");
//        
//        for(ArrayList<String> rowData : userData) {
//        	for(int i = 0; i < rowData.size(); i++) {
//        		System.out.print(rowData.get(i) + " ");
//        	}
//        	System.out.println();
//        }
    	
    	Menu.runMenu();
    }
}