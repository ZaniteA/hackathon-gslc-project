package query;

import java.util.ArrayList;

import connection.Connection;
import model.Team;
import model.User;
import repository.UserRepository;
import repository.Repository;
import repository.RepositoryUtil;
import repository.TeamRepository;

public class Query {
    // Facade class for Repository.
    // All user queries are handled through the Query class.

    Connection conn = Connection.getInstance();
    UserRepository UR = new UserRepository();
    TeamRepository TR = new TeamRepository();

    public Query() {

    }

    // Table = 1: user
    // Table = 2: team
    // Inserts data into the specified table.
    // Returns true if the insert was successful, and false otherwise.
    public Boolean insert(int table, ArrayList<String> fields) {
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

    // Shows data from the specified table.
    // Returns true if the show was successful, and false otherwise.
    public Boolean show(int table, String condition) {
        // Create and validate condition
        String field = "";
        String[] filter = {"", ""};
        String join_table_name = "";

        if (condition != null) {
            // Parse condition into filter variables
            String[] raw_filter = condition.split(";");

            if (raw_filter.length == 3) {
                field = raw_filter[0];
                filter[0] = raw_filter[1];
                filter[1] = raw_filter[2];
                
            } else if (raw_filter.length == 5) {
                if (raw_filter[3] != "join") {
                    RepositoryUtil.displayException("Invalid filter format");
                    return false;    
                }

                field = raw_filter[0];
                filter[0] = raw_filter[1];
                filter[1] = raw_filter[2];
                join_table_name = raw_filter[4];

            } else {
                RepositoryUtil.displayException("Invalid filter format");
                return false;
            }
        }

        if (table == 1) {
            ArrayList<User> to_show;

            if (condition == null) { // show all
                to_show = UR.find(null, null, null, null, conn);
            } else {
                to_show = UR.find(field, filter, true, join_table_name, conn);
            }

            if (to_show == null) {
                System.out.println("There is no data to show!");
                return true;
            }

            for (User u : to_show) {
                for (int i = 0; i < u.fields.size(); i++) {
                    System.out.println(u.fields.get(i) + ": " + u.values.get(i));
                }
                System.out.println("");
            }

        } else {
            ArrayList<Team> to_show;

            if (condition == null) { // show all
                to_show = TR.find(null, null, null, null, conn);
            } else {
                to_show = TR.find(field, filter, true, join_table_name, conn);
            }

            if (to_show == null) {
                System.out.println("There is no data to show!");
                return true;
            }

            for (Team t : to_show) {
                for (int i = 0; i < t.fields.size(); i++) {
                    System.out.println(t.fields.get(i) + ": " + t.values.get(i));
                }
                System.out.println("");
            }
        }

        return true;
    }
    
    // Deletes data from the specified table.
    // Returns true if the deletion was successful, and false otherwise.
    public Boolean delete(int table, String condition) {
        if (table == 1) {
            if (UR.delete(condition, conn)) return true;
            return false;
        } else if (table == 2) {
            if (TR.delete(condition, conn)) return true;
            return false;
        } else {
            return false;
        }
    }
}
