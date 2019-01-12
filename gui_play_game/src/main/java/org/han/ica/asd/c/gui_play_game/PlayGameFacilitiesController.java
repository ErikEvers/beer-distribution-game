package org.han.ica.asd.c.gui_play_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;

public class PlayGameFacilitiesController extends PlayGame {
    @FXML
    public ListView<String> deliverList;
    @FXML
    public ListView<String> orderList;
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
     * Button event handling the order sending.
     */
    public void handleSendOrderButtonClick() {
        super.handleSendOrderButtonClick();
    }

    public void handleSeeActivityLogButtonClicked() {
        super.handleSeeActivityLogButtonClicked();
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


    public void submitTurnButtonClicked(MouseEvent mouseEvent) {
        super.submitTurnButtonClicked();
    }

    @Override
    public void refreshInterfaceWithCurrentStatus(int roundId) {
        super.refreshInterfaceWithCurrentStatus(roundId);
        fillComboBox();
    }
}
