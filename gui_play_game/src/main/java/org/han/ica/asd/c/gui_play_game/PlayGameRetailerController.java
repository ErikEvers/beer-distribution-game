package org.han.ica.asd.c.gui_play_game;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;

public class PlayGameRetailerController extends PlayGame {
    @FXML
    private TextField incomingGoodsNextRound;

    @FXML
    private ComboBox<Facility> comboBox;

    /**
     * First code being executed.
     */
    public void initialize() {
        superInitialize();
    }

    /**
     * Button event handling the order sending.
     */
    public void handleSendOrderButtonClick() {
        super.handleSendOrderButtonClick();
    }

    public void submitTurnButtonClicked(MouseEvent mouseEvent) {
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
