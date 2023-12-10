package model;

import java.util.ArrayList;
import java.util.Arrays;

public class User extends Model {
    // Implements the User object.

    // Constants
    public static final ArrayList<String> user_fields = new ArrayList<String>(Arrays.asList("NIM", "Name", "ID Team"));

    // Constructor, calls the parent constructor with default fields.
    public User(ArrayList<String> values) {
        super(user_fields, values);
    }
}
