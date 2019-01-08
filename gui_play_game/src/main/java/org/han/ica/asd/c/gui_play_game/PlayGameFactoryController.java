package org.han.ica.asd.c.gui_play_game;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;
import javax.inject.Named;

public class PlayGameFactoryController {
    private Configuration configuration;

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
        // implementation coming
    }

    public void seeOtherFacilitiesButtonClicked() {
        seeOtherFacilities.setData(new Object[]{configuration});
        seeOtherFacilities.setupScreen();
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
