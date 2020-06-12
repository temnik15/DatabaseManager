package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.Entities.DocumentStatus;
import ru.temnik.databaseManager.Entities.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJdbcImpl implements UserDAO {
    private Connection connection;
    private final String SQL_SELECT_BY_LOGIN =
            "SELECT login,password FROM users WHERE login = ?";
    private final String SQL_FIND_ALL = "SELECT * FROM users";

    private final String SQL_FIND_BY_PARAMETRS = "SELECT * FROM users " +
            "WHERE id = ? AND login LIKE ? AND password LIKE ?";

    public UserDaoJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByLogin(String login) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("login"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        try {
            List<User> users = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL);
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(User obj) {

    }

    @Override
    public void delete(User obj) {

    }

    @Override
    public void update(User obj) {

    }

    @Override
    public List<User> findByParameters(User obj) {
        try {
            List<User> users = new ArrayList<>();
            PreparedStatement statement = null;
            if(obj.getId()!=0 && !obj.getLogin().equals("") && !obj.getPassword().equals("")){
                statement = connection.prepareStatement(SQL_FIND_BY_PARAMETRS);
                statement.setInt(1,obj.getId());
                statement.setString(2,obj.getLogin()+'%');
                statement.setString(3,obj.getPassword()+'%');
            }

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
