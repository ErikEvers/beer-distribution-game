package org.han.ica.asd.c.gui_replay_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class ReplayGameList implements IGUIHandler {
    private String gameId;
    @Override
    public void setData(Object[] data) {
        this.gameId = (String) data[0];
    }

    public void setupScreen() {
        ReplayGameScreenController replayGameScreenController =FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/ReplayGameListScreen.fxml"));
        replayGameScreenController.setGameId(gameId);
    }
}
