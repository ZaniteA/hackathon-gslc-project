package model;

import java.util.ArrayList;

public class Model {
    public ArrayList<String> fields;
    public ArrayList<String> values;

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

    public Boolean checkCondition(String operator, String field, String value) {
        // Checks condition on a model.

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
        if (operator == "=") {
            return (value == compare_value);

        } else if (operator == "!=") {
            return (value != compare_value);

        } else {
            // Invalid operator
            return null;
        }
    }

    public String fetchField(String field) {
        // Fetches the value of a field.
        // If the field does not exist, returns null.

        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i) == field) {
                return values.get(i);
            }
        }

        return null;
    }
}
