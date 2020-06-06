package ru.temnik.databaseManager.DAO;

import lombok.Getter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Getter
public class TableDaoJdbcImpl implements TableDAO{
    private Connection connection;

    private final String SQL_FIND_ALL_TABLE_NAMES =
            "SELECT table_name FROM information_schema.tables  " +
                    "WHERE table_schema='public'  and table_name !='users' ORDER BY table_name";
    private final String SQL_FIND_TABLE_NAMES_BY_NAME =
            "SELECT table_name FROM information_schema.tables  " +
                    "WHERE table_schema='public' and table_name LIKE ? and table_name !='users' ORDER BY table_name";

    public TableDaoJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    @Override
    public List<String> findAll() {
        List<String> tableNames = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_TABLE_NAMES);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    @Override
    public void save(String obj) {

    }

    @Override
    public void delete(String obj) {

    }

    @Override
    public void update(String obj) {

    }

    @Override
    public List<String> findByParameters(String obj) {
        return null;
    }


    @Override
    public List<String> findTableNamesByName(String name) {
        name=name.toLowerCase().concat("%");
        List<String> tableNames = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_TABLE_NAMES_BY_NAME);
            statement.setString(1,name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }




}
