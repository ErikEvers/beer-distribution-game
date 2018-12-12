package org.han.ica.asd.c.gui_program_agent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static final String GAME_TITLE = "Beer Distribution Game";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProgramAgent.fxml"));

        primaryStage.setTitle(GAME_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}