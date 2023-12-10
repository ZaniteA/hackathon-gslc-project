package repository;

import java.util.ArrayList;
import java.util.Arrays;

import connection.Connection;
import model.User;
import model.Team;

public class UserRepository implements Repository {
    // Implements a user repository.

    // Returns the ID of the given team name, or null if it does not exist.
    private String getTeamIDFromName(String team_name, Connection connection) {
        // Fetch team data from CSV file
        ArrayList<ArrayList<String>> teamData = connection.readCsv(connection.teamsFile);
        for (ArrayList<String> row : teamData) {
            Team current_team = new Team(row);
            
            // Check if the team ID is equal to the one that we want to find
            if (current_team.checkCondition("=", "Nama", team_name)) {
                return current_team.fetchField("id");
            }
        }

        // Specified team does not exist
        return null;
    }

    // Returns true if the specified NIM already exists in the database.
    private Boolean existingNIM(String nim, Connection connection) {
        // Fetch user data from CSV file
        ArrayList<ArrayList<String>> userData = connection.readCsv(connection.userFile);

        for (ArrayList<String> row : userData) {
            User current_user = new User(row);

            // Check if the NIM is equal to the one that we want to find
            if (current_user.checkCondition("=", "NIM", nim)) {
                return true;
            }
        }
        return false;
    }

    // Returns the number of members of a team with the specified team ID.
    private Integer existingTeamMemberCount(String team_id, Connection connection) {
        // Fetch team data from CSV file
        ArrayList<ArrayList<String>> teamData = connection.readCsv(connection.teamsFile);

        Integer member_count = 0;
        for (ArrayList<String> row : teamData) {
            Team current_team = new Team(row);
            
            // Check if the team ID is equal to the one that we want to count
            if (current_team.checkCondition("=", "id", team_id)) {
                member_count++;
            }
        }
        return member_count;
    }

    // Finds at most `bound` users from the database that specifies the conditions:
    // - `field` fulfills the conditions in `filter`, if both are not null.
    // - the table is first joined with `join_table_name`, if both are not null and `join_table` is true.
    // If `bound` is null, it is considered to be infinite (unbounded).
    // The users will be returned in an ArrayList.
    // Returns null if the parameters are invalid.
    private ArrayList<User> findBounded(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection, Integer bound) {
        // Validate parameters
        if (!RepositoryUtil.validateFindParameters(1, field, filter, join_table, join_table_name)) {
            return null;
        }

        // Get list of Users to compare with
        ArrayList<User> user_list = new ArrayList<User>();
        if (join_table) {
            // Join the tables if needed
            ArrayList<ArrayList<String>> users = connection.readCsv(connection.userFile);
            ArrayList<ArrayList<String>> teams = connection.readCsv(connection.teamsFile);

            // Compute the cross product of both tables and check if the join condition is fulfilled
            for (ArrayList<String> u : users) {
                for (ArrayList<String> t : teams) {
                    User nu = new User(u);
                    Team nt = new Team(t);
                    if (nu.fetchField("ID Team") == nt.fetchField("id")) {
                        User joined_data = nu;
                        for (int i = 0; i < nt.fields.size(); i++) {
                            joined_data.fields.add(nt.fields.get(i));
                            joined_data.values.add(nt.values.get(i));
                        }
                        user_list.add(joined_data);
                    }
                }
            }

        } else {
            // Fetch data from CSV file
            ArrayList<ArrayList<String>> users = connection.readCsv(connection.userFile);

            for (ArrayList<String> u : users) {
                user_list.add(new User(u));
            }
        }

        // Filter the results based on the filter condition
        ArrayList<User> condition_fulfilled = new ArrayList<User>();
        for (User current_user : user_list) {
            Boolean add = current_user.checkCondition(filter[0], field, filter[1]);
            if (add == null) {
                RepositoryUtil.displayException("Invalid filter format");
                return null;
            }
            if (add) {
                condition_fulfilled.add(current_user);
                // If the size bound has been reached, return immediately
                if ((bound != null) && (condition_fulfilled.size() == bound)) {
                    return condition_fulfilled;
                }
            }
        }

        // If the size was unbounded, or the bound has not been reached
        return condition_fulfilled;
    }

    // Returns an ArrayList of Users fulfilling the conditions:
    // - `field` fulfills the conditions in `filter`, if both are not null.
    // - the table is first joined with `join_table_name`, if both are not null and `join_table` is true.
    // If no Users fulfilled the condition, returns null.
    public ArrayList<User> find(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        ArrayList<User> valid_users = findBounded(field, filter, join_table, join_table_name, connection, null);

        if (valid_users.size() == 0) {
            return null;
        }
        return valid_users;
    }

    // Returns one User fulfilling the conditions:
    // - `field` fulfills the conditions in `filter`, if both are not null.
    // - the table is first joined with `join_table_name`, if both are not null and `join_table` is true.
    // If no Users fulfilled the condition, returns null.
    public User findOne(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        ArrayList<User> valid_user = findBounded(field, filter, join_table, join_table_name, connection, 1);

        if (valid_user.size() == 0) {
            return null;
        }
        return valid_user.get(0);
    }

    // Inserts the specified fields into the database as a User.
    // Returns the new User, or null if the specified data was invalid.
    public User insert(ArrayList<String> fields, Connection connection) {
        // Validate fields

        // Number of fields
        if (fields.size() != User.user_fields.size()) {
            RepositoryUtil.displayException("Invalid fields format");
            return null;
        }

        // Create the User object (needed for further validation)
        User current_user = new User(fields);

        String user_NIM = current_user.fetchField("NIM");
        String user_team_name = current_user.fetchField("ID Team"); // Temporarily store in field ID Team
        String user_team_id = getTeamIDFromName(user_team_name, connection);

        // NIM must be unique
        if (existingNIM(user_NIM, connection)) {
            RepositoryUtil.displayException("User with NIM " + user_NIM + " already exists");
            return null;
        }
        
        // If user team does not exist yet, create the team
        if (user_team_id == null) {
            TeamRepository team_repo = new TeamRepository();
            Team new_team = team_repo.insert(new ArrayList<String>(Arrays.asList(user_team_name)), connection);
            user_team_id = new_team.fetchField("id");
        }

        // There must be at most `max_members` members in a Team
        if (existingTeamMemberCount(user_team_id, connection) >= Team.max_members) {
            RepositoryUtil.displayException("Team " + user_team_name + " is already full!");
            return null;
        }

        // Write the User object to the CSV file and return the object
        connection.writeCsv(connection.userFile, current_user.values);
        return current_user;
    }

    // Deletes a User with the specified NIM from the database.
    public void delete(String nim, Connection connection) {
        // Validation

        // NIM must not be null
        if (nim == null) {
            RepositoryUtil.displayException("NIM to delete not specified");
            return;
        }

        // User to be deleted must exist
        String[] condition = {"=", nim};
        User to_delete = findOne("nim", condition, null, null, connection);
        if (to_delete == null) {
            RepositoryUtil.displayException("User with NIM " + nim + " not found");
            return;
        }

        // Rewrite the CSV file, excluding the User that is to be deleted
        ArrayList<ArrayList<String>> users = connection.readCsv(connection.userFile);
        connection.clearCsv(connection.userFile);
        for (ArrayList<String> u : users) {
            User current_user = new User(u);
            if (current_user.fetchField("NIM") == nim) {
                // Exclude the deleted User
                continue;
            }
            connection.writeCsv(connection.userFile, u);
        }
    }
}
