package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.EntitiesGUI.DocumentStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentStatusJdbcImpl implements DocumentStatusDAO {
    private Connection connection;
    private  final String SQL_FIND_ALL = "SELECT * FROM document_statuses";
    private final String SQL_SAVE = "INSERT INTO document_statuses(status_name) VALUES (?)";



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
            statement.setString(1,obj.getStatusName());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
