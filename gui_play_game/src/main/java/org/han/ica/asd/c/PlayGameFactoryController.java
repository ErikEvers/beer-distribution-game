package org.han.ica.asd.c;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PlayGameFactoryController extends PlayGame {
    @FXML
    private TextField step1TextField;

    @FXML
    private TextField step2TextField;

    /**
     * Button event handling the order sending.
     */
    public void initialize() {
        superInitialize();
    }

    /**
     * Button event handling the order sending.
     */
    public void handleSendOrderButtonClick() {
        //TODO get the real order from the facility ordering from this one.
        outgoingGoodsNextRound.setText(Integer.toString(orderFake.orders()[roundNumber]));
        roundNumber++;
        int step2Value = 0;

        if (!outgoingOrderTextField.getText().trim().isEmpty()) {
            if (!step2TextField.getText().trim().isEmpty()) {
                step2Value = Integer.parseInt(step2TextField.getText());
            }
            if (!step1TextField.getText().trim().isEmpty()) {
                step2TextField.setText(step1TextField.getText());
            }

            //TODO get the real calculation result from the game logic component/from the game leader.
            inventory.setText(calculateInventory(step2Value, Integer.parseInt(outgoingGoodsNextRound.getText())));
            step1TextField.setText(outgoingOrderTextField.getText());
            outgoingOrderTextField.clear();

            int order = Integer.parseInt(outgoingOrderTextField.getText());
            playerComponent.placeOrder(null, order);
        }
    }
}
