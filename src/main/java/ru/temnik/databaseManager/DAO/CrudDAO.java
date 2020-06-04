package ru.temnik.databaseManager.DAO;

import java.util.List;

public interface CrudDAO<T> {
    List<T> findAll();
    void save(T obj);

}
