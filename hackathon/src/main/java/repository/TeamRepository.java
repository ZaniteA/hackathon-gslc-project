package repository;

import java.util.ArrayList;
import java.util.Arrays;

import connection.Connection;
import model.User;
import model.Team;

public class TeamRepository implements Repository {
    // Implements a team repository.

    // Returns true if the specified team name already exists in the database.
    private Boolean existingTeamName(String team_name, Connection connection) {
        // Fetch team data from CSV file
        ArrayList<ArrayList<String>> team_data = connection.readCsv(connection.teamsFile);

        for (ArrayList<String> row : team_data) {
            Team current_team = new Team(row);
            if (current_team.checkCondition("=", "Nama", team_name)) {
                // Found team with specified team name
                return true;
            }
        }
        return false;
    }

    // Gets the maximum team ID that exists in the database.
    // Used to auto-increment the team IDs.
    private Integer getMaximumTeamID(Connection connection) {
        // Fetch team data from CSV file
        ArrayList<ArrayList<String>> team_data = connection.readCsv(connection.teamsFile);

        Integer maximum_id = 0;
        for (ArrayList<String> row : team_data) {
            Team current_team = new Team(row);
            Integer current_id = Integer.parseInt(current_team.fetchField("id"));
            if (current_id > maximum_id) {
                maximum_id = current_id;
            }
        }
        return maximum_id;
    }

    // Finds at most `bound` teams from the database that specifies the conditions:
    // - `field` fulfills the conditions in `filter`, if both are not null.
    // - the table is first joined with `join_table_name`, if both are not null and `join_table` is true.
    // If `bound` is null, it is considered to be infinite (unbounded).
    // The teams will be returned in an ArrayList.
    // Returns null if the parameters are invalid.
    private ArrayList<Team> findBounded(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection, Integer bound) {
        // Validate parameters
        if (!RepositoryUtil.validateFindParameters(2, field, filter, join_table, join_table_name)) {
            return null;
        }

        // Get list of Teams to compare with
        ArrayList<Team> team_list = new ArrayList<Team>();
        if (join_table) {
            // Join the tables if needed
            ArrayList<ArrayList<String>> users = connection.readCsv(connection.userFile);
            ArrayList<ArrayList<String>> teams = connection.readCsv(connection.teamsFile);

            // Compute the cross product of both tables and check if the join condition is fulfilled
            for (ArrayList<String> t : teams) {
                for (ArrayList<String> u : users) {
                    Team nt = new Team(t);
                    User nu = new User(u);
                    if (nu.fetchField("ID Team") == nt.fetchField("id")) {
                        Team joined_data = nt;
                        for (int i = 0; i < nu.fields.size(); i++) {
                            joined_data.fields.add(nu.fields.get(i));
                            joined_data.values.add(nu.values.get(i));
                        }
                        team_list.add(joined_data);
                    }
                }
            }

        } else {
            // Fetch data from CSV file
            ArrayList<ArrayList<String>> teams = connection.readCsv(connection.teamsFile);

            for (ArrayList<String> t : teams) {
                team_list.add(new Team(t));
            }
        }

        // Filter the results based on the filter condition
        ArrayList<Team> condition_fulfilled = new ArrayList<Team>();
        for (Team current_team : team_list) {
            Boolean add = current_team.checkCondition(filter[0], field, filter[1]);
            if (add == null) {
                RepositoryUtil.displayException("Invalid filter format");
                return null;
            }
            if (add) {
                condition_fulfilled.add(current_team);
                // If the size bound has been reached, return immediately
                if ((bound != null) && (condition_fulfilled.size() == bound)) {
                    return condition_fulfilled;
                }
            }
        }

        // If the size was unbounded, or the bound has not been reached
        return condition_fulfilled;
    }

    // Returns an ArrayList of Teams fulfilling the conditions:
    // - `field` fulfills the conditions in `filter`, if both are not null.
    // - the table is first joined with `join_table_name`, if both are not null and `join_table` is true.
    // If no Teams fulfilled the condition, returns null.
    public ArrayList<Team> find(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        ArrayList<Team> valid_teams = findBounded(field, filter, join_table, join_table_name, connection, null);

        if (valid_teams.size() == 0) {
            return null;
        }
        return valid_teams;
    }

    // Returns one Team fulfilling the conditions:
    // - `field` fulfills the conditions in `filter`, if both are not null.
    // - the table is first joined with `join_table_name`, if both are not null and `join_table` is true.
    // If no Teams fulfilled the condition, returns null.
    public Team findOne(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        ArrayList<Team> valid_team = findBounded(field, filter, join_table, join_table_name, connection, 1);

        if (valid_team.size() == 0) {
            return null;
        }
        return valid_team.get(0);
    }

    // Inserts the specified fields into the database as a Team.
    // Returns the new Team, or null if the specified data was invalid.
    public Team insert(ArrayList<String> fields, Connection connection) {
        // Validate fields

        // Number of fields (except ID)
        if (fields.size() != Team.team_fields.size() - 1) {
            RepositoryUtil.displayException("Invalid fields format");
            return null;
        }

        // Team name must be unique
        String team_name = fields.get(0);
        if (existingTeamName(team_name, connection)) {
            RepositoryUtil.displayException("Team with name " + team_name + " already exists");
            return null;
        }

        // Get auto-incremented ID and construct the Team object
        Integer team_id = getMaximumTeamID(connection) + 1;
        ArrayList<String> new_team_values = new ArrayList<String>(Arrays.asList(team_id.toString(), team_name));
        Team current_team = new Team(new_team_values);

        // Write the Team object to the CSV file and return the object
        connection.writeCsv(connection.teamsFile, current_team.values);
        return current_team;
    }

    // Deletes a Team with the specified team name from the database.
    // Returns true if the deletion was successful, and false otherwise.
    public Boolean delete(String team_name, Connection connection) {
        // Validation

        // Team name must not be null
        if (team_name == null) {
            RepositoryUtil.displayException("Team name to delete not specified");
            return false;
        }

        // Team to be deleted must exist
        String[] condition = {"=", team_name};
        Team to_delete = findOne("Nama", condition, null, null, connection);
        if (to_delete == null) {
            RepositoryUtil.displayException("Team with name " + team_name + " not found");
            return false;
        }

        // Rewrite the CSV file, excluding the Team that is to be deleted
        ArrayList<ArrayList<String>> teams = connection.readCsv(connection.teamsFile);
        connection.clearCsv(connection.teamsFile);
        for (ArrayList<String> t : teams) {
            Team current_team = new Team(t);
            if (current_team.fetchField("Nama") == team_name) {
                // Exclude the deleted Team
                continue;
            }
            connection.writeCsv(connection.teamsFile, t);
        }

        return true;
    }

}
