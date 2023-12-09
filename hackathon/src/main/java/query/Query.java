package query;

import java.util.ArrayList;

import connection.Connection;
import repository.UserRepository;
import repository.TeamRepository;

public class Query {
    // Facade class for Repository
	
    Connection conn = Connection.getInstance();
    UserRepository UR = new UserRepository();
    TeamRepository TR = new TeamRepository();

    // table = 1, user
    // table = 2, team
    // will return true if the insert successfull
    boolean insert(int table, ArrayList<String> fields) {
        if (table == 1) {
            if (UR.insert(fields, conn) != null) return true;
            return false;
        } else if (table == 2) {
            if (TR.insert(fields, conn) != null) return true;
            return false;
        } else {
            return false;
        }
        return true;
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
