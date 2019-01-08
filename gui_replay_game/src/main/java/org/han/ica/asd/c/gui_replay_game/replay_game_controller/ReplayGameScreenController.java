package org.han.ica.asd.c.gui_replay_game.replay_game_controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.han.ica.asd.c.gui_replay_game.replay_data.ReplayData;
import org.han.ica.asd.c.model.domain_objects.Facility;

public class ReplayGameScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private LineChart<Double, Double> replayGraph;

    @FXML
    private CheckBox factoryCheckBox;

    @FXML
    private CheckBox warehouseCheckBox;

    @FXML
    private CheckBox wholesaleCheckBox;

    @FXML
    private CheckBox retailCheckBox;

    @FXML
    private ComboBox<Facility> facilityComboBox;

    @FXML
    private Button prevRoundButton;

    @FXML
    private Button nextRoundButton;

    @FXML
    private TextField currentRoundTextfield;

    @FXML
    private TextField totalRoundsTextfield;

    @FXML
    private Button backButton;

    private ReplayData replayData;

    @FXML
    public void currentRoundEntered(ActionEvent event) {
        replayData.updateCurrentRound(Integer.parseInt(currentRoundTextfield.getText()));

        updateCurrentRound();
        drawGraph();
    }

    @FXML
    public void nextRoundButtonClicked(ActionEvent event) {
        if (replayData.incrementCurrentRound()) {
            updateCurrentRound();
            drawGraph();
        }
    }

    @FXML
    public void prevRoundButtonClicked(ActionEvent event) {
        if (replayData.decrementCurrentRound()) {
            updateCurrentRound();
            drawGraph();
        }
    }

    @FXML
    void initialize() {
        this.replayData = new ReplayData();

        currentRoundTextfield.setTextFormatter(NumericTextFormatter.getTextFormatter());

        totalRoundsTextfield.setText(replayData.getTotalRoundsString());

        initializeComboBox();
        initializeCheckBoxes();
        updateCurrentRound();
        drawGraph();
    }

    private void initializeComboBox() {
        facilityComboBox.getItems().clear();

        ObservableList<Facility> observableList = FXCollections.observableArrayList(replayData.getAllFacilities());

        facilityComboBox.setItems(observableList);

        facilityComboBox.valueProperty().addListener((obs, oldVal, newVal) ->
                facilityComboBoxUpdated(oldVal, newVal));

        facilityComboBox.setConverter(new StringConverter<Facility>() {
            @Override
            public String toString(Facility object) {
                return object.getFacilityType().getFacilityName() + " " + object.getFacilityId();
            }

            @Override
            public Facility fromString(String string) {
                return null;
            }
        });
    }

    private void facilityComboBoxUpdated(Facility facilityOld, Facility facilityNew) {
        replayData.removeDisplayedFacility(facilityOld);
        if (facilityNew != null) {
            clearCheckBoxes();
            replayData.addDisplayedFacility(facilityNew);
        }
        drawGraph();
    }

    private void clearCheckBoxes() {
        factoryCheckBox.selectedProperty().setValue(false);
        retailCheckBox.selectedProperty().setValue(false);
        warehouseCheckBox.selectedProperty().setValue(false);
        wholesaleCheckBox.selectedProperty().setValue(false);
    }

    private void clearComboBox() {
        facilityComboBox.getSelectionModel().clearSelection();
    }

    public void initializeCheckBoxes() {
        factoryCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                factoryCheckBoxUpdated(newValue));
        retailCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                retailCheckBoxUpdated(newValue));
        warehouseCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                warehouseCheckBoxUpdated(newValue));
        wholesaleCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                wholesaleCheckBoxUpdated(newValue));
    }

    private void wholesaleCheckBoxUpdated(Boolean newValue) {
        facilityCheckBoxUpdated(newValue, 2);
    }

    private void warehouseCheckBoxUpdated(Boolean newValue) {
        facilityCheckBoxUpdated(newValue, 4);
    }

    private void retailCheckBoxUpdated(Boolean newValue) {
        facilityCheckBoxUpdated(newValue, 3);
    }

    private void factoryCheckBoxUpdated(Boolean newValue) {
        facilityCheckBoxUpdated(newValue, 1);
    }

    private void facilityCheckBoxUpdated(Boolean newValue, int id) {
        if (!newValue) {
            replayData.removeDisplayedFacility(id);
        } else {
            clearComboBox();
            replayData.addDisplayedFacility(id);
        }
        drawGraph();
    }

    private void updateCurrentRound() {
        currentRoundTextfield.setText(replayData.getCurrentRoundString());
    }

    private void drawGraph() {
        replayGraph.setData(replayData.getChartData());
    }

    public void setGameId(String gameId){
        //TODO GET GAME VALUES.
    }
}

