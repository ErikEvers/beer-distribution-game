package org.han.ica.asd.c.gui_program_agent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


public class ProgramAgentInfoController implements ProgramAgentSharedController {
    @FXML
    AnchorPane mainContainer;

    @FXML
    Button close;

    /***
     * Function for initialising the current ProgramAgentInfo FXML. It also sets the actions of the button to close current window on click.
     */
    public void initialize() {
        mainContainer.getChildren().addAll();
        setCloseStageOnButton(close);
    }
}
