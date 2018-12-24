package org.han.ica.asd.c.gui_join_game;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;

public class AgentListController {

    @FXML
    private AnchorPane mainContainer;

    @Inject
    @Named("MainMenu")
    private IGUIHandler mainMenu;

    @Inject
    @Named("ChooseAgent")
    private IGUIHandler chooseAgent;

    public void initialize(){
        mainContainer.getChildren().addAll();
    }

    public void handleBackToMenuButtonClick() {
        mainMenu.setupScreen();
    }

    public void handleChooseAgentButtonClick(){
        chooseAgent.setupScreen();
    }
}
