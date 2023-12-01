package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Team extends Model {
    public static final ArrayList<String> team_fields = new ArrayList<String>(Arrays.asList("id", "Nama"));
    public static final Integer max_members = 3;

    public Team(ArrayList<String> values) {
        super(team_fields, values);
    }
}
