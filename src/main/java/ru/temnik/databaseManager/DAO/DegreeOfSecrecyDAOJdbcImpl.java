package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.Entities.Act;
import ru.temnik.databaseManager.Entities.DegreeOfSecrecy;
import ru.temnik.databaseManager.Entities.DocumentStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DegreeOfSecrecyDAOJdbcImpl implements CrudDAO<DegreeOfSecrecy> {
    private Connection connection;
    private final String SQL_FIND_ALL = "SELECT * FROM degrees_of_secrecy";
    private final String SQL_SAVE = "INSERT INTO degrees_of_secrecy(degree_name) VALUES (?)";
    private final String SQL_DELETE = "DELETE FROM degrees_of_secrecy WHERE id_degree= ?";
    private final String SQL_UPDATE = "UPDATE degrees_of_secrecy SET degree_name = ? WHERE id_degree = ?";
    private final String SQL_FIND_BY_PARAMETRS = "SELECT * FROM degrees_of_secrecy" +
            " WHERE  LOWER(degree_name) LIKE ? and id_degree = ?";
    private final String SQL_FIND_BY_PARAMETR_STATUS = "SELECT * FROM degrees_of_secrecy" +
            " WHERE  LOWER(degree_name) LIKE ?";
    private final String SQL_FIND_BY_PARAMETR_ID = "SELECT * FROM degrees_of_secrecy" +
            " WHERE  id_degree = ?";

    public DegreeOfSecrecyDAOJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DegreeOfSecrecy> findAll() {
        try {
            List<DegreeOfSecrecy> degrees = new ArrayList<>();
            degrees.add(new DegreeOfSecrecy());
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL);
            while (resultSet.next()) {
                DegreeOfSecrecy degreeOfSecrecy = new DegreeOfSecrecy(
                        resultSet.getInt("id_degree"),
                        resultSet.getString("degree_name"));
                degrees.add(degreeOfSecrecy);
            }
            return degrees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(DegreeOfSecrecy obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE);
            statement.setString(1,obj.getDegree_name());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(DegreeOfSecrecy obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setInt(1,obj.getId_degree());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DegreeOfSecrecy obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1,obj.getDegree_name());
            statement.setInt(2,obj.getId_degree());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DegreeOfSecrecy> findByParameters(DegreeOfSecrecy obj) {
        try {
            List<DegreeOfSecrecy> degrees = new ArrayList<>();
            PreparedStatement statement = null;
            if(obj.getId_degree()==0){
                statement= connection.prepareStatement(SQL_FIND_BY_PARAMETR_STATUS);
                statement.setString(1,obj.getDegree_name()+'%');
            }else if(obj.getDegree_name().equals("")){
                statement = connection.prepareStatement(SQL_FIND_BY_PARAMETR_ID);
                statement.setInt(1,obj.getId_degree());
            }else{
                statement = connection.prepareStatement(SQL_FIND_BY_PARAMETRS);
                statement.setString(1,obj.getDegree_name()+'%');
                statement.setInt(2,obj.getId_degree());
            }

            System.out.println(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                DegreeOfSecrecy degree = new DegreeOfSecrecy(
                        resultSet.getInt("id_degree"),
                        resultSet.getString("degree_name")
                );
                degrees.add(degree);
            }
            return degrees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
