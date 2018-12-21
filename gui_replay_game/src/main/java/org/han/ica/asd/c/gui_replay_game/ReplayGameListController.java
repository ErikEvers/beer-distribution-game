package org.han.ica.asd.c.gui_replay_game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

public class ReplayGameListController {

    @FXML
    Button back;

    @FXML
    Button replay;

    @FXML
    AnchorPane mainContainer;

    @Inject
    ReplayGame replayGame;

    /***
     * Function for initialising the current ProgramAgentInfo FXML. It also sets the actions of the button to close current window on click.
     */
    public void initialize() {
        mainContainer.getChildren().addAll();
    }

}
