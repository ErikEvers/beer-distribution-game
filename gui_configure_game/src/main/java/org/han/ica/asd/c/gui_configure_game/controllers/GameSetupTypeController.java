package org.han.ica.asd.c.gui_configure_game.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;
import javax.inject.Named;

public class GameSetupTypeController {

    @Inject
    @Named("GameSetup")
    private IGUIHandler gameSetup;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private Button back;

    /**
     * Configuration variables that sould be passed down to the next screen
     */
    private Configuration configuration;
    private String gameName = "";
    private String onlineGame = "TRUE";

    /**
     * Method to initialize the controller. Will only be called once when the fxml is loaded.
     */
    public void initialize() {
        mainContainer.getChildren().addAll();
        backButton();
    }

    /**
     * Button function to return to the previous screen
     */

    @FXML
    private void backButton() {
        back.setOnAction(event -> gameSetup.setupScreen());
    }

    void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    void setGameName(String gameName) {
        this.gameName = gameName;
    }

    void isOnlineGame(String onlineGame) {
        this.onlineGame = onlineGame;

    }
}
