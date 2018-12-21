package org.han.ica.asd.c.gui_replay_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class ReplayGameList implements IGUIHandler {
    @Override
    public void setData(Object[] data) {
    }

    public void setupScreen() {
         FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/ReplayGameListScreen.fxml"));
    }
}
