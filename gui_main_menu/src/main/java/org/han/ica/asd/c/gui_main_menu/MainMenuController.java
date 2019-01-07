package org.han.ica.asd.c.gui_main_menu;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;


public class MainMenuController {

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private Button close;

    @FXML
    private Button createAgent;

    @FXML
    private Button replay;

    @FXML
    private Button createGame;

    @Inject
    @Named("GameSetupStart")
    private IGUIHandler gameSetupStart;


    @Inject
    @Named("ProgramAgentList")
    private IGUIHandler programAgentList;

    @Inject
    @Named("ReplayGame")
    private IGUIHandler replayGame;

    @Inject
    @Named("JoinGame")
    private IGUIHandler joinGame;

    public void initialize() {
        mainContainer.getChildren().addAll();
        setCloseButtonAction();
        setProgramAgentButtonAction();
        setReplayButtonAction();
        setCreateGameButtonAction();
    }

    private void setCloseButtonAction() {
        close.setOnAction(event -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
    }

    private void setProgramAgentButtonAction() {
        createAgent.setOnAction(event -> programAgentList.setupScreen());
    }

    private void setReplayButtonAction() {
        replay.setOnAction(event -> replayGame.setupScreen());
    }

    public void handleJoinGameButtonClick() {
        joinGame.setupScreen();
    }

    private void setCreateGameButtonAction() {
        createGame.setOnAction(event -> gameSetupStart.setupScreen());
    }


}
