package org.han.ica.asd.c.gui_play_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.model.domain_objects.Facility;

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
        int order = Integer.parseInt(outgoingOrderTextField.getText());
        playerComponent.placeOrder(null, order);
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

    public void submitTurnButtonClicked(MouseEvent mouseEvent) {
        int step2Value = 0;

        if (!outgoingOrderTextField.getText().trim().isEmpty()) {
            if (!step2TextField.getText().trim().isEmpty()) {
                step2Value = Integer.parseInt(step2TextField.getText());
            }
            if (!step1TextField.getText().trim().isEmpty()) {
                step2TextField.setText(step1TextField.getText());
            }

            step1TextField.setText(outgoingOrderTextField.getText());
            outgoingOrderTextField.clear();

        }

        super.submitTurnButtonClicked();
    }

    @Override
    public void refreshInterfaceWithCurrentStatus(int roundId) {
        super.refreshInterfaceWithCurrentStatus(roundId);
        fillComboBox();
    }
}
