package org.han.ica.asd.c.gui_replay_game;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ReplayGameScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private LineChart<?, ?> replayGraph;

    @FXML
    private CheckBox factoryCheckbox;

    @FXML
    private CheckBox warehouseCheckBox;

    @FXML
    private CheckBox wholesaleCheckbox;

    @FXML
    private CheckBox retailCheckbox;

    @FXML
    private ComboBox<?> facilityCombobox;

    @FXML
    private Button prevRoundButton;

    @FXML
    private Button nextRoundButton;

    @FXML
    private TextField currentRoundTextfield;

    @FXML
    private TextField totalRoundsTextfield;

    @FXML
    public void currentRoundEntered(ActionEvent event) {

    }

    @FXML
    public void nextRoundButtonClicked(ActionEvent event) {

    }

    @FXML
    public void prevRoundButtonClicked(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert replayGraph != null : "fx:id=\"replayGraph\" was not injected: check your FXML file 'Untitled'.";
        assert factoryCheckbox != null : "fx:id=\"factoryCheckbox\" was not injected: check your FXML file 'Untitled'.";
        assert warehouseCheckBox != null : "fx:id=\"warehouseCheckBox\" was not injected: check your FXML file 'Untitled'.";
        assert wholesaleCheckbox != null : "fx:id=\"wholesaleCheckbox\" was not injected: check your FXML file 'Untitled'.";
        assert retailCheckbox != null : "fx:id=\"retailCheckbox\" was not injected: check your FXML file 'Untitled'.";
        assert facilityCombobox != null : "fx:id=\"facilityCombobox\" was not injected: check your FXML file 'Untitled'.";
        assert prevRoundButton != null : "fx:id=\"prevRoundButton\" was not injected: check your FXML file 'Untitled'.";
        assert nextRoundButton != null : "fx:id=\"nextRoundButton\" was not injected: check your FXML file 'Untitled'.";
        assert currentRoundTextfield != null : "fx:id=\"currentRoundTextfield\" was not injected: check your FXML file 'Untitled'.";
        assert totalRoundsTextfield != null : "fx:id=\"totalRoundsTextfield\" was not injected: check your FXML file 'Untitled'.";

    }
}

