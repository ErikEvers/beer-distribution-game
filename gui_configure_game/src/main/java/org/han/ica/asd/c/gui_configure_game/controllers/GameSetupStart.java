package org.han.ica.asd.c.gui_configure_game.controllers;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;
import java.util.ResourceBundle;


public class GameSetupStart implements IGUIHandler {

    private String gamename = "";
    private String onlineGame = "TRUE";
    private Configuration configuration;

    @Override
    public void setData(Object[] data) {
        this.configuration = (Configuration) data[0];
        this.gamename = (String) data[1];
        this.onlineGame = (String) data[2];

    }

    @Override
    public void setupScreen() {
        GameSetupStartController gameSetupStartController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiGameConfiguration"), getClass().getResource("/fxml/GameSetupStart.fxml"));
        gameSetupStartController.setGameName(gamename);
        gameSetupStartController.setOnlineGame(onlineGame);
        System.out.println();
        if (configuration != null) {
            gameSetupStartController.setConfigurationInScreen(configuration);

        }
    }
}
