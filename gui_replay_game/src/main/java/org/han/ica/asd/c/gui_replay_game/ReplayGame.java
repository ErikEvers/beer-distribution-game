package org.han.ica.asd.c.gui_replay_game;

import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class ReplayGame implements IGUIHandler {

    public void setupReplayGameScreen(Stage primaryStage) throws Exception {
//        setScreen("");
//        loader.setLocation(getClass().getResource("/fxml/ReplayGameScreen.fxml"));

    }

    public void setupScreen() {
        FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/ReplayGameListScreen.fxml"));
    }

}
