package org.han.ica.asd.c.gui_replay_game;

import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_replay_game.replay_game_controller.ReplayGameRoundController;

public class ReplayGameRound implements IGUIHandler {
    private String gameId;
    private int currentRound;
    @Override
    public void setData(Object[] data) {
        if (data[0].getClass().equals(String.class)){
            this.gameId = (String) data[0];
        }
        else if (data[0].getClass().equals(Integer.class)){
            this.currentRound = (int) data[0];
        }
    }

    @Override
    public void setupScreen() {
        ReplayGameRoundController replayGameRoundController = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/ReplayGameRoundScreen.fxml"));
        if (gameId != null){
            DaoConfig.setCurrentGameId(gameId);
        }
        else{
            replayGameRoundController.setCurrentRound(currentRound);
        }
    }
}
