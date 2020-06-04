package ru.temnik.databaseManager.DAO;

import java.util.List;

public interface TableDAO extends CrudDAO<String> {
    List<String> findTableNamesByName(String name);

}
