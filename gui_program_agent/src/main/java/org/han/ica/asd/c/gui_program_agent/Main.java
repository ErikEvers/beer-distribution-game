package org.han.ica.asd.c.gui_program_agent;

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
    private static final String GAME_TITLE = "Beer Distribution Game";

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languageResources");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProgramAgent.fxml"), resourceBundle);

        if (!ComponentOrientation.getOrientation(new Locale(System.getProperty("user.language"))).isLeftToRight()) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        primaryStage.setTitle(GAME_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}