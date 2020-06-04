package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.EntitiesGUI.User;

public interface UserDAO extends CrudDAO<User> {
    User findByLogin(String login);
}
