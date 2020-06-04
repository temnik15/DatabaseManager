package ru.temnik.databaseManager.Scenes;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import ru.temnik.databaseManager.ControllerGUI;
import ru.temnik.databaseManager.DAO.UserDAO;
import ru.temnik.databaseManager.DAO.UserDaoJdbcImpl;
import ru.temnik.databaseManager.EntitiesGUI.User;

public class AuthScene extends AbstractScene {
    private final UserDAO userDAO;
    public AuthScene(ControllerGUI controllerGUI) {
        super(controllerGUI);
        userDAO = new UserDaoJdbcImpl(controllerGUI.getApp().getDataSource());
    }

    @Override
    public void init() {
        stage = controllerGUI.getMainStage();
        initNodes();
    }

    private void initNodes() {

        Label sceneName = new Label("Авторизация");
        sceneName.setFont(new Font(32));
        nodes.put("sceneName",sceneName);


        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(30.0, 30.0);
        progress.setVisible(false);
        nodes.put("progress",progress);

        Label errorMsg = new Label("");
        errorMsg.setFont(new Font(16));
        errorMsg.setStyle("-fx-text-fill: red");
        errorMsg.setVisible(false);
        nodes.put("errorMsg",errorMsg);


        Label loginText = new Label("Логин");
        Label passwordText = new Label("Пароль");
        nodes.put("loginText",loginText);
        nodes.put("passwordText",passwordText);

        TextField loginField = new TextField();
        loginField.setMaxWidth(300);
        nodes.put("loginField",loginField);


        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(300);
        nodes.put("passwordField",passwordField);


        Button btnAuth = new Button("Войти");
        nodes.put("btnAuth",btnAuth);
        btnAuth.setOnAction((ae)->{
            progress.setVisible(true);
            errorMsg.setVisible(false);
            new Thread(()->{
                User user = new User(loginField.getText(),passwordField.getText());
                if(app.authorization(userDAO,user)){
                    System.out.println("true");
                    AbstractScene homeScene = controllerGUI.getAllScenes().get("HomeScene");
                    if(homeScene==null){
                        homeScene = new HomeScene(controllerGUI);
                        homeScene.init();
                        controllerGUI.getAllScenes().put("HomeScene",homeScene);
                    }
                    homeScene.show(scene);
                }else{
                    Platform.runLater(()->{
                        errorMsg.setText("Неверный логин или пароль!");
                        sceneAdaptation();
                        passwordField.clear();
                    });
                    errorMsg.setVisible(true);
                }
                progress.setVisible(false);

            }).start();
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(sceneName,progress,loginText,loginField,passwordText,passwordField,btnAuth);
        AnchorPane root = new AnchorPane(sceneName,progress,errorMsg,loginText,loginField,passwordText,passwordField,btnAuth);
        scene = new Scene(root,600,400);
        stage.widthProperty().addListener(e -> {
            sceneAdaptation();
        });
        stage.heightProperty().addListener(e -> {
            sceneAdaptation();
        });
    }

    @Override
    public void show(Scene prevScene) {
        if(scene!=null){
            if(prevScene==null){
                stage.setScene(scene);
                sceneAdaptation();
                controllerGUI.setCurrentScene(this);
            }
        }
    }

    private void sceneAdaptation(){
        AnchorPane.setLeftAnchor(nodes.get("sceneName"), stage.getWidth() / 2 - ((Label)nodes.get("sceneName")).getWidth() / 2);
        AnchorPane.setTopAnchor(nodes.get("sceneName"), 5.0);
        AnchorPane.setLeftAnchor(nodes.get("progress"), stage.getWidth() / 2 - ((ProgressIndicator)nodes.get("progress")).getWidth() / 2);
        AnchorPane.setTopAnchor(nodes.get("progress"), stage.getHeight() / 2 - 170);
        AnchorPane.setLeftAnchor(nodes.get("errorMsg"), stage.getWidth() / 2 - ((Label)nodes.get("errorMsg")).getWidth() / 2);
        AnchorPane.setTopAnchor(nodes.get("errorMsg"), stage.getHeight() / 2 - 155);
        AnchorPane.setLeftAnchor(nodes.get("loginText"), stage.getWidth() / 2 - ((Label)(nodes.get("loginText"))).getWidth() / 2);
        AnchorPane.setTopAnchor(nodes.get("loginText"), stage.getHeight() / 2 - 130);
        AnchorPane.setLeftAnchor(nodes.get("loginField"), stage.getWidth() / 2 - ((TextField)nodes.get("loginField")).getWidth() / 2);
        AnchorPane.setTopAnchor(nodes.get("loginField"), stage.getHeight() / 2 - 100);
        AnchorPane.setLeftAnchor(nodes.get("passwordText"), stage.getWidth() / 2 - ((Label)nodes.get("passwordText")).getWidth() / 2);
        AnchorPane.setTopAnchor(nodes.get("passwordText"), stage.getHeight() / 2 - 75);
        AnchorPane.setLeftAnchor(nodes.get("passwordField"), stage.getWidth() / 2 - ((PasswordField)nodes.get("passwordField")).getWidth() / 2);
        AnchorPane.setTopAnchor(nodes.get("passwordField"), stage.getHeight() / 2 - 50);
        AnchorPane.setLeftAnchor(nodes.get("btnAuth"), stage.getWidth() / 2 - ((Button)nodes.get("btnAuth")).getWidth() / 2);
        AnchorPane.setTopAnchor(nodes.get("btnAuth"), stage.getHeight() / 2 - 10);

    }
}
