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

public class PlayGameFactoryController {
    private ObservableList<FacilityFake> facilityListView = FXCollections.observableArrayList();

    @FXML
    private GridPane playGridPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private TextField incomingOrderTextField;

    @FXML
    private TextField step1TextField;

    @FXML
    private TextField step2TextField;

    @FXML
    private TextField outgoingOrderTextField;

    @FXML
    private Label inventory;

    @FXML
    private ComboBox<FacilityFake> comboBox;

    public void initialize() {
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");
        incomingOrderTextField.setText("50");

        UnaryOperator<TextFormatter.Change> textFieldFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")){
                return change;
            }
            return null;
        };

        outgoingOrderTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, textFieldFilter));
        fillComboBox();
    }



    public void handleSendOrderButtonClick() {
        if (!outgoingOrderTextField.getText().trim().isEmpty()) {
            if (!step2TextField.getText().trim().isEmpty()) {
                inventory.setText(calculateInventory());
            }
            if (!step1TextField.getText().trim().isEmpty()) {
                step2TextField.setText(step1TextField.getText());
            }
            step1TextField.setText(outgoingOrderTextField.getText());
            outgoingOrderTextField.clear();

        }
    }

    private String calculateInventory(){
        int inventoryValue = Integer.parseInt(inventory.getText());
        int step2Value = Integer.parseInt(step2TextField.getText());
        int result = inventoryValue + step2Value;

        return Integer.toString(result);
    }

    private void fillComboBox(){
        FacilityFake warehouse = new FacilityFake();
        FacilityFake warehouse2 = new FacilityFake();
        facilityListView.add(warehouse);
        facilityListView.add(warehouse2);
        comboBox.setItems(facilityListView);
    }
}
