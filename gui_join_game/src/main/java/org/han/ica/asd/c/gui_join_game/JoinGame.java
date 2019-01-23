package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class JoinGame implements IGUIHandler {
    @Override
    public void setData(Object[] data) {
        // not used in this handler
    }

    @Override
    public void setupScreen() {
        FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiJoinGame"), getClass().getResource("/fxml/JoinGame.fxml"));
    }

    @Override
    public void updateScreen() {

    }
}
