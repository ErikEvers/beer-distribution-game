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

    private final int baseRoundNumber = 20;
    private final int baseMinNumber = 1;
    private final int baseMaxNumber = 50;

    @Inject
    @Named("GameSetup")
    IGUIHandler gameSetup;

    @Inject
    @Named("MainMenu")
    IGUIHandler mainMenu;


    @Inject
    private Configuration configuration;
    private String gameNamef = "";
    private String onlineGame = "TRUE";
    @FXML
    private TextField gameName;

    @FXML
    private TextField roundNumber;

    @FXML
    private TextField minOrder;

    @FXML
    private TextField maxOrder;

    @FXML
    private CheckBox bankrupt;

    @FXML
    private CheckBox seeDetail;

    @FXML
    private CheckBox offlineGame;

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
            Object[] data = new Object[3];
            fillConfiguration();
            data[0] = configuration;
            data[1] = gameNamef;
            if (offlineGame.isSelected()) {
                data[2] = "FALSE";
            } else data[2] = onlineGame;
            gameSetup.setData(data);
            gameSetup.setupScreen();
        });
    }


    /**
     * Button function to return to the previous screen
     */
    @FXML
    private void backButton() {
        back.setOnAction(event -> mainMenu.setupScreen());
    }


    /**
     * Fill the injected configuration based on the values given in GUI
     */
    private void fillConfiguration() {
        if (gameName.getText() != null && !roundNumber.getText().isEmpty()) {
            gameNamef = gameName.getText();
        }
        if (roundNumber.getText() != null && !roundNumber.getText().isEmpty()) {
            configuration.setAmountOfRounds(Integer.parseInt(roundNumber.getText()));
        } else {
            configuration.setAmountOfRounds(baseRoundNumber);
        }
        if (minOrder.getText() != null && !minOrder.getText().isEmpty()) {
            configuration.setMinimalOrderRetail(Integer.parseInt(minOrder.getText()));
        } else {
            configuration.setMinimalOrderRetail(baseMinNumber);
        }
        if (maxOrder.getText() != null && !maxOrder.getText().isEmpty()) {
            configuration.setMaximumOrderRetail(Integer.parseInt(maxOrder.getText()));
        } else {
            configuration.setMaximumOrderRetail(baseMaxNumber);
        }

        configuration.setContinuePlayingWhenBankrupt(bankrupt.isSelected());
        configuration.setInsightFacilities(seeDetail.isSelected());
    }

}
