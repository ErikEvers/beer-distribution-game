package org.han.ica.asd.c;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class SpelSpelenFabriekController {
    @FXML
    GridPane playGridPane;

    @FXML
    AnchorPane mainContainer;

    @FXML
    TextField incomingOrderTextField;

    @FXML
    TextField step1TextField;

    @FXML
    TextField step2TextField;

    public void initialize(){
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");
        incomingOrderTextField.setText("50");
    }
}
