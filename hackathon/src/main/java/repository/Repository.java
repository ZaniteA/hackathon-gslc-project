package repository;

import java.util.ArrayList;

import connection.Connection;
import model.Model;

public interface Repository {
    // Interface for TeamRepository and UserRepository.

    // Returns an ArrayList of Teams or Users fulfilling the conditions:
    // - `field` fulfills the conditions in `filter`, if both are not null.
    // - the table is first joined with `join_table_name`, if both are not null and `join_table` is true.
    public ArrayList<? extends Model> find(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection);

    // Returns one Team or User fulfilling the conditions:
    // - `field` fulfills the conditions in `filter`, if both are not null.
    // - the table is first joined with `join_table_name`, if both are not null and `join_table` is true.
    public Model findOne(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection);

    // Inserts the specified fields into the database.
    // Returns the new object.
    public Model insert(ArrayList<String> fields, Connection connection);
}
