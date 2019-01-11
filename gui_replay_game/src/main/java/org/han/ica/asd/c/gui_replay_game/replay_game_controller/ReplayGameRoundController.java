package org.han.ica.asd.c.gui_replay_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;

public class ReplayGameRoundController {

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private AnchorPane facilitiesContainer;

    @FXML
    private Button returnButton;

    @FXML
    private Button previousRoundButton;

    @FXML
    private TextField roundId;

    @FXML
    private Label maxRoundId;

    @FXML
    private Button NextRoundButton;

    @FXML
    private Label gameName;

    @FXML
    private Button showGraph;

    @Inject
    @Named("ReplayGame")
    IGUIHandler replayGame;

    @Inject
    @Named("ReplayGameList")
    IGUIHandler replayGameList;

    @FXML
    void handleNextRoundButton(ActionEvent event) {

    }

    @FXML
    void handlePreviousRoundButton(ActionEvent event) {

    }

    @FXML
    void handleReturnButton(ActionEvent event) {
        replayGameList.setupScreen();
    }

    @FXML
    void handleRoundIdChange(InputMethodEvent event) {

    }

    @FXML
    void showGraphHandler(ActionEvent event) {
        replayGame.setupScreen();
    }

    public void setGameId(String gameId){
        //TODO GET GAME VALUES.
    }

}
