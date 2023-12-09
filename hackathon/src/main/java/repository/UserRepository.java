package repository;

import java.util.ArrayList;
import java.util.Arrays;

import connection.Connection;
import model.User;
import model.Team;

public class UserRepository implements Repository {
    private String getTeamIDFromName(String team_name, Connection connection) {
        // Returns the team name with id id, or null if it does not exists
        ArrayList<ArrayList<String>> teamData = connection.readCsv(connection.teamsFile);
        for (ArrayList<String> row : teamData) {
            Team current_team = new Team(row);
            
            // Check if the team ID is equal to the one that we want to count
            if (current_team.checkCondition("=", "Nama", team_name)) {
                return current_team.fetchField("id");
            }
        }

        return null;
    }

    private Boolean existingNIM(String nim, Connection connection) {
        ArrayList<ArrayList<String>> userData = connection.readCsv(connection.userFile);
        for (ArrayList<String> row : userData) {
            User current_user = new User(row);
            if (current_user.checkCondition("=", "NIM", nim)) {
                return true;
            }
        }
        return false;
    }

    private Integer existingTeamMemberCount(String team_id, Connection connection) {
        Integer member_count = 0;
        ArrayList<ArrayList<String>> teamData = connection.readCsv(connection.teamsFile);
        for (ArrayList<String> row : teamData) {
            Team current_team = new Team(row);
            
            // Check if the team ID is equal to the one that we want to count
            if (current_team.checkCondition("=", "id", team_id)) {
                member_count++;
            }
        }
        return member_count;
    }

    private ArrayList<User> findBounded(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection, Integer bound) {
        // Validation
        if (!RepositoryUtil.validateFindParameters(field, filter, join_table, join_table_name)) {
            return null;
        }

        // Get list of Users to compare with
        ArrayList<User> user_list = new ArrayList<User>();
        if (join_table) {
            ArrayList<ArrayList<String>> users = connection.readCsv(connection.userFile);
            ArrayList<ArrayList<String>> teams = connection.readCsv(connection.teamsFile);

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
            ArrayList<ArrayList<String>> users = connection.readCsv(connection.userFile);

            for (ArrayList<String> u : users) {
                user_list.add(new User(u));
            }
        }

        ArrayList<User> condition_fulfilled = new ArrayList<User>();;
        for (User current_user : user_list) {
            Boolean add = current_user.checkCondition(filter[0], field, filter[1]);
            if (add == null) {
                RepositoryUtil.displayException("Invalid filter format");
                return null;
            }
            if (add) {
                condition_fulfilled.add(current_user);
                if ((bound != null) && (condition_fulfilled.size() == bound)) {
                    return condition_fulfilled;
                }
            }
        }

        return condition_fulfilled;
    }

    public ArrayList<User> find(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        ArrayList<User> valid_users = findBounded(field, filter, join_table, join_table_name, connection, null);

        if (valid_users.size() == 0) {
            return null;
        }
        return valid_users;
    }

    public User findOne(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        ArrayList<User> valid_user = findBounded(field, filter, join_table, join_table_name, connection, 1);

        if (valid_user.size() == 0) {
            return null;
        }
        return valid_user.get(0);
    }

    public static User insert(ArrayList<String> fields, Connection connection) {
        if (fields.size() != User.user_fields.size()) {
            RepositoryUtil.displayException("Invalid fields format");
            return null;
        }

        User current_user = new User(fields);

        String user_NIM = current_user.fetchField("NIM");
        String user_team_name = current_user.fetchField("ID Team"); // Temporarily store in field ID Team
        String user_team_id = getTeamIDFromName("ID Team", connection);

        // Validation
        if (existingNIM(user_NIM, connection)) {
            RepositoryUtil.displayException("User with NIM " + user_NIM + " already exists");
            return null;
        }
        if (user_team_id == null) {
            TeamRepository team_repo = new TeamRepository();
            Team new_team = team_repo.insert(new ArrayList<String>(Arrays.asList(user_team_name)), connection);
            user_team_id = new_team.fetchField("id");
        }

        if (existingTeamMemberCount(user_team_id, connection) >= Team.max_members) {
            RepositoryUtil.displayException("Team " + user_team_name + " is already full!");
            return null;
        }

        connection.writeCsv(connection.userFile, current_user.values);
        return current_user;
    }

    public void delete(String nim, Connection connection) {
        if (nim == null) {
            RepositoryUtil.displayException("NIM to delete not specified");
            return;
        }

        String[] condition = {"=", nim};
        User to_delete = findOne("nim", condition, null, null, connection);
        if (to_delete == null) {
            RepositoryUtil.displayException("User with NIM " + nim + " not found");
            return;
        }

        ArrayList<ArrayList<String>> users = connection.readCsv(connection.userFile);
        connection.clearCsv(connection.userFile);
        for (ArrayList<String> u : users) {
            User current_user = new User(u);
            if (current_user.fetchField("NIM") == nim) {
                continue;
            }
            connection.writeCsv(connection.userFile, u);
        }
    }
}
