package ru.temnik.databaseManager;

import ru.temnik.databaseManager.Annotation.Table;
import ru.temnik.databaseManager.DAO.CrudDAO;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class Test<T> implements CrudDAO<T> {
    private final String SQL_FIND_ALL="SELECT * FROM ";
    private Class entityClass;
    public  T tmp;
    public Test(Class entityClass){
         this.entityClass=entityClass;
    }

    @Override
    public List<T> findAll() {
        try {
            Field f = this.getClass().getDeclaredField("tmp");
            System.out.println("тип - "+f.getType().getTypeName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(T obj) {



    }

    @Override
    public void delete(T obj) {

    }

    @Override
    public void update(T obj) {

    }

    @Override
    public List<T> findByParameters(T obj) {
        return null;
    }
}
