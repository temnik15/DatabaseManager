package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.Entities.DocumentStatus;
import ru.temnik.databaseManager.Entities.Reader;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAOJdbcImpl implements CrudDAO<Reader> {
    private Connection connection;
    private  final String SQL_FIND_ALL = "SELECT * FROM readers";
    private  final String SQL_FIND_ALL_JOIN = "SELECT * FROM readers JOIN ";
    private final String SQL_SAVE = "INSERT INTO readers(last_name,first_name,patronymic,id_degree,phone)" +
            "  VALUES (?,?,?,?,?)";
    private final String SQL_DELETE = "DELETE FROM readers WHERE id_reader = ?";
    private final String SQL_UPDATE = "UPDATE readers  SET " +
            "last_name = ? , first_name=? ,patronymic=?, id_degree=?, phone=? WHERE id_reader = ?";

    public ReaderDAOJdbcImpl(DataSource dataSource){
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Reader> findAll() {
        try {
            List<Reader> readers = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL);
            while(resultSet.next()){
                Reader reader = new Reader(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getString(6)
                );
                readers.add(reader);
            }
            return readers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Reader obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE);
            statement.setString(1,obj.getLast_name());
            statement.setString(2,obj.getFirst_name());
            statement.setString(3,obj.getPatronymic());
            statement.setInt(4,obj.getId_degree());
            statement.setString(5,obj.getPhone());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Reader obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setInt(1,obj.getId_reader());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Reader obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1,obj.getLast_name());
            statement.setString(2,obj.getFirst_name());
            statement.setString(3,obj.getPatronymic());
            statement.setInt(4,obj.getId_degree());
            statement.setString(5,obj.getPhone());
            statement.setInt(6,obj.getId_reader());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reader> findByParameters(Reader obj) {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return Reader.class;
    }


    public List<Reader> findAllJoin() {
        try {
            List<Reader> readers = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_JOIN);
            while(resultSet.next()){
                Reader reader = new Reader(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getString(6)
                );
                readers.add(reader);
            }
            return readers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
