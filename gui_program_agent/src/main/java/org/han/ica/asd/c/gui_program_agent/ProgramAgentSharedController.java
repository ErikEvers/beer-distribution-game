package org.han.ica.asd.c.gui_program_agent;

import javafx.scene.control.Button;
import javafx.stage.Stage;

interface  ProgramAgentSharedController {

    /***
     * Sets the close action of a button. It closes the current stage.
     * @param close The button that should get the action.
     */
     default void setCloseStageOnButton(Button close){
        close.setOnAction(event -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
    }

}
