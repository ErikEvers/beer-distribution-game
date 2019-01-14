package org.han.ica.asd.c.gui_configure_game.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;
import javax.inject.Named;


public class GameSetupStartController {

    private static final int BASEROUNDNUMBER = 20;
    private static final int BASEMINNUMBER = 1;
    private static final int BASEMAXNUMBER = 50;

    @Inject
    @Named("GameSetup")
    IGUIHandler gameSetup;

    @Inject
    @Named("MainMenu")
    IGUIHandler mainMenu;


    @Inject
    private Configuration configuration;

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
    private Button back;


    /**
     * Method to initialize the controller. Will only be called once when the fxml is loaded.
     */
    public void initialize() {
        mainContainer.getChildren().addAll();
        backButton();
    }

    /**
     * Button function to proceed to the next screen it will parse the configuration to the next screen.
     */
    @FXML
    public void nextScreenButton() {

        Object[] data = new Object[3];
        fillConfiguration();
        data[0] = configuration;
        if (gameName.getText() != null && !gameName.getText().isEmpty()) {
            data[1] = gameName.getText();
        }
        if (offlineGame.isSelected()) {
            data[2] = "FALSE";
        } else data[2] = "TRUE";
        gameSetup.setData(data);
        gameSetup.setupScreen();
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

        if (roundNumber.getText() != null && !roundNumber.getText().isEmpty()) {
            configuration.setAmountOfRounds(Integer.parseInt(roundNumber.getText()));
        } else {
            configuration.setAmountOfRounds(BASEROUNDNUMBER);
        }
        if (minOrder.getText() != null && !minOrder.getText().isEmpty()) {
            configuration.setMinimalOrderRetail(Integer.parseInt(minOrder.getText()));
        } else {
            configuration.setMinimalOrderRetail(BASEMINNUMBER);
        }
        if (maxOrder.getText() != null && !maxOrder.getText().isEmpty()) {
            configuration.setMaximumOrderRetail(Integer.parseInt(maxOrder.getText()));
        } else {
            configuration.setMaximumOrderRetail(BASEMAXNUMBER);
        }

        configuration.setContinuePlayingWhenBankrupt(bankrupt.isSelected());
        configuration.setInsightFacilities(seeDetail.isSelected());
    }


    void setGameName(String gamename) {
        gameName.setText(gamename);
    }

    void setConfigurationInScreen(Configuration configuration) {
        roundNumber.setText(String.valueOf(configuration.getAmountOfRounds()));
        minOrder.setText(String.valueOf(configuration.getMinimalOrderRetail()));
        maxOrder.setText(String.valueOf(configuration.getMaximumOrderRetail()));
        bankrupt.setSelected(configuration.isContinuePlayingWhenBankrupt());
        seeDetail.setSelected(configuration.isInsightFacilities());
    }


    void setOnlineGame(String onlineGame) {
        if ("FALSE".equals(onlineGame)) {
            offlineGame.setSelected(true);
        }
    }
}
