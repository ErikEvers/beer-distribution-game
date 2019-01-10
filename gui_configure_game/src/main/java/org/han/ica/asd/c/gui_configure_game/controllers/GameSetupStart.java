package org.han.ica.asd.c.gui_configure_game.controllers;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;


public class GameSetupStart implements IGUIHandler {


    private String gamename;


    @Override
    public void setData(Object[] data) {
        this.gamename = (String) data[0];

    }

    @Override
    public void setupScreen() {
        GameSetupStartController gameSetupStartController = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/GameSetupStart.fxml"));
        gameSetupStartController.setGameName(gamename);

    }
}
