package model;

import java.util.ArrayList;

public class Model {
    ArrayList<String> fields, values;

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
        // Note that all possible exceptions on the values of operator, field, and value
        // needs to be handled by the Repository.

        // No operator to check
        if (operator == null) {
            return true;
        }

        // Get the value to compare with, based on field
        String compare_value = null;
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i) == field) {
                compare_value = values.get(i);
            }
        }

        // Perform operator check
        if (operator == "=") {
            return (value == compare_value);

        } else if (operator == "!=") {
            return (value != compare_value);

        } else {
            // Invalid operator
            return false;
        }
    }
}
