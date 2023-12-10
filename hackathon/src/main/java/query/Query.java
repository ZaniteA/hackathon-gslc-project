package query;

import java.util.ArrayList;

import connection.Connection;
import repository.UserRepository;
import repository.TeamRepository;

public class Query {
    // Facade class for Repository.
    // All user queries are handled through the Query class.
	
    Connection conn = Connection.getInstance();
    UserRepository UR = new UserRepository();
    TeamRepository TR = new TeamRepository();

    // Table = 1: user
    // Table = 2: team
    // Inserts data into the specified table.
    // Returns true if the insert was successful, and false otherwise.
    Boolean insert(int table, ArrayList<String> fields) {
        if (table == 1) {
            if (UR.insert(fields, conn) != null) return true;
            return false;
        } else if (table == 2) {
            if (TR.insert(fields, conn) != null) return true;
            return false;
        } else {
            return false;
        }
    }

    /* soon...
    void show(int table, String condition) {
        if (table == 1) {
            if (condition == null) { // show all
                ;
            } else {
                ;
            }
        } else {
            if (condition == null) { // show all
                ;
            } else {
                ;
            }
        }
    }

    void delete(int table, String condition) {
        ;
    }
    */
}
