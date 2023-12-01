package repository;

public class RepositoryUtil {
    protected static void displayException(String message) {
        try {
            throw new Exception(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    protected static Boolean validateFindParameters(String field, String[] filter, Boolean join_table, String join_table_name) {
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
            if (join_table_name.toLowerCase() != "team") {
                displayException("Invalid table to join with");
                return false;
            }
        }
        if ((join_table == null) && (join_table_name != null)) {
            displayException("Whether or not to join not specified");
            return false;
        }
        
        if (field == "") {
            displayException("Field must not be empty");
            return false;
        }
        if (filter.length != 2) {
            displayException("Invalid filter format");
            return false;
        }

        return true;
    }
}
