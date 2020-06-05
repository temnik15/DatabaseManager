package ru.temnik.databaseManager.DAO;

import ru.temnik.databaseManager.Annotation.Table;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudDAOJdbcImpl<T> implements CrudDAO<T> {
    private Class entityClass;
    private Connection connection;
    private  StringBuilder SQL_FIND_ALL=new StringBuilder("SELECT * FROM ");
    private StringBuilder SQL_SAVE = new StringBuilder("INSERT INTO ");
    private StringBuilder SQL_DELETE = new StringBuilder("DELETE FROM ");
    private StringBuilder SQL_UPDATE = new StringBuilder("UPDATE ");

    public CrudDAOJdbcImpl(DataSource dataSource, Class entityClass) {
        this.entityClass = entityClass;
        assemblyDAO();
        try {
            this.connection=dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void assemblyDAO() {
        if(entityClass.isAnnotationPresent(Table.class)){
            Table table = (Table) entityClass.getAnnotation(Table.class);
            SQL_FIND_ALL.append(table.name());
            assemblySave(table.name());
            assemblyDelete(table.name());
            assemblyUpdate(table.name());
        }
    }

    private void assemblyUpdate(String table) {
        SQL_UPDATE.append(table).append(" SET ");
        Field[] fields = entityClass.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <fields.length; i++) {
            stringBuilder.append(fields[i].getName()).append(" = ?, ");
        }
        String values = stringBuilder.toString();
        values=values.substring(0,values.length()-2);
        SQL_UPDATE.append(values).append(" WHERE ");
        SQL_UPDATE.append(fields[0].getName()).append("= ?");
    }

    private void assemblyDelete(String table) {
        SQL_DELETE.append(table).append(" WHERE ");
        Field[] fields = entityClass.getDeclaredFields();
        SQL_DELETE.append(fields[0].getName()).append("= ?");
    }

    private void assemblySave(String table) {
        SQL_SAVE.append(table).append('(');
        Field[] fields = entityClass.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <fields.length; i++) {
            stringBuilder.append(fields[i].getName());
            stringBuilder.append(", ");
        }
        String values = stringBuilder.toString();
        values=values.substring(0,values.length()-2);
        SQL_SAVE.append(values).append(") VALUES (");
        String quest = "";
        for (int i = 1; i <fields.length; i++) {
            quest+="?,";
        }
        quest=quest.substring(0,quest.length()-1);
        SQL_SAVE.append(quest).append(')');
    }

    @Override
    public List<T> findAll() {
        try {
            System.out.println(SQL_FIND_ALL.toString());
            List<T> entities = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL.toString());
            Field[] fields = entityClass.getDeclaredFields();
            while(resultSet.next()){
                Object entity = null;
                entity = entityClass.newInstance();
                for (int i = 0; i <fields.length; i++) {
                    fields[i].setAccessible(true);
                    if(fields[i].getType().getSimpleName().equals("int")){
                        fields[i].set(entity,resultSet.getInt(fields[i].getName()));
                    }else{
                        fields[i].set(entity,resultSet.getString(fields[i].getName()));
                    }
                }
                entities.add((T)entity);
            }
            return entities;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(T obj) {
        Field[] fields = entityClass.getDeclaredFields();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE.toString());
            for (int i = 1; i <fields.length; i++) {
                fields[i].setAccessible(true);
                if(fields[i].getType().getSimpleName().equals("int")){
                    statement.setInt(i,(int)fields[i].get(obj));
                }else{
                    statement.setString(i,(String)fields[i].get(obj));
                }
            }
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(T obj) {
        Field[] fields = entityClass.getDeclaredFields();
        fields[0].setAccessible(true);
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE.toString());
            statement.setInt(1,(int)fields[0].get(obj));
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T obj) {
        Field[] fields = entityClass.getDeclaredFields();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE.toString());
            for (int i = 1; i <fields.length; i++) {
                fields[i].setAccessible(true);
                if(fields[i].getType().getSimpleName().equals("int")){
                    statement.setInt(i,(int)fields[i].get(obj));
                }else{
                    statement.setString(i,(String)fields[i].get(obj));
                }
            }
            fields[0].setAccessible(true);
            statement.setInt(fields.length,(int)fields[0].get(obj));
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> findByParameters(T obj) {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }
}
