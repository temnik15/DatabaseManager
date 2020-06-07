package ru.temnik.databaseManager;

import lombok.Getter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.temnik.databaseManager.DAO.UserDAO;
import ru.temnik.databaseManager.Entities.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class App {
    private final ControllerGUI controllerGUI;
    @Getter
    private final DriverManagerDataSource dataSource;


    public App(ControllerGUI controllerGUI) throws SQLException {
        this.controllerGUI = controllerGUI;
        dataSource = new DriverManagerDataSource();
        initDB();
    }

    private void initDB() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/java/ru/temnik/databaseManager/Config/db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dbUrl = properties.getProperty("db.url");
        String dbUsername = properties.getProperty("db.username");
        String dbPassword = properties.getProperty("db.password");
        String driverClassName = properties.getProperty("db.driverClassName");

        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setUrl(dbUrl);
        dataSource.setDriverClassName(driverClassName);
    }

    public boolean authorization(UserDAO userDAO, User user) {
        User userFromDB = userDAO.findByLogin(user.getLogin());
        if (userFromDB != null) {
            if (userFromDB.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
