package org.han.ica.asd.c.gui_main_menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

    @Inject @Named("ProgramAgentList")
    private IGUIHandler programAgentList;

    @Inject @Named("ReplayGame")
    private IGUIHandler replayGame;

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
        createAgent.setOnAction(event -> {
            try {
                programAgentList.setupScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setReplayButtonAction() {
        replay.setOnAction(event -> {
            try {
                replayGame.setupScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
