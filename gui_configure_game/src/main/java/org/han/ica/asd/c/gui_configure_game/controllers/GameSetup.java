package org.han.ica.asd.c.gui_configure_game.controllers;



import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import org.han.ica.asd.c.model.domain_objects.Configuration;


import java.util.ResourceBundle;


public class GameSetup implements IGUIHandler {

    private Configuration configuration;
    private String gamename;
    private boolean onlinegame;
    private String password;

    public void setData(Object[] data) {
        this.configuration = (Configuration) data[0];
        this.gamename = (String) data[1];
        this.onlinegame = (boolean) data[2];
        if(data.length > 3) {
            this.password = (String) data[3];
        }
    }


    @Override
    public void setupScreen() {
        GameSetupController gameSetupController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiGameConfiguration"), getClass().getResource("/fxml/GameSetup.fxml"));
        gameSetupController.setConfiguration(configuration);
        gameSetupController.setGameName(gamename);
        gameSetupController.setOnlineGame(onlinegame);
        if(password != null) {
            gameSetupController.setPassword(password);
        }
    }
}
