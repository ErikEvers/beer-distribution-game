package org.han.ica.asd.c;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.han.ica.asd.c.model.domain_objects.Facility;

public class PlayGameFacilitiesController extends PlayGame {
    @FXML
    private Label lblFacilities;

    @FXML
    private TextField incomingGoodsNextRound;

    @FXML
    protected ComboBox<Facility> comboBox;

    private ObservableList<Facility> facilityListView = FXCollections.observableArrayList();

    /**
     * First code being executed.
     */
    public void initialize() {
        superInitialize();

        //TODO when the facility object is saved in the player model get the facility name and set in in this label.
        //lblFacilities.setText(facilityFake.toString());
        roundNumber = 0;
        fillComboBox();
    }

    /**
     * Button event handling the order sending.
     */
    public void handleSendOrderButtonClick() {
        if (!outgoingOrderTextField.getText().isEmpty()) {
            int order = Integer.parseInt(outgoingOrderTextField.getText());

            if (order < 0) {
                outgoingOrderTextField.setText("");
                return;
            }

            if (!incomingGoodsNextRound.getText().isEmpty() && !outgoingGoodsNextRound.getText().isEmpty()) {
                int incomingGoodsNextRoundAmount = Integer.parseInt(incomingGoodsNextRound.getText());
                int outgoingGoodsNextRoundAmount = Integer.parseInt(outgoingGoodsNextRound.getText());

                //TODO get the real calculation result from the game logic component/from the game leader.
                inventory.setText(calculateInventory(incomingGoodsNextRoundAmount, outgoingGoodsNextRoundAmount));
            }

            Facility facility = comboBox.getValue();
            playerComponent.placeOrder(facility, order);

            //TODO get the real order from the facility ordering from this one.
            outgoingGoodsNextRound.setText(Integer.toString(orderFake.orders()[roundNumber]));
            roundNumber++;

            incomingGoodsNextRound.setText(outgoingOrderTextField.getText());

            outgoingOrderTextField.setText("");
        }
    }

    /**
     * Fills up the comboBox with facilities where the current facility can order.
     */
    private void fillComboBox(){
        //TODO Get the facilities linked to the facility currently being played by the player.
        Facility warehouse = new Facility();
        Facility warehouse2 = new Facility();
        facilityListView.add(warehouse);
        facilityListView.add(warehouse2);
        comboBox.setItems(facilityListView);
    }
}
