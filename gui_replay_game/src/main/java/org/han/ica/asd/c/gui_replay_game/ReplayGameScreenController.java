package org.han.ica.asd.c.gui_replay_game;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

public class ReplayGameScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AreaChart<String, Number> replayGameGraph;

    @FXML
    private CategoryAxis xAxisGraph;

    @FXML
    private NumberAxis yAxisGraph;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    void initialize() {
        assert replayGameGraph != null : "fx:id=\"replayGameGraph\" was not injected: check your FXML file 'ReplayGameScreen.fxml'.";
        assert xAxisGraph != null : "fx:id=\"xAxisGraph\" was not injected: check your FXML file 'ReplayGameScreen.fxml'.";
        assert yAxisGraph != null : "fx:id=\"yAxisGraph\" was not injected: check your FXML file 'ReplayGameScreen.fxml'.";
        assert previousButton != null : "fx:id=\"previousButton\" was not injected: check your FXML file 'ReplayGameScreen.fxml'.";
        assert nextButton != null : "fx:id=\"nextButton\" was not injected: check your FXML file 'ReplayGameScreen.fxml'.";

        xAxisGraph.setLabel("Rounds");
        yAxisGraph.setLabel("Budget");

        XYChart.Series<String, Number> series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data( "1", 80));
        replayGameGraph.getData().add(series1);
    }
}

