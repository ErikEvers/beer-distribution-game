package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class JoinGame implements IGUIHandler {
    @Override
    public void setupScreen() {
        FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/JoinGame.fxml"));
    }
}
