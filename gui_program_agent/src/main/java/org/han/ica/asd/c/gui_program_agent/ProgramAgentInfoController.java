package org.han.ica.asd.c.gui_program_agent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ProgramAgentInfoController {
    @FXML
    AnchorPane mainContainer;

    @FXML
    Button close;

    public void initialize() {
        mainContainer.getChildren().addAll();
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    Stage stage = (Stage) close.getScene().getWindow();
                    stage.close();
            }
        });
    }
}
