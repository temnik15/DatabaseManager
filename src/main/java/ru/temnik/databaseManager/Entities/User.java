package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.temnik.databaseManager.Annotation.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="users")
public class User {

    private int id;
    private String login;
    private String password;

    public User(String login, String password) {
       this.login=login;
       this.password=password;
    }

}
