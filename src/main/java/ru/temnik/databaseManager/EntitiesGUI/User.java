package ru.temnik.databaseManager.EntitiesGUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import ru.temnik.databaseManager.Entities.Test;

@AllArgsConstructor
public class User  {
    private SimpleIntegerProperty id;
    private SimpleStringProperty login;
    private SimpleStringProperty password;

    public User(String login, String password) {
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
    }

    public User(int id,String login, String password) {
        this.id=new SimpleIntegerProperty(id);
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
    }

    public int getId() {
        return id.get();
    }
    public String getLogin() {
        return login.get();
    }
    public String getPassword() {
        return password.get();
    }
}
