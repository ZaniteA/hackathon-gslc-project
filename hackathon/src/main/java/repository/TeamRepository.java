package repository;

import java.util.ArrayList;
import java.util.Arrays;

import connection.Connection;
import model.User;
import model.Team;

public class TeamRepository implements Repository {
    private Boolean existingTeamName(String team_name, Connection connection) {
        ArrayList<ArrayList<String>> team_data = connection.readCsv(connection.userFile);
        for (ArrayList<String> row : team_data) {
            Team current_team = new Team(row);
            if (current_team.checkCondition("=", "Nama", team_name)) {
                return true;
            }
        }
        return false;
    }

    private Integer getMaximumTeamID(Connection connection) {
        ArrayList<ArrayList<String>> team_data = connection.readCsv(connection.userFile);
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

    private ArrayList<Team> findBounded(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection, Integer bound) {
        // Validation
        if (!RepositoryUtil.validateFindParameters(field, filter, join_table, join_table_name)) {
            return null;
        }

        // Get list of Teams to compare with
        ArrayList<Team> team_list = new ArrayList<Team>();
        if (join_table) {
            ArrayList<ArrayList<String>> users = connection.readCsv(connection.userFile);
            ArrayList<ArrayList<String>> teams = connection.readCsv(connection.teamsFile);

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
            ArrayList<ArrayList<String>> teams = connection.readCsv(connection.teamsFile);

            for (ArrayList<String> t : teams) {
                team_list.add(new Team(t));
            }
        }

        ArrayList<Team> condition_fulfilled = new ArrayList<Team>();;
        for (Team current_team : team_list) {
            Boolean add = current_team.checkCondition(filter[0], field, filter[1]);
            if (add == null) {
                RepositoryUtil.displayException("Invalid filter format");
                return null;
            }
            if (add) {
                condition_fulfilled.add(current_team);
                if ((bound != null) && (condition_fulfilled.size() == bound)) {
                    return condition_fulfilled;
                }
            }
        }

        return condition_fulfilled;
    }

    public ArrayList<Team> find(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        ArrayList<Team> valid_teams = findBounded(field, filter, join_table, join_table_name, connection, null);

        if (valid_teams.size() == 0) {
            return null;
        }
        return valid_teams;
    }

    public Team findOne(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection) {
        ArrayList<Team> valid_team = findBounded(field, filter, join_table, join_table_name, connection, 1);

        if (valid_team.size() == 0) {
            return null;
        }
        return valid_team.get(0);
    }

    public static Team insert(ArrayList<String> fields, Connection connection) {
        if (fields.size() != User.user_fields.size() - 1) {
            RepositoryUtil.displayException("Invalid fields format");
            return null;
        }

        String team_name = fields.get(0);

        // Validation
        if (existingTeamName(team_name, connection)) {
            RepositoryUtil.displayException("Team with name " + team_name + " already exists");
            return null;
        }

        Integer team_id = getMaximumTeamID(connection) + 1;
        ArrayList<String> new_team_values = new ArrayList<String>(Arrays.asList(team_id.toString(), team_name));
        Team current_team = new Team(new_team_values);

        connection.writeCsv(connection.userFile, current_team.values);
        return current_team;
    }

    public void delete(String team_name, Connection connection) {
        if (team_name == null) {
            RepositoryUtil.displayException("Team name to delete not specified");
            return;
        }

        String[] condition = {"=", team_name};
        Team to_delete = findOne("Nama", condition, null, null, connection);
        if (to_delete == null) {
            RepositoryUtil.displayException("Team with name " + team_name + " not found");
            return;
        }

        ArrayList<ArrayList<String>> teams = connection.readCsv(connection.teamsFile);
        connection.clearCsv(connection.teamsFile);
        for (ArrayList<String> t : teams) {
            Team current_team = new Team(t);
            if (current_team.fetchField("Nama") == team_name) {
                continue;
            }
            connection.writeCsv(connection.teamsFile, t);
        }
    }
}
