package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.Entities.Act;
import ru.temnik.databaseManager.Entities.DocumentStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActsDAOJdbsImpl implements CrudDAO<Act> {
    private Connection connection;
    private final String SQL_FIND_ALL = "SELECT * FROM acts_admission";
    private final String SQL_SAVE = "INSERT INTO acts_admission(id_acts,id_document, id_employee,id_reader,date) " +
            " VALUES (?,?,?,?,?)";
    private final String SQL_DELETE = "DELETE FROM acts_admission WHERE id_acts = ?";
    private final String SQL_UPDATE = "UPDATE acts_admission SET status_name = ? " +
            "WHERE id_status = ?, id_document=?, id_employee=?, id_reader=?, date=?";

    public ActsDAOJdbsImpl(DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Act> findAll() {
        try {
            List<Act> acts = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL);
            while (resultSet.next()) {
                Act act = new Act(
                        resultSet.getInt("id_acts"),
                        resultSet.getInt("id_document"),
                        resultSet.getInt("id_employee"),
                        resultSet.getInt("id_reader"),
                        resultSet.getString("date")
                );
                acts.add(act);
            }
            return acts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Act obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE);
            statement.setInt(1, obj.getId_acts());
            statement.setInt(2, obj.getId_document());
            statement.setInt(3, obj.getId_employee());
            statement.setInt(4, obj.getId_reader());
            statement.setString(5, obj.getDate());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Act obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setInt(1, obj.getId_acts());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Act obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setInt(1, obj.getId_acts());
            statement.setInt(2, obj.getId_document());
            statement.setInt(3, obj.getId_employee());
            statement.setInt(4, obj.getId_reader());
            statement.setString(5, obj.getDate());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Act> findByParameters(Act obj) {
        return null;
    }
}
