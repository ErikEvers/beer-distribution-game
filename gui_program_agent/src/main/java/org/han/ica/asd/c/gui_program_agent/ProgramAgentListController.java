package org.han.ica.asd.c.gui_program_agent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;

public class ProgramAgentListController {
    @FXML
    AnchorPane mainContainer;

    @FXML
    Button programAgent;

    @FXML
    Button back;

    @Inject
    @Named("ProgramAgent")
    IGUIHandler programAgent1;

    @Inject
    @Named("MainMenu")
    IGUIHandler mainMenu;

    public void initialize() {
        mainContainer.getChildren().addAll();
        setProgramAgentButtonAction();
        setBackButtonAction();
    }

    private void setBackButtonAction() {
        back.setOnAction(event -> mainMenu.setupScreen());
    }

    private void setProgramAgentButtonAction() {
        programAgent.setOnAction(event -> programAgent1.setupScreen());
    }
}
