package org.han.ica.asd.c.gui_play_game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import static org.han.ica.asd.c.fxml_helper.NumberInputFormatter.getChangeUnaryOperator;

public class PlayGameFacilitiesController extends PlayGame {

    @FXML
    private Label lblFacilities;


    /**
     * First code being executed.
     */
    public void initialize() {
        superInitialize();

        txtOutgoingDelivery.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, getChangeUnaryOperator()));
        deliverList.setItems(deliverFacilities);
        orderList.setItems(orderFacilities);
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

    @Override
    public synchronized void refreshInterfaceWithCurrentStatus(int previousRoundId, int roundId, boolean gameEnded) {
        super.refreshInterfaceWithCurrentStatus(previousRoundId, roundId, gameEnded);
        fillComboBox();
    }
}
