package org.han.ica.asd.c.interfaces.gui_replay_game;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.ArrayList;

public interface IVisualisedPlayedGameData {
    String getTotalRoundsString();

    String getCurrentRoundString();

    boolean incrementCurrentRound();

    boolean decrementCurrentRound();

    void updateCurrentRound(int round);

    ArrayList<Facility> getAllFacilities();

    ObservableList<XYChart.Series<Double, Double>> getChartData();

    void removeDisplayedFacility(GameValue facility);

    void addDisplayedFacility(GameValue facility);

    void addDisplayedFacility(Facility facility);

    void removeDisplayedFacility(Facility facility);

    void setAttribute(GameValue value);
}
