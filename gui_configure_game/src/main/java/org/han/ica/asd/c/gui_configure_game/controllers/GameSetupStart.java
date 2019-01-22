package org.han.ica.asd.c.gui_configure_game.controllers;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import java.util.ResourceBundle;


public class GameSetupStart implements IGUIHandler {

    private String gamename = "";
    private boolean onlineGame = true;
    private Configuration configuration;

    @Override
    public void setData(Object[] data) {
        this.configuration = (Configuration) data[0];
        this.gamename = (String) data[1];
        this.onlineGame = (boolean) data[2];
    }

    @Override
    public void setupScreen() {
        GameSetupStartController gameSetupStartController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiGameConfiguration"), getClass().getResource("/fxml/GameSetupStart.fxml"));
        gameSetupStartController.setGameName(gamename);
        gameSetupStartController.setOnlineGame(onlineGame);
        if (configuration != null) {
            gameSetupStartController.setConfigurationInScreen(configuration);

        }
    }

    @Override
    public void updateScreen() {

    }
}
