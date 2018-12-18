package org.han.ica.asd.c.gui_replay_game;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

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

    private int currentRound;
    private int totalRounds;
    private TextFormatter textFormatter;
    private ReplayData replayData;

    @FXML
    public void currentRoundEntered(ActionEvent event) {
        int currentRoundInput = Integer.parseInt(currentRoundTextfield.getText());

        if (currentRoundInput > totalRounds) {
            currentRound = totalRounds;
        } else if (currentRoundInput < 1) {
            currentRound = 1;
        } else {
            currentRound = currentRoundInput;
        }
        updateCurrentRound();
        drawGraph();
    }

    @FXML
    public void nextRoundButtonClicked(ActionEvent event) {
        if (currentRound < totalRounds) {
            this.currentRound++;
            updateCurrentRound();
            drawGraph();
        }
    }

    @FXML
    public void prevRoundButtonClicked(ActionEvent event) {
        if (currentRound > 1) {
            this.currentRound--;
            updateCurrentRound();
            drawGraph();
        }
    }

    @FXML
    void initialize() {
        this.replayData = new ReplayData();

        this.setuptextFormatter();
        currentRoundTextfield.setTextFormatter(textFormatter);

        currentRound = 1;
        totalRounds = replayData.getHighestRound();
        totalRoundsTextfield.setText(totalRounds + "");

        insertComboBox();
        updateCurrentRound();
        drawGraph();
    }

    private void insertComboBox() {
        facilityCombobox.getItems().clear();

        ObservableList<Facility> observableList = FXCollections.observableArrayList(replayData.getAllFacilities());

        facilityCombobox.setItems(observableList);
    }

    private void updateCurrentRound() {
        currentRoundTextfield.setText(currentRound + "");
    }

    private void drawGraph() {
        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
        LineChart.Series<Double, Double> series1 = new LineChart.Series<>();
        series1.setName("TestData");
        for (int i = 0; i <= currentRound; i++) {
            series1.getData().add(new XYChart.Data<>(new Double(i), new Double(i)));
        }
        lineChartData.add(series1);
        replayGraph.setData(lineChartData);
    }

    private void setuptextFormatter() {
        textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            sanitizeInput(change);

            return change;
        });
    }

    private void sanitizeInput(TextFormatter.Change change) {
        String text = change.getControlNewText();
        if (!(text.matches("[0-9]+"))) {
            change.setText(change.getText().replaceAll("[^\\d.]", ""));
        }
    }
}

