package org.han.ica.asd.c.gui_main_menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;

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

    @Inject
    private IConnectorForSetup connector;

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

    public void handleCreateGameButtonClick() {
			connector.start();
			connector.createRoom("12345", "");
		}

    public void handleJoinGameButtonClick(){
        joinGame.setupScreen();
    }

}
