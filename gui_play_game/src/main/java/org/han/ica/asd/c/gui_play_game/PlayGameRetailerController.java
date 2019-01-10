package org.han.ica.asd.c.gui_play_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;
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

        roundNumber = 0;
    }

    /**
     * Button event handling the order sending.
     */
    public void handleSendOrderButtonClick() {
        super.handleSendOrderButtonClick();
    }

    public void submitTurnButonClicked(MouseEvent mouseEvent) {
        super.submitTurnButonClicked();
    }

    /**
     * Fills up the comboBox with facilities where the current facility can order.
     */
    @Override
    public void fillComboBox(){
        fillOutGoingOrderFacilityComboBox(comboBox);
    }
}
