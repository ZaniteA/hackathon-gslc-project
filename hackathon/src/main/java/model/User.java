package model;

import java.util.ArrayList;
import java.util.Arrays;

public class User extends Model {
    static final ArrayList<String> user_fields = new ArrayList<String>(Arrays.asList("NIM", "Name", "ID Team"));

    public User(ArrayList<String> values) {
        super(user_fields, values);
    }
}
