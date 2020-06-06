package ru.temnik.databaseManager.DAO;

import java.util.List;

public interface CrudDAO<T> {
    List<T> findAll();
    void save(T obj);
    void delete(T obj);
    void update(T obj);
    List<T> findByParameters(T obj);
    default Class  getEntityClass(){
        return null;
    }
}
