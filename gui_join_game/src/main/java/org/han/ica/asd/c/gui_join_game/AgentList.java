package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class AgentList implements IGUIHandler {
    @Override
    public void setData(Object[] data) {
        // implement interface

    }

    @Override
    public void setupScreen() {
        FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiJoinGame"), getClass().getResource("/fxml/AgentList.fxml"));
    }

    @Override
    public void updateScreen() {

    }
}
