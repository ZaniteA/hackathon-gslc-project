package repository;

import java.util.ArrayList;

import connection.Connection;
import model.Model;

public interface Repository {
    public ArrayList<? extends Model> find(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection);
    public Model findOne(String field, String[] filter, Boolean join_table, String join_table_name, Connection connection);
    public void insert(ArrayList<String> fields, Connection connection);
}
