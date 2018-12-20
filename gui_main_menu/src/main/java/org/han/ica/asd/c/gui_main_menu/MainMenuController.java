package org.han.ica.asd.c.gui_main_menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.han.ica.asd.c.gui_program_agent.ProgramAgent;
import org.han.ica.asd.c.gui_replay_game.ReplayGame;

import javax.inject.Inject;


public class MainMenuController {

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private Button close;

    @FXML
    private Button createAgent;

    @FXML
    private Button replay;

    @Inject
    private ProgramAgent programAgent;

    @Inject
    private ReplayGame replayGame;

    public void initialize() {
        mainContainer.getChildren().addAll();
        setCloseButtonAction();
        setProgramAgentButtonAction();
        setReplayButtonAction();
    }

    private void setCloseButtonAction() {
        close.setOnAction(event -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
    }

    private void setProgramAgentButtonAction() {
        replay.setOnAction(event -> {
            try {
                replayGame.setupScreen((Stage)close.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setReplayButtonAction() {
        replay.setOnAction(event -> {
            try {
                replayGame.setupReplayGameListScreen((Stage)close.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
