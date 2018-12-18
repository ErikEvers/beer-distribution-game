package org.han.ica.asd.c.gui_replay_game.replay_game_controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

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
    private CheckBox factoryCheckbox;

    @FXML
    private CheckBox warehouseCheckBox;

    @FXML
    private CheckBox wholesaleCheckbox;

    @FXML
    private CheckBox retailCheckbox;

    @FXML
    private ComboBox<Facility> facilityCombobox;

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
        updateCurrentRound();
        drawGraph();
    }

    private void initializeComboBox() {
        facilityCombobox.getItems().clear();

        ObservableList<Facility> observableList = FXCollections.observableArrayList(replayData.getAllFacilities());

        facilityCombobox.setItems(observableList);

        facilityCombobox.valueProperty().addListener((obs, oldVal, newVal) ->
                //hier komt een functie die het afhandeld, voor nu een println
                System.out.println("The ID of Facility: " + newVal.getFacilityType().getFacilityName() + " is : " + newVal.getFacilityId()));

        facilityCombobox.setConverter(new StringConverter<Facility>() {
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

