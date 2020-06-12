package ru.temnik.databaseManager.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.temnik.databaseManager.Annotation.AnnotationException.IdException;
import ru.temnik.databaseManager.Annotation.AnnotationException.TableAnnotationNotFound;
import ru.temnik.databaseManager.Annotation.Id;
import ru.temnik.databaseManager.Annotation.Table;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudDAOJdbcImpl<T> implements CrudDAO<T> {
    //Класс entity, для которой создается реализация CRUD
    private Class entityClass;
    //Имя таблицы с которой работает реализация (Берется из аннотации @Table(name = ?))
    private String tableName;
    private Connection connection;

    private final StringBuilder SQL_FIND_ALL = new StringBuilder("SELECT * FROM ");
    private final StringBuilder SQL_SAVE = new StringBuilder("INSERT INTO ");
    private final StringBuilder SQL_DELETE = new StringBuilder("DELETE FROM ");
    private final StringBuilder SQL_UPDATE = new StringBuilder("UPDATE ");
    private Field fieldId;

    private static final Logger logger = LogManager.getLogger();


    public CrudDAOJdbcImpl(DataSource dataSource, Class<T> entityClass) {
        this.entityClass = entityClass;
        //Метод собирающий стандартные SQL запросы (SELECT ALL,INSERT,DELETE,UPDATE)
        assemblyDAO();
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void assemblyDAO() {
        // У сущности БД обязательно должна быть аннотация @Table с указанием таблицы, с которой
        // будет работать реализация, иначе будет выброшено исключение.
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = (Table) entityClass.getAnnotation(Table.class);
            tableName = table.name();
            Field[] fields = entityClass.getDeclaredFields();
            /*
            Дальше происходит поиск поля с аннотацией @Id.
            Если не будет найдено такое поле или будет найдено 2 и более таких полей, то
            будет выброшено исключение
             */
            int counterId = 0;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (counterId > 0) {
                        try {
                            throw new IdException("Таблица: " + tableName + ". Найдено более одного поля с аннотацией Id.");
                        } catch (IdException e) {
                            e.printStackTrace();
                        }
                    }
                    counterId++;
                    fieldId = field;
                    fieldId.setAccessible(true);
                }
            }
            if(counterId==0){
                try {
                    throw new IdException("Таблица: " + tableName + ". Отсутствует обязательная аннотация @Id.");
                } catch (IdException e) {
                    e.printStackTrace();
                }
            }
            //Непосредственно формирование всех запросов.
            SQL_FIND_ALL.append(table.name());
            assemblySave(tableName);
            assemblyDelete(tableName);
            assemblyUpdate(tableName);
        } else {
            try {
                throw new TableAnnotationNotFound("Не найдена аннотация @Table у сущности: " + entityClass.getSimpleName());
            } catch (TableAnnotationNotFound ex) {
                ex.printStackTrace();
            }
        }

    }
    /*
    Формирование запроса для обновления записи.
    Формирует запрос используя поля сущности из entityClass.
    Вид запроса создается под PreparedStatement (id = ?),
    значения подставляются уже в методе update(T obj)
     */
    private void assemblyUpdate(String table) {
        SQL_UPDATE.append(table).append(" SET ");
        Field[] fields = entityClass.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field fieldTmp = fields[i];
            // В UPDATE включается все поля, кроме того которое помечено аннотацией @Id
            if (!fieldTmp.isAnnotationPresent(Id.class)) {
                stringBuilder.append(fieldTmp.getName()).append(" = ?, ");
            }
        }
        String values = stringBuilder.toString();
        values = values.substring(0, values.length() - 2);
        SQL_UPDATE.append(values).append(" WHERE ");
        SQL_UPDATE.append(fieldId.getName()).append("= ?");
    }

    private void assemblyDelete(String table) {
        // Удаление работает по id (с front-а приходит обьект для удаления с id.
        SQL_DELETE.append(table).append(" WHERE ");
        SQL_DELETE.append(fieldId.getName()).append("= ?");
    }

    private void assemblySave(String table) {
        // Работа похожа на Update, игнорируется поле с @Id. Остальные поля включаются в запрос
        SQL_SAVE.append(table).append('(');
        Field[] fields = entityClass.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field fieldTmp = fields[i];
            if (!fieldTmp.isAnnotationPresent(Id.class)) {
                stringBuilder.append(fields[i].getName());
                stringBuilder.append(", ");
            }
        }
        String values = stringBuilder.toString();
        values = values.substring(0, values.length() - 2);
        SQL_SAVE.append(values).append(") VALUES (");
        String quest = "";
        for (int i = 1; i < fields.length; i++) {
            quest += "?, ";
        }
        quest = quest.substring(0, quest.length() - 2);
        SQL_SAVE.append(quest).append(')');
    }

    @Override
    public List<T> findAll() {
        try {
            logger.info(SQL_FIND_ALL.toString());
            List<T> entities = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL.toString());
            Field[] fields = entityClass.getDeclaredFields();
            while (resultSet.next()) {
                Object entity = entityClass.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    if (fields[i].getType().getSimpleName().equals("int")) {
                        fields[i].set(entity, resultSet.getInt(fields[i].getName()));
                    } else {
                        fields[i].set(entity, resultSet.getString(fields[i].getName()));
                    }
                }
                entities.add((T) entity);
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
            logger.info(SQL_SAVE.toString());
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE.toString());
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if(!field.isAnnotationPresent(Id.class)){
                    field.setAccessible(true);
                    if (field.getType().getSimpleName().equals("int")) {
                        statement.setInt(i, (int) field.get(obj));
                    } else {
                        statement.setString(i, (String) field.get(obj));
                    }
                }
            }
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(T obj) {
        try {
            logger.info(SQL_DELETE.toString());
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE.toString());
            statement.setInt(1, (int) fieldId.get(obj));
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T obj) {
        Field[] fields = entityClass.getDeclaredFields();
        try {
            logger.info(SQL_UPDATE.toString());
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE.toString());
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if(!field.isAnnotationPresent(Id.class)){
                    field.setAccessible(true);
                    if (field.getType().getSimpleName().equals("int")) {
                        statement.setInt(i, (int) field.get(obj));
                    } else {
                        statement.setString(i, (String) field.get(obj));
                    }
                }
            }
            fieldId.setAccessible(true);
            statement.setInt(fields.length, (int) fieldId.get(obj));
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    /*
    В отличии от остальных запросов, для которых sql запрос генерируется при создание обьекта,
    для поиска по параметрам каждый раз создается новый запрос. Его состав зависит от значений полей
    пришедшего объекта. Игнорируется поля со значением -1* или пустые строки.

    P.s:
    первоначально делал только по id, так как id  не может быть отрицательным, а сейчас понял,
    что это убивает  вообще значение -1 для любых полей int, значит надо поставить условие для
    сущностей, не использовать примитивы, а только обертки, и заменить сравнение с -1, на null.
     */
    @Override
    public List<T> findByParameters(T obj) {
        List<T> entities = new ArrayList<>();
        String sql = assemblyFindByP(obj);
        logger.info(sql);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            Field[] fields = entityClass.getDeclaredFields();
            int counterPosStatement = 1;
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                if (field.getType().getSimpleName().equals("int")) {
                    try {
                        if ((int) field.get(obj) != -1) {
                            statement.setInt(counterPosStatement, (int) field.get(obj));
                            counterPosStatement++;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (!field.get(obj).equals("")) {
                            statement.setString(counterPosStatement, (String) field.get(obj) + '%');
                            counterPosStatement++;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Object entity = entityClass.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    if (fields[i].getType().getSimpleName().equals("int")) {
                        fields[i].set(entity, resultSet.getInt(fields[i].getName()));
                    } else {
                        fields[i].set(entity, resultSet.getString(fields[i].getName()));
                    }
                }
                entities.add((T) entity);
            }
            return entities;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return entities;
    }
    //Сборка sql запроса для поиска по параметрам
    private String assemblyFindByP(T obj) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ")
                .append(tableName).append(" WHERE ");
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType().getSimpleName().equals("int")) {
                try {
                    if ((int) field.get(obj) != -1) {
                        sql.append(field.getName()).append(" = ? AND ");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (!field.get(obj).equals("")) {
                        sql.append("LOWER(").append(field.getName())
                                .append(") ").append(" LIKE ? AND ");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        String value = sql.toString();
        value = value.substring(0, value.length() - 5);
        return value;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }
}
