package org.han.ica.asd.c.gui_main_menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;


public class MainMenuController {

    @FXML
    private Button close;

    @Inject
    @Named("ProgramAgentList")
    private IGUIHandler programAgentList;

    @Inject
    @Named("ReplayGameList")
    private IGUIHandler replayGameList;

    @Inject
    @Named("JoinGame")
    private IGUIHandler joinGame;

    public void initialize() {

    }
    @FXML
    private void closeButtonAction() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void programAgentButtonAction() {
        programAgentList.setupScreen();
    }

    @FXML
    private void replayButtonAction() {
         replayGameList.setupScreen();
    }

    public void handleJoinGameButtonClick(){
        joinGame.setupScreen();
    }

}
