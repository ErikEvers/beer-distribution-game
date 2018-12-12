package org.han.ica.asd.c;

//*

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

//
public abstract class PlayGame {
    @FXML
    protected GridPane playGridPane;

    @FXML
    protected AnchorPane mainContainer;

    protected void superInitialize() {
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");
    }
}
