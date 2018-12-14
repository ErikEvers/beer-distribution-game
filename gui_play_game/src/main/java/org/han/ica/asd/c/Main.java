package org.han.ica.asd.c;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languageResources");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"), resourceBundle);
        primaryStage.setScene(new Scene(root));
        if (!ComponentOrientation.getOrientation(new Locale(System.getProperty("user.language"))).isLeftToRight()){
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
