package org.han.ica.asd.c.gui_play_game;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;

public class PlayGameFactoryController {
    @FXML
    GridPane playGridPane;

    @FXML
    AnchorPane mainContainer;

    @Inject @Named("SeeOtherFacilities")
    IGUIHandler seeOtherFacilities;

    public void initialize(){
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");

    }

    public void handleSendOrderButtonClick() {

    }

    public void seeOtherFacilitiesButtonClicked() {
        seeOtherFacilities.setupScreen();
    }
}
