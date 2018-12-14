package org.han.ica.asd.c.gui_play_game;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class PlayGameFactoryController {
    @FXML
    GridPane playGridPane;

    @FXML
    AnchorPane mainContainer;

    public void initialize(){
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");

    }
}
