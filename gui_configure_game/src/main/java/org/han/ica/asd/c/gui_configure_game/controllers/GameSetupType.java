package org.han.ica.asd.c.gui_configure_game.controllers;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

public class GameSetupType implements IGUIHandler {


    private Configuration configuration;
    private String gamename;
    private String onlingame;

    @Override
    public void setData(Object[] data) {
        this.configuration = (Configuration) data[0];
        this.gamename = (String) data[1];
        this.onlingame = (String) data[2];
    }

    @Override
    public void setupScreen() {
        GameSetupTypeController gameSetupTypeController = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/GameSetupType.fxml"));
        gameSetupTypeController.setConfiguration(configuration);
        gameSetupTypeController.setGameName(gamename);
        gameSetupTypeController.isOnlineGame(onlingame);
    }
}
