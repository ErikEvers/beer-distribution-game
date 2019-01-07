package org.han.ica.asd.c.gui_configure_game.controllers;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class GameSetupType implements IGUIHandler {

    @Override
    public void setupScreen() {
        FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/GameSetupType.fxml"));
    }
}
