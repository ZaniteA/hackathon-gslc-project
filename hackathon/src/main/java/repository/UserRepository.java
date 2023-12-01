package repository;

import java.util.ArrayList;

import connection.Connection;
import model.User;
import model.Team;

public class UserRepository implements Repository {
    public ArrayList<User> find(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        return new ArrayList<User>();
    }

    public User findOne(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        return new User(new ArrayList<String>());
    }

    private String getTeamNameFromID(String team_id, Connection connection) {
        // Returns the team name with id id, or null if it does not exists
        ArrayList<ArrayList<String>> teamData = connection.readCsv(connection.teamsFile);
        for (ArrayList<String> row : teamData) {
            Team current_team = new Team(row);
            
            // Check if the team ID is equal to the one that we want to count
            if (current_team.checkCondition("=", "id", team_id)) {
                return current_team.fetchField("Nama");
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

    public void insert(ArrayList<String> fields, Connection connection) {
        User current_user = new User(fields);

        String user_NIM = current_user.fetchField("NIM");
        String user_team = current_user.fetchField("ID Team");
        String user_team_name = getTeamNameFromID("ID Team", connection);

        if (user_team_name == null) {
            try {
                throw new Exception("Team with name " + user_team_name + " does not exist");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }
        
        if (existingNIM(user_NIM, connection)) {
            try {
                throw new Exception("User with NIM " + user_NIM + " already exists");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }
        
        if (existingTeamMemberCount(user_team, connection) >= 3) {
            try {
                throw new Exception("Team " + user_team_name + " is already full!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }

        connection.writeCsv(connection.userFile, current_user.getValuesArray());
    }
}
