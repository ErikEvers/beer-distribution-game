package org.han.ica.asd.c.gui_play_game;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.model.domain_objects.Facility;

import static org.han.ica.asd.c.fxml_helper.NumberInputFormatter.getChangeUnaryOperator;

public class PlayGameFactoryController extends PlayGame {

    @FXML
    private TextField step1TextField;

    @FXML
    private TextField step2TextField;

    @FXML
    protected TextField txtOutgoingDelivery;
    /**
     * Button event handling the order sending.
     */
    public void initialize() {
        superInitialize();

        txtOutgoingDelivery.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, getChangeUnaryOperator()));
        deliverList.setItems(deliverFacilities);
        orderList.setItems(orderFacilities);
    }

    @FXML
    protected void handleSendDeliveryButtonClick() {
        if (!txtOutgoingDelivery.getText().isEmpty()) {
            playerComponent.sendDelivery(cmbChooseOutgoingDelivery.getValue(), Integer.parseInt(txtOutgoingDelivery.getText()));
            refillDeliveriesList();
            txtOutgoingDelivery.clear();
        }
    }

    /**
     * Button event handling the order sending.
     */
    @Override
    public void handleSendOrderButtonClick() {
        int order = Integer.parseInt(outgoingOrderTextField.getText());
        playerComponent.placeOrder(playerComponent.getPlayer().getFacility(), order);
        refillOrdersList();
    }

    protected void refillOrdersList() {
        orderFacilities.clear();
        Facility ownFacility = playerComponent.getPlayer().getFacility();

        playerComponent.getRound().getFacilityOrders().stream().filter(order -> order.getFacilityId() == ownFacility.getFacilityId()).forEach(order ->
                orderFacilities.add(addProduceOrderToList(order.getOrderAmount())));
    }

    private String addProduceOrderToList(int amount) {
        return "To produce: " + Integer.toString(amount);
    }

    @Override
    public void fillComboBox() {
        fillOutGoingDeliveryFacilityComboBox(cmbChooseOutgoingDelivery);
    }

    @Override
    public void submitTurnButtonClicked() {
//        int step2Value = 0;
//
//        if (!outgoingOrderTextField.getText().trim().isEmpty()) {
//            if (!step2TextField.getText().trim().isEmpty()) {
//                step2Value = Integer.parseInt(step2TextField.getText());
//            }
//            if (!step1TextField.getText().trim().isEmpty()) {
//                step2TextField.setText(step1TextField.getText());
//            }
//
//            step1TextField.setText(outgoingOrderTextField.getText());
//            outgoingOrderTextField.clear();
//
//        }

        super.submitTurnButtonClicked();
    }

    @Override
    public synchronized void refreshInterfaceWithCurrentStatus(int previousRoundId, int roundId, boolean gameEnded) {
        super.refreshInterfaceWithCurrentStatus(previousRoundId, roundId, gameEnded);
        fillComboBox();
    }
}
