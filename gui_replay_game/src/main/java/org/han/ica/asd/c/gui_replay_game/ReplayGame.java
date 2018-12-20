package org.han.ica.asd.c.gui_replay_game;

import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;

public class ReplayGame {
    private static final String GAME_TITLE = "Beer Distribution Game";
    private final Provider<FXMLLoaderOnSteroids> loaderProvider;

    @Inject
    public ReplayGame(Provider<FXMLLoaderOnSteroids> loaderProvider){
        this.loaderProvider = loaderProvider;
    }


    public void setupReplayGameListScreen(Stage primaryStage) throws IOException {
        setScreen("/fxml/ReplayGameListScreen.fxml",primaryStage);
    }
    public void setupScreen(Stage primaryStage) throws Exception {
        FXMLLoaderOnSteroids loader = loaderProvider.get();
        loader.setLocation(getClass().getResource("/fxml/ReplayGameScreen.fxml"));

        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }

    private void setScreen(String pathFxml, Stage primaryStage) throws IOException {
        FXMLLoaderOnSteroids loader = loaderProvider.get();
        loader.setLocation(getClass().getResource(pathFxml));
        Parent root = loader.load();

        if (!ComponentOrientation.getOrientation(new Locale(System.getProperty("user.language"))).isLeftToRight()) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }

        primaryStage.setTitle(GAME_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
