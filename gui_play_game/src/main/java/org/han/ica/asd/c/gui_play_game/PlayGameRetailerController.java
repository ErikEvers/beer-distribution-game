package org.han.ica.asd.c.gui_play_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PlayGameRetailerController extends PlayGame {

    /**
     * First code being executed.
     */
    public void initialize() {
        superInitialize();
        orderList.setItems(orderFacilities);
    }

    /**
     * Button event handling the order sending.
     */
    @Override
    public void handleSendOrderButtonClick() {
        super.handleSendOrderButtonClick();
    }

    @Override
    public void submitTurnButtonClicked() {
        super.submitTurnButtonClicked();
    }

    /**
     * Fills up the comboBox with facilities where the current facility can order.
     */
    @Override
    public void fillComboBox(){
        fillOutGoingOrderFacilityComboBox(comboBox);
    }

    @Override
    public void refreshInterfaceWithCurrentStatus(int roundId) {
			super.refreshInterfaceWithCurrentStatus(roundId);
			fillComboBox();
    }
}
