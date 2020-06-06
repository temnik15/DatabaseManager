package ru.temnik.databaseManager;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.temnik.databaseManager.DAO.TableDAO;
import ru.temnik.databaseManager.DAO.TableDaoJdbcImpl;
import ru.temnik.databaseManager.Scenes.AbstractScene;
import ru.temnik.databaseManager.Scenes.AuthScene;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ControllerGUI extends Application {
    private App app;
    private Stage mainStage;
    @Setter
    private AbstractScene currentScene;
    private Map<String, AbstractScene> allScenes;
    private TableDAO tableDAO;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        app = new App(this);
        tableDAO = new TableDaoJdbcImpl(app.getDataSource());
        mainStage = stage;
        allScenes = new HashMap<>();

        AuthScene authScene = new AuthScene(this);
        authScene.init();
        authScene.show(null);
        allScenes.put("authScene", authScene);
        mainStage.show();
    }
}
