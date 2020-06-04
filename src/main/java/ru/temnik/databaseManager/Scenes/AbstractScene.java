package ru.temnik.databaseManager.Scenes;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.temnik.databaseManager.App;
import ru.temnik.databaseManager.ControllerGUI;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractScene {
    protected final App app;
    protected final ControllerGUI controllerGUI;
    protected final Map<String, Node> nodes;
    protected Stage stage;
    protected Scene scene;

    public AbstractScene(ControllerGUI controllerGUI) {
        this.app = controllerGUI.getApp();
        this.controllerGUI = controllerGUI;
        this.nodes = new HashMap<>();
    }

    public abstract void init();

    public abstract void show(Scene prevScene);
}
