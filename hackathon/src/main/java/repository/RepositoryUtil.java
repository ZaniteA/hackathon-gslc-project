package repository;

public class RepositoryUtil {
    // Utility functions used in TeamRepository and UserRepository.

    // Displays an exception.
    // This is just to shorten the validation code.
    public static void displayException(String message) {
        try {
            throw new Exception(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Table = 1: user
    // Table = 2: team
    // Validates parameters passed to `find` or `findOne`, as outlined in the specifications.
    // Mostly handles parameters being null inconsistently.
    protected static Boolean validateFindParameters(int table, String field, String[] filter, Boolean join_table, String join_table_name) {
        if ((field != null) && (filter == null)) {
            displayException("Field to use filter not specified");
            return false;
        }
        if ((field == null) && (filter != null)) {
            displayException("Filter to use on field not specified");
            return false;
        }
        if (join_table != null) {
            if (join_table_name == null) {
                displayException("Table to join with not specified");
                return false;
            }
            if (join_table) {
                if ((table == 1 && join_table_name.toLowerCase() != "user") || (table == 2 && join_table_name.toLowerCase() != "team")) {
                    displayException("Invalid table to join with");
                    return false;
                }
            }
        }
        if ((join_table == null) && (join_table_name != null)) {
            displayException("Whether or not to join not specified");
            return false;
        }
        
        if (field.equals("")) {
            displayException("Field must not be empty");
            return false;
        }
        if ((filter != null) && (filter.length != 2)) {
            for (String s : filter) {
                System.out.println(s);
            }

            displayException("Invalid filter format");
            return false;
        }

        return true;
    }
}
