package org.han.ica.asd.c.gui_play_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.player.PlayerComponent;

import java.util.ArrayList;
import java.util.List;

public class PlayGameFactoryController extends PlayGame {

    @FXML
    private TextField step1TextField;

    @FXML
    private TextField step2TextField;

    @FXML
    private ComboBox<Facility> cmbChooseOutgoingDelivery;

    @FXML
    private TextField txtOutgoingDelivery;

    /**
     * Button event handling the order sending.
     */
    public void initialize() {
        superInitialize();

        txtOutgoingDelivery.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, getChangeUnaryOperator()));
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

    @Override
    public void fillComboBox() {
        fillOutGoingDeliveryFacilityComboBox(cmbChooseOutgoingDelivery);
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
}
