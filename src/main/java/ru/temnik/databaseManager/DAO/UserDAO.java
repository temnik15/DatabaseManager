package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.Entities.User;

public interface UserDAO extends CrudDAO<User> {
    User findByLogin(String login);
}
