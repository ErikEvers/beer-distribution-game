package org.han.ica.asd.c.gui_configure_game.controllers;



import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;


public class GameSetup implements IGUIHandler {

    private Configuration configuration;

    public void setData(Object[] data) {
        this.configuration = (Configuration) data[0];
    }


    @Override
    public void setupScreen() {
       GameSetupController gameSetupController =  FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/GameSetup.fxml"));

        gameSetupController.setConfiguration(configuration);
    }
}
