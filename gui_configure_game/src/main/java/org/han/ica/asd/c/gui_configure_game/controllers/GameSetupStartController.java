package org.han.ica.asd.c.gui_configure_game.controllers;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameSetupStartController {

    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    private int baseRoundNumber = 20;

    @Inject
    @Named("GameSetup")
    IGUIHandler gameSetup;

    @Inject
    @Named("MainMenu")
    IGUIHandler mainMenu;


    @Inject
    private Configuration configuration;

    //To Do vragen over gamename in db
    @FXML
    private TextField gameName;

    @FXML
    private TextField roundNumber;

    @FXML
    private CheckBox bankrupt;

    @FXML
    private CheckBox seeDetail;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private Button nextScreenButton;

    @FXML
    private Button back;


    /**
     * Method to initialize the controller. Will only be called once when the fxml is loaded.
     */
    public void initialize() {
        mainContainer.getChildren().addAll();
        nextScreen();
        backButton();
    }

    /**
     * Button function to proceed to the next screen it will parse the configuration to the next screen.
     */
    @FXML
    public void nextScreen() {
        nextScreenButton.setOnAction(event -> {
            fillConfiguration();
            gameSetup.setData(new Object[]{configuration});
            gameSetup.setupScreen();
        });
    }


    /**
     * Button function to return to the previous screen
     */
    @FXML
    public void backButton() {
        back.setOnAction(event -> mainMenu.setupScreen());
    }

    /**
     * Fill the injected configuration based on the values given in GUI
     */
    private void fillConfiguration() {
        if (roundNumber.getText() != null && !roundNumber.getText().isEmpty()) {
            configuration.setAmountOfRounds(Integer.parseInt(roundNumber.getText()));
        } else {
            configuration.setAmountOfRounds(baseRoundNumber);
        }
        configuration.setContinuePlayingWhenBankrupt(bankrupt.isSelected());
        configuration.setInsightFacilities(seeDetail.isSelected());
    }

}
