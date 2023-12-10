package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Team extends Model {
    // Implements the Team object.

    // Constants
    public static final ArrayList<String> team_fields = new ArrayList<String>(Arrays.asList("id", "Nama"));
    public static final Integer max_members = 3;

    // Constructor, calls the parent constructor with default fields.
    public Team(ArrayList<String> values) {
        super(team_fields, values);
    }
}
