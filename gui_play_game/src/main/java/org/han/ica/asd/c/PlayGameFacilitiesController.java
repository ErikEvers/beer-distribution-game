package org.han.ica.asd.c;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlayGameFacilitiesController extends PlayGame {
    @FXML
    private Label lblFacilities;

    public void initialize(){
        super.superInitialize();

//        lblFacilities.setText(playerComponent.getFacilityName());
    }
}
