package org.han.ica.asd.c.gui_main_menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainMenuController {

    @FXML
    private Button close;

    @Inject
    @Named("GameSetupStart")
    private IGUIHandler gameSetupStart;

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
    @Named("AssignAgents")
    private IGUIHandler assignAgents;

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

    @FXML
    public void handleCreateGameButtonClick() {
        gameSetupStart.setupScreen();
        //connector.start();
        //connector.createRoom("12345", "");
    }

    @FXML
    public void handleJoinGameButtonClick() {
        joinGame.setupScreen();
    }

}
