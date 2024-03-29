package ru.temnik.databaseManager.Scenes;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.temnik.databaseManager.ControllerGUI;
import ru.temnik.databaseManager.DAO.*;
import ru.temnik.databaseManager.Entities.*;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeScene extends AbstractScene {
    private final TableDAO tableDAO;
    private final DataSource dataSource;
    private CrudDAO crudDAO;
    private Map<String, Button> controlBtns;

    public HomeScene(ControllerGUI controllerGUI) {
        super(controllerGUI);
        tableDAO = controllerGUI.getTableDAO();
        controlBtns = new HashMap<>();
        dataSource = controllerGUI.getApp().getDataSource();
    }

    @Override
    public void init() {
        stage = controllerGUI.getMainStage();
        //Инициализация сцены
        initNodes();
    }

    private void initNodes() {
        //Создание и настройка основных контейнеров сцены
        //root - корневой
        BorderPane root = new BorderPane();
        //topBox - для кнопок CRUD
        HBox topBox = new HBox();
        topBox.setSpacing(10.0);
        //Для списка доступных таблиц БД
        VBox leftBox = new VBox();
        //Для отображение TableView, а также в него вкладывается topBox
        VBox centerBox = new VBox();
        centerBox.setSpacing(10.0);



        //Метка для поиска таблиц
        Label searchTableLabel = new Label("Поиск по таблицам: ");
        nodes.put("searchTableLabel", searchTableLabel);
        //Поле для поиска таблиц
        TextField searchTableField = new TextField();
        nodes.put("searchTableField", searchTableField);

        //CRUD кнопки. Сохраняются в Map, чтоб иметь доступ потом из подметодов.
        Button btnAdd = new Button("Добавить");
        controlBtns.put("btnAdd", btnAdd);
        Button btnChange = new Button("Изменить");
        controlBtns.put("btnChange", btnChange);
        Button btnDelete = new Button("Удалить");
        controlBtns.put("btnDelete", btnDelete);
        Button btnSearch = new Button("Поиск по таблице");
        controlBtns.put("btnSearch", btnSearch);
        //До выбора таблицы из списка слева все кнопки должны быть не активны
        btnAdd.setDisable(true);
        btnChange.setDisable(true);
        btnDelete.setDisable(true);
        btnSearch.setDisable(true);


        topBox.getChildren().addAll(btnAdd, btnChange, btnDelete, btnSearch);
        centerBox.getChildren().add(topBox);

        List<String> listTables = tableDAO.findAll();
        ObservableList<String> tables = FXCollections.observableList(listTables);
        ListView<String> listViewTables = new ListView<>(tables);


        MultipleSelectionModel<String> tableSelectionModel = listViewTables.getSelectionModel();
        tableSelectionModel.selectedItemProperty().addListener((changed, oldValue, newValue) -> {
            btnSearch.setDisable(false);
            btnAdd.setDisable(false);
            if (newValue == null) {
                return;
            }
            EntitiesType entityType = EntitiesType.valueOf(newValue.toUpperCase());
            ObservableList<Object> objects = null;
            Object objectDao = null;
            switch (entityType) {
                case DOCUMENT_STATUSES:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, DocumentStatus.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
                case USERS:
                   /* crudDAO=new CrudDAOJdbcImpl(dataSource, User.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;*/
                    crudDAO = new UserDaoJdbcImpl(dataSource);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
                case DEGREES_OF_SECRECY:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, DegreeOfSecrecy.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    objectDao = crudDAO;
                    break;
                case ACTS_ADMISSION:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, ActAdm.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
                case ACTS_EXTRADITION:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, ActExtr.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
                case READERS:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, Reader.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
                case DOCUMENTS:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, Document.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
                case EMPLOYEES:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, Employee.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
                case PASSPORTS:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, Passport.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
                case POSITIONS_EMPLOYEES:
                    crudDAO = new CrudDAOJdbcImpl(dataSource, PositionEmployee.class);
                    objects = FXCollections.observableArrayList(crudDAO.findAll().toArray());
                    objectDao = crudDAO;
                    break;
            }
            TableView<Object> table = getTable(objects, objectDao);
            if (centerBox.getChildren().size() > 1) {
                centerBox.getChildren().remove(1);
            }
            centerBox.getChildren().add(table);
        });


        searchTableField.textProperty().addListener((ae) -> {
            tables.clear();
            List<String> listTablesSearch = tableDAO.findTableNamesByName(searchTableField.getText());
            for (String table : listTablesSearch) {
                tables.add(table);
            }
        });


        ScrollPane scrollPane = new ScrollPane(listViewTables);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        leftBox.setSpacing(10.0);
        leftBox.setMaxWidth(200);
        leftBox.getChildren().addAll(searchTableLabel, searchTableField, scrollPane);
        root.setLeft(leftBox);
        root.setCenter(centerBox);
        BorderPane.setMargin(leftBox, new Insets(5.0, 5.0, 5.0, 5.0));
        BorderPane.setMargin(centerBox, new Insets(30.0, 10.0, 5.0, 5.0));
        scene = new Scene(root);
    }

    private TableView<Object> getTable(ObservableList<Object> objects, Object objectDao) {
        TableView<Object> table = new TableView<>(objects);
        CrudDAO crudDAO = (CrudDAO) objectDao;
        Class entityClass = crudDAO.getEntityClass();
        Field[] fields = entityClass.getDeclaredFields();
        configControlBtns(table, fields, objectDao, entityClass);
        for (int i = 0; i < fields.length; i++) {
            TableColumn<Object, String> col = new TableColumn<>(fields[i].getName());
            col.setCellValueFactory(new PropertyValueFactory<>(fields[i].getName()));
            table.getColumns().add(col);
        }
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private void configControlBtns(TableView<Object> table, Field[] fields, Object objDAO, Class entityClass) {
        ObservableList<Object> objects = table.getItems();
        controlBtns.get("btnSearch").setOnAction((ae) -> {
            Stage stageSearch = new Stage();
            VBox rows = new VBox();
            rows.setSpacing(10.0);
            for (int i = 0; i < fields.length; i++) {
                Label label = new Label(fields[i].getName());
                TextField textField = new TextField();
                VBox row = new VBox();
                VBox.setMargin(row, new Insets(10.0, 10.0, 10.0, 10.0));
                row.getChildren().addAll(label, textField);
                row.setSpacing(5.0);
                rows.getChildren().add(row);
            }
            Button btn = new Button("Найти");
            btn.setOnAction((ae2) -> {
                Object entity = null;
                try {
                    entity = entityClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ObservableList<Node> rowsList = rows.getChildren();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    VBox row = (VBox) rowsList.get(i);
                    TextField textField = (TextField) row.getChildren().get(1);
                    try {
                        field.setAccessible(true);
                        String valueSearch = textField.getText().toLowerCase();
                        Object value = null;
                        Type type = field.getType();
                        if (type.getTypeName().equals("int")) {
                            int valueInt = Integer.parseInt(valueSearch);
                            value = new Integer(valueInt);
                        } else {
                            value = valueSearch;
                        }
                        field.set(entity, value);
                    } catch (NumberFormatException e) {
                        try {
                            field.set(entity, -1);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                CrudDAO crudDAO = (CrudDAO) objDAO;

                objects.clear();
                objects.addAll(crudDAO.findByParameters(entity));
            });
            rows.getChildren().add(btn);
            Scene scene = new Scene(rows);
            stageSearch.setScene(scene);
            stageSearch.show();
        });

        //

        TableView.TableViewSelectionModel<Object> selectionModel = table.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    Button btnChange = controlBtns.get("btnChange");
                    Button btnDelete = controlBtns.get("btnDelete");
                    btnChange.setDisable(false);
                    btnDelete.setDisable(false);
                    btnChange.setOnAction((aeC) -> {
                        Stage stageSearch = new Stage();
                        VBox rows = new VBox();
                        rows.setSpacing(10.0);
                        for (int i = 1; i < fields.length; i++) {
                            fields[i].setAccessible(true);
                            Label label = new Label(fields[i].getName());
                            TextField textField = new TextField();
                            try {
                                Type type = fields[i].getType();
                                if (type.getTypeName().equals("int")) {
                                    textField.setText(String.valueOf(fields[i].get(newValue)));
                                } else {
                                    textField.setText((String) fields[i].get(newValue));
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            VBox row = new VBox();
                            VBox.setMargin(row, new Insets(10.0, 10.0, 10.0, 10.0));
                            row.getChildren().addAll(label, textField);
                            row.setSpacing(5.0);
                            rows.getChildren().add(row);
                        }
                        Button btn = new Button("Изменить");
                        btn.setOnAction((ae2) -> {
                            stageSearch.close();
                            Object entity = null;
                            try {
                                entity = entityClass.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ObservableList<Node> rowsList = rows.getChildren();
                            for (int i = 1; i < fields.length; i++) {
                                Field field = fields[i];
                                VBox row = (VBox) rowsList.get(i - 1);
                                TextField textField = (TextField) row.getChildren().get(1);
                                try {
                                    field.setAccessible(true);
                                    String valueSearch = textField.getText();
                                    Object value = null;
                                    Type type = field.getType();
                                    if (type.getTypeName().equals("int")) {
                                        int valueInt = Integer.parseInt(valueSearch);
                                        value = new Integer(valueInt);
                                    } else {
                                        value = valueSearch;
                                    }
                                    field.set(entity, value);
                                } catch (NumberFormatException e) {
                                    try {
                                        field.set(entity, 0);
                                    } catch (IllegalAccessException ex) {
                                        ex.printStackTrace();
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                            CrudDAO crudDAO = (CrudDAO) objDAO;
                            try {
                                fields[0].setAccessible(true);
                                fields[0].set(entity, fields[0].get(newValue));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            crudDAO.update(entity);
                            objects.clear();
                            objects.addAll(crudDAO.findAll());


                        });
                        rows.getChildren().add(btn);
                        Scene scene = new Scene(rows);
                        stageSearch.setScene(scene);
                        stageSearch.show();
                    });
                    btnDelete.setOnAction((aeD) -> {
                        CrudDAO crudDAO = (CrudDAO) objDAO;
                        crudDAO.delete(newValue);
                        objects.clear();
                        objects.addAll(crudDAO.findAll());
                    });

                }
            }
        });
        Button btnAdd = controlBtns.get("btnAdd");
        btnAdd.setOnAction((ae) -> {
            Stage stageSearch = new Stage();
            VBox rows = new VBox();
            rows.setSpacing(10.0);
            for (int i = 1; i < fields.length; i++) {
                Label label = new Label(fields[i].getName());
                TextField textField = new TextField();
                VBox row = new VBox();
                VBox.setMargin(row, new Insets(10.0, 10.0, 10.0, 10.0));
                row.getChildren().addAll(label, textField);
                row.setSpacing(5.0);
                rows.getChildren().add(row);
            }
            Button btn = new Button("Добавить");
            btn.setOnAction((ae2) -> {
                stageSearch.close();
                Object entity = null;
                try {
                    entity = entityClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ObservableList<Node> rowsList = rows.getChildren();
                for (int i = 1; i < fields.length; i++) {
                    Field field = fields[i];
                    VBox row = (VBox) rowsList.get(i - 1);
                    TextField textField = (TextField) row.getChildren().get(1);
                    try {
                        field.setAccessible(true);
                        String valueSearch = textField.getText();
                        Object value = null;
                        Type type = field.getType();
                        if (type.getTypeName().equals("int")) {
                            int valueInt = Integer.parseInt(valueSearch);
                            value = new Integer(valueInt);
                        } else {
                            value = valueSearch;
                        }
                        field.set(entity, value);
                    } catch (NumberFormatException e) {
                        try {
                            field.set(entity, 0);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                CrudDAO crudDAO = (CrudDAO) objDAO;
                crudDAO.save(entity);
                objects.clear();
                objects.addAll(crudDAO.findAll());
            });
            rows.getChildren().add(btn);
            Scene scene = new Scene(rows);
            stageSearch.setScene(scene);
            stageSearch.show();
        });
    }


    @Override
    public void show(Scene prevScene) {
        if (scene != null) {
            Platform.runLater(() -> {
                stage.setScene(scene);
                stage.setMinWidth(1000);
                stage.setMinHeight(600);
            });

            controllerGUI.setCurrentScene(this);
        }
    }
}
