package org.han.ica.asd.c.gui_replay_game;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;

import javax.inject.Inject;
import javax.inject.Provider;

public class ReplayGame {
    private final Provider<FXMLLoaderOnSteroids> loaderProvider;

    @Inject
    public ReplayGame(Provider<FXMLLoaderOnSteroids> loaderProvider){
        this.loaderProvider = loaderProvider;
    }


    public void setupScreen(Stage primaryStage) throws Exception {
        FXMLLoaderOnSteroids loader = loaderProvider.get();
        loader.setLocation(getClass().getResource("/fxml/ReplayGameScreen.fxml"));

        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }

}
