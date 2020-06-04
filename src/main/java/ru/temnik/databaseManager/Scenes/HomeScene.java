package ru.temnik.databaseManager.Scenes;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.temnik.databaseManager.ControllerGUI;
import ru.temnik.databaseManager.DAO.TableDaoJdbcImpl;
import ru.temnik.databaseManager.Entities.EntitiesType;

import java.util.List;

public class HomeScene extends AbstractScene {
    private final TableDaoJdbcImpl tableDAO;

    public HomeScene(ControllerGUI controllerGUI) {
        super(controllerGUI);
        tableDAO = controllerGUI.getTableDAO();
    }

    @Override
    public void init() {
        stage = controllerGUI.getMainStage();
        initNodes();
    }

    private void initNodes() {
        BorderPane root = new BorderPane();
        HBox topBox = new HBox();
        VBox leftBox = new VBox();
        VBox centerBox = new VBox();

        topBox.setSpacing(10.0);
        centerBox.setSpacing(10.0);



        Label searchTableLabel = new Label("Поиск по таблицам: ");
        nodes.put("searchTableLabel", searchTableLabel);

        TextField searchTableField = new TextField();
        nodes.put("searchTableField", searchTableField);

        Button btnAdd = new Button("Добавить");
        Button btnChange = new Button("Изменить");
        Button btnDelete = new Button("Удалить");
        Button btnSearch = new Button("Поиск по таблице");
        btnAdd.setDisable(true);
        btnChange.setDisable(true);
        btnDelete.setDisable(true);
        btnSearch.setDisable(true);


        topBox.getChildren().addAll(btnAdd,btnChange,btnDelete,btnSearch);
        centerBox.getChildren().add(topBox);

        List<String> listTables = tableDAO.findAll();
        ObservableList<String> tables = FXCollections.observableList(listTables);
        ListView<String> listViewTables = new ListView<>(tables);


        MultipleSelectionModel<String> tableSelectionModel = listViewTables.getSelectionModel();
        tableSelectionModel.selectedItemProperty().addListener((changed, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }


        });

        searchTableField.textProperty().addListener((ae) -> {
            tables.clear();
            List<String> listTablesSearch = tableDAO.findTableNamesByName(searchTableField.getText());
            for (String table : listTablesSearch) {
                tables.add(table);
            }
        });
        btnDelete.setOnAction((ae)->{

        });

        btnAdd.setOnAction((ae)->{

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
