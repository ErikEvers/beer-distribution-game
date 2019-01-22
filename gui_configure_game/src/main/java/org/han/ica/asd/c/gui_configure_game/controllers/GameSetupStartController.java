package org.han.ica.asd.c.gui_configure_game.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.fxml_helper.NumberInputFormatter;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Timer;
import java.util.function.UnaryOperator;


public class GameSetupStartController {

    private static final int BASEROUNDNUMBER = 20;
    private static final int BASEMINNUMBER = 0;
    private static final int BASEMAXNUMBER = 40;

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

    private Timer maxInputTimer;


    /**
     * Method to initialize the controller. Will only be called once when the fxml is loaded.
     */
    public void initialize() {
        UnaryOperator<TextFormatter.Change> textFieldFilter = NumberInputFormatter.getChangeUnaryOperator();
        roundNumber.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), BASEROUNDNUMBER, textFieldFilter));
        minOrder.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), BASEMINNUMBER, textFieldFilter));
				minOrder.textProperty().addListener((observable, oldValue, newValue) -> checkMaxInputValue());
        maxOrder.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), BASEMAXNUMBER, textFieldFilter));
				maxOrder.textProperty().addListener((observable, oldValue, newValue) -> checkMaxInputValue());


        mainContainer.getChildren().addAll();
        backButton();
    }

    private void checkMaxInputValue() {
			maxInputTimer = new Timer();
			maxInputTimer.schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						if(Integer.parseInt(maxOrder.getText()) < Integer.parseInt(minOrder.getText())) {
							maxOrder.setText(minOrder.getText());
							maxOrder.positionCaret(maxOrder.getText().length());
						}
					}
				},
				500
			);
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
            data[2] = false;
        } else data[2] = true;
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


    void setOnlineGame(boolean onlineGame) {
        if (!onlineGame) {
            offlineGame.setSelected(true);
        }
    }
}
