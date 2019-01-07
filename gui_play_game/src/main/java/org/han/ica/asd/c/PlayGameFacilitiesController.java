package org.han.ica.asd.c;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.han.ica.asd.c.model.domain_objects.Facility;

import javax.inject.Inject;

public class PlayGameFacilitiesController extends PlayGame {
    @FXML
    private Label lblFacilities;

    @FXML
    private TextField incomingGoodsNextRound;

    @FXML
    protected ComboBox<FacilityFake> comboBox;

    private ObservableList<FacilityFake> facilityListView = FXCollections.observableArrayList();


    public void initialize() {
        superInitialize();

        lblFacilities.setText(facilityFake.toString());
        roundNumber = 0;
        fillComboBox();
    }

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

            //TODO uncomment this when playerComponent place order and gamelogic place order is implemented.
            /* Facility facility = comboBox.getValue();
            playerComponent.placeOrder(facility, order);*/


            //TODO get the real outgoing Order from the facility ordering from this one.
            outgoingGoodsNextRound.setText(Integer.toString(orderFake.orders()[roundNumber]));
            roundNumber++;

            incomingGoodsNextRound.setText(outgoingOrderTextField.getText());

            outgoingOrderTextField.setText("");
        }
    }

    private void fillComboBox(){
        FacilityFake warehouse = new FacilityFake();
        FacilityFake warehouse2 = new FacilityFake();
        facilityListView.add(warehouse);
        facilityListView.add(warehouse2);
        comboBox.setItems(facilityListView);
    }
}
