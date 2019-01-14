package org.han.ica.asd.c.gui_replay_game.replay_game_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_replay_game.IVisualisedPlayedGameData;

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
    private IVisualisedPlayedGameData replayComponent;

    @Inject
    @Named("ReplayGame")
    IGUIHandler replayGame;

    @Inject
    @Named("ReplayGameList")
    IGUIHandler replayGameList;

    @FXML
    void initialize() {
        roundId.setTextFormatter(NumericTextFormatter.getTextFormatter());
        maxRoundId.setText(replayComponent.getTotalRoundsString());

        updateCurrentRound();
    }

    @FXML
    void handleNextRoundButton(ActionEvent event) {
        if (replayComponent.incrementCurrentRound()) {
            updateCurrentRound();
        }
    }

    @FXML
    void handlePreviousRoundButton(ActionEvent event) {
        if (replayComponent.decrementCurrentRound()) {
            updateCurrentRound();
        }
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
        replayGame.setData(new Integer[]{Integer.parseInt(replayComponent.getCurrentRoundString())});
        replayGame.setupScreen();
    }

    private void updateCurrentRound() {
        roundId.setText(replayComponent.getCurrentRoundString());
    }

    public void setCurrentRound(int round){
        replayComponent.updateCurrentRound(round);
        updateCurrentRound();
    }
}
