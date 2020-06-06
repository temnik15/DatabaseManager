package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.Entities.DegreeOfSecrecy;
import ru.temnik.databaseManager.Entities.DocumentStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentStatusJdbcImpl implements DocumentStatusDAO {
    private Connection connection;
    private  final String SQL_FIND_ALL = "SELECT * FROM document_statuses";
    private final String SQL_SAVE = "INSERT INTO document_statuses(status_name) VALUES (?)";
    private final String SQL_DELETE = "DELETE FROM document_statuses WHERE id_status = ?";
    private final String SQL_UPDATE = "UPDATE document_statuses SET status_name = ? WHERE id_status = ?";
    private final String SQL_FIND_BY_PARAMETRS = "SELECT * FROM document_statuses" +
            " WHERE  LOWER(status_name) LIKE ? and id_status = ?";
    private final String SQL_FIND_BY_PARAMETR_STATUS = "SELECT * FROM document_statuses" +
            " WHERE  LOWER(status_name) LIKE ?";
    private final String SQL_FIND_BY_PARAMETR_ID = "SELECT * FROM document_statuses" +
            " WHERE  id_status = ?";
    private final Class entityClass = DocumentStatus.class;




    public DocumentStatusJdbcImpl(DataSource dataSource){
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<DocumentStatus> findAll() {
        try {
            List<DocumentStatus> statuses = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL);
            while(resultSet.next()){
                DocumentStatus status = new DocumentStatus(
                        resultSet.getInt("id_status"),
                        resultSet.getString("status_name"));
                statuses.add(status);
            }
            return statuses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(DocumentStatus obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE);
            statement.setString(1,obj.getStatus_Name());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(DocumentStatus obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setInt(1,obj.getId_status());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DocumentStatus obj) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1,obj.getStatus_Name());
            statement.setInt(2,obj.getId_status());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DocumentStatus> findByParameters(DocumentStatus obj) {
        try {
            List<DocumentStatus> statuses = new ArrayList<>();
            PreparedStatement statement = null;
            if(obj.getId_status()==0){
                statement= connection.prepareStatement(SQL_FIND_BY_PARAMETR_STATUS);
                statement.setString(1,obj.getStatus_Name()+'%');
            }else if(obj.getStatus_Name().equals("")){
                statement = connection.prepareStatement(SQL_FIND_BY_PARAMETR_ID);
                statement.setInt(1,obj.getId_status());
            }else{
                statement = connection.prepareStatement(SQL_FIND_BY_PARAMETRS);
                statement.setString(1,obj.getStatus_Name()+'%');
                statement.setInt(2,obj.getId_status());
            }

            System.out.println(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                DocumentStatus status = new DocumentStatus(
                        resultSet.getInt("id_status"),
                        resultSet.getString("status_name"));
                statuses.add(status);
            }
            return statuses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }
}
