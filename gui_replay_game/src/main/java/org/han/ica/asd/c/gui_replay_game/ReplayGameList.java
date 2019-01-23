package org.han.ica.asd.c.gui_replay_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class ReplayGameList implements IGUIHandler {
    @Override
    public void setData(Object[] data) {
        // implement interface
    }

    public void setupScreen() {
         FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesReplayGame"), getClass().getResource("/fxml/ReplayGameListScreen.fxml"));
    }
}
