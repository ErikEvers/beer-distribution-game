package org.han.ica.asd.c.gui_replay_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_replay_game.replay_game_controller.ReplayGameController;

import java.util.ResourceBundle;

public class ReplayGame implements IGUIHandler {
    private int currentRound;

    @Override
    public void setData(Object[] data) {
        this.currentRound = (int) data[0];
    }


    public void setupScreen() {
        ReplayGameController replayGameController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesReplayGame"), getClass().getResource("/fxml/ReplayGameScreen.fxml"));
        replayGameController.setCurrentRound(currentRound);
    }
}
