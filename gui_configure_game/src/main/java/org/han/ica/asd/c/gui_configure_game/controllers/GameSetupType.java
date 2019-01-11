package org.han.ica.asd.c.gui_configure_game.controllers;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import org.han.ica.asd.c.model.domain_objects.Configuration;

import java.util.ResourceBundle;


public class GameSetupType implements IGUIHandler {


    private Configuration configuration;
    private String gamename;
    private String onlinegame;


    @Override
    public void setData(Object[] data) {
        this.configuration = (Configuration) data[0];
        this.gamename = (String) data[1];
        this.onlinegame = (String) data[2];

    }

    @Override
    public void setupScreen() {
        GameSetupTypeController gameSetupTypeController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiGameConfiguration"), getClass().getResource("/fxml/GameSetupType.fxml"));
        gameSetupTypeController.setConfiguration(configuration);
        gameSetupTypeController.setGameName(gamename);
        gameSetupTypeController.isOnlineGame(onlinegame);
    }
}
