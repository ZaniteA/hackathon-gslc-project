package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Team extends Model {
    static final ArrayList<String> team_fields = new ArrayList<String>(Arrays.asList("id", "Nama"));

    public Team(ArrayList<String> values) {
        super(team_fields, values);
    }
}
