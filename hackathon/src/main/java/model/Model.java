package model;

import java.util.ArrayList;

public class Model {
    // Superclass of Team and User.
    // Acts as a general "row" in SQL-like database.
    // Works somewhat like a dictionary, but with fixed fields.

    public ArrayList<String> fields, values;

    // Constructor of a Model with particular `fields` and `values`.
    public Model(ArrayList<String> fields, ArrayList<String> values) {
        // Ensure that fields and values have the same size
        try {
            assert fields.size() == values.size() : "Fields and values have different sizes";
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }

        this.fields = fields;
        this.values = values;
    }

    // Checks whether or not the expression (`field` `operator` `value`) is true.
    // `operator` can be "=" or "!=".
    // Returns null if the operator or field is invalid.
    public Boolean checkCondition(String operator, String field, String value) {
        // No operator to check
        if (operator == null) {
            return true;
        }

        // Get the value to compare with, based on field
        String compare_value = fetchField(field);
        if (compare_value == null) {
            return null;
        }

        // Perform operator check
        if (operator.equals("=")) {
            return (value.equals(compare_value));
        } else if (operator.equals("!=")) {
            return (!value.equals(compare_value));
        } else {
            // Invalid operator
            return null;
        }
    }

    // Fetches the value of a field.
    // If the field does not exist, returns null.
    public String fetchField(String field) {
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).equals(field)) {
                return values.get(i);
            }
        }

        return null;
    }
}
