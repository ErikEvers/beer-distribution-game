package org.han.ica.asd.c.gui_replay_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_replay_game.replay_game_controller.ReplayGameRoundController;

public class ReplayGameRound implements IGUIHandler {
    private String gameId;
    @Override
    public void setData(Object[] data) {
        this.gameId = (String) data[0];
    }

    @Override
    public void setupScreen() {
        ReplayGameRoundController replayGameRoundController = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/ReplayGameRoundScreen.fxml"));
        replayGameRoundController.setGameId(gameId);
    }
}
