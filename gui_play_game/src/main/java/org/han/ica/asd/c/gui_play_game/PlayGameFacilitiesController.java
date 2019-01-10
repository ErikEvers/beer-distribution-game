package org.han.ica.asd.c.gui_play_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;

public class PlayGameFacilitiesController extends PlayGame {
    @FXML
    private Label lblFacilities;


    /**
     * First code being executed.
     */
    public void initialize() {
        superInitialize();

        roundNumber = 0;

        txtOutgoingDelivery.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, getChangeUnaryOperator()));
    }

    /**
     * Button event handling the order sending.
     */
    public void handleSendOrderButtonClick() {
        super.handleSendOrderButtonClick();
    }

    /**
     * Fills up the comboBox with facilities where the current facility can order.
     */
    @Override
    public void fillComboBox(){
        fillOutGoingOrderFacilityComboBox(comboBox);
        fillOutGoingDeliveryFacilityComboBox(cmbChooseOutgoingDelivery);
    }

    public void setLblFacilitiesText(String facilityName) {
        lblFacilities.setText(facilityName);
    }

    /**
     * Button event handling the order delivering.
     */
    public void handleSendDeliveryButtonClick(ActionEvent actionEvent) {
        super.handleSendDeliveryButtonClick();
    }


    public void submitTurnButonClicked(MouseEvent mouseEvent) {
        super.submitTurnButonClicked();
    }

    @Override
    public void refreshInterfaceWithCurrentStatus(BeerGame beerGame) {
        
    }
}
