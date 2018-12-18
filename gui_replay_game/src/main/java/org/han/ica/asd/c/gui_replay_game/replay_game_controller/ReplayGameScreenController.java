package org.han.ica.asd.c.gui_replay_game.replay_game_controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.han.ica.asd.c.gui_replay_game.replay_data.ReplayData;
import org.han.ica.asd.c.model.Facility;

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
                facilityComboBoxUpdated(newVal));

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

    private void facilityComboBoxUpdated(Facility facility) {
        clearCheckBoxes();
    }

    private void clearCheckBoxes(){
        factoryCheckBox.selectedProperty().setValue(false);
        retailCheckBox.selectedProperty().setValue(false);
        warehouseCheckBox.selectedProperty().setValue(false);
        wholesaleCheckBox.selectedProperty().setValue(false);
    }

    private void clearComboBox(){

    }

    public void initializeCheckBoxes(){
        factoryCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            factoryCheckBoxUpdated(newValue);
        });
    }

    private void factoryCheckBoxUpdated(Boolean newValue) {

    }

    private void updateCurrentRound() {
        currentRoundTextfield.setText(replayData.getCurrentRoundString());
    }

    private void drawGraph() {
        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
        LineChart.Series<Double, Double> series1 = new LineChart.Series<>();
        series1.setName("TestData");
        for (int i = 0; i <= replayData.getCurrentRound(); i++) {
            series1.getData().add(new XYChart.Data<>(new Double(i), new Double(i)));
        }
        lineChartData.add(series1);
        replayGraph.setData(lineChartData);
    }
}

