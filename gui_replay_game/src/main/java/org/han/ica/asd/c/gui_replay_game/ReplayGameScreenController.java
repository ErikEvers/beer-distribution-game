package org.han.ica.asd.c.gui_replay_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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


    public void setGameId(String gameId){
    //TODO GET GAME VALUES.
    }

    @FXML
    public void prevRoundButtonClicked(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }
}

