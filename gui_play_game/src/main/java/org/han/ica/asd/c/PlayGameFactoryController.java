package org.han.ica.asd.c;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

public class PlayGameFactoryController extends PlayGame {
    @FXML
    private TextField step1TextField;

    @FXML
    private TextField step2TextField;

    public void initialize() {
        superInitialize();
    }

    public void handleSendOrderButtonClick() {
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

            inventory.setText(calculateInventory(step2Value, Integer.parseInt(outgoingGoodsNextRound.getText())));
            step1TextField.setText(outgoingOrderTextField.getText());
            outgoingOrderTextField.clear();
        }
    }
}
