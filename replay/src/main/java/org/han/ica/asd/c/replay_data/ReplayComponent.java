package org.han.ica.asd.c.replay_data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.interfaces.gui_replay_game.IVisualisedPlayedGameData;
import org.han.ica.asd.c.replay_data.fakes.AverageRoundFake;
import org.han.ica.asd.c.replay_data.fakes.RoundFake;
import org.han.ica.asd.c.replay_data.fakes.RoundsFake;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplayComponent implements IVisualisedPlayedGameData {
    private static final int LOWEST_ROUND_POSSIBLE = 0;
    private static final int FIRST_ROUND_TO_DISPLAY = 1;
    private List<RoundFake> rounds;
    private List<Facility> displayedFacilities;
    private int currentRound;
    private int totalRounds;
    private GameValue attribute;
    private List<GameValue> displayedAverages;
    private List<AverageRoundFake> averageRounds;

    public ReplayComponent() {
        displayedFacilities = new ArrayList<>();
        displayedFacilities.add(getAllFacilities().get(0));
        displayedFacilities.add(getAllFacilities().get(1));
        displayedFacilities.add(getAllFacilities().get(2));
        displayedFacilities.add(getAllFacilities().get(3));

        RoundsFake roundsFake = new RoundsFake();
        rounds = roundsFake.getRounds();

        displayedAverages = new ArrayList<>();
        averageRounds = new ArrayList<>();

        currentRound = FIRST_ROUND_TO_DISPLAY;

        for (int i = 0; i < 10; i++) {
            averageRounds.add(new AverageRoundFake(i, GameValue.FACTORY, 1, 2, 3, 4, 5));
        }
        for (int i = 0; i < 10; i++) {
            averageRounds.add(new AverageRoundFake(i, GameValue.WHOLESALER, 6, 7, 8, 9, 10));
        }
        for (int i = 0; i < 10; i++) {
            averageRounds.add(new AverageRoundFake(i, GameValue.RETAILER, 11, 12, 13, 14, 15));
        }
        for (int i = 0; i < 10; i++) {
            averageRounds.add(new AverageRoundFake(i, GameValue.REGIONALWAREHOUSE, 16, 17, 18, 19, 26));
        }

        totalRounds = 9;
    }

    public String getTotalRoundsString() {
        return String.valueOf(totalRounds);
    }

    public String getCurrentRoundString() {
        return String.valueOf(currentRound);
    }

    public boolean incrementCurrentRound() {
        if (currentRound < totalRounds) {
            this.currentRound++;
            return true;
        }
        return false;
    }

    public boolean decrementCurrentRound() {
        if (currentRound > LOWEST_ROUND_POSSIBLE) {
            this.currentRound--;
            return true;
        }
        return false;
    }

    public void updateCurrentRound(int round) {
        if (round > totalRounds) currentRound = totalRounds;
        else if (round < LOWEST_ROUND_POSSIBLE) currentRound = LOWEST_ROUND_POSSIBLE;
        else currentRound = round;
    }

    public ArrayList<Facility> getAllFacilities() {
        //List<String> l = new ArrayList<String>(map.keySet());
        //Wanneer de rounddata aanwezig is kan het bovenstaande gebruikt worden om alle keys
        //dus de facilities op te halen
        //Wat hier nu aanwezig is is test code om zo de controller werkend te krijgen

        FacilityType factory = new FacilityType("Factory", 1, 1, 1, 1, 1, 1, 1);
        FacilityType wholesaler = new FacilityType("Wholesaler", 1, 1, 1, 1, 1, 1, 1);
        FacilityType retailer = new FacilityType("Retailer", 1, 1, 1, 1, 1, 1, 1);
        FacilityType warehouse = new FacilityType("Warehouse", 1, 1, 1, 1, 1, 1, 1);

        Facility facility1 = new Facility(factory, 1);
        Facility facility2 = new Facility(wholesaler, 2);
        Facility facility3 = new Facility(retailer, 3);
        Facility facility4 = new Facility(warehouse, 4);

        ArrayList<Facility> returnList = new ArrayList<>();

        returnList.add(facility1);
        returnList.add(facility2);
        returnList.add(facility3);
        returnList.add(facility4);

        return returnList;
    }

    public ObservableList<XYChart.Series<Double, Double>> getChartData() {
        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
        LineChart.Series<Double, Double> factorySeries = new LineChart.Series<>();
        LineChart.Series<Double, Double> warehouseSeries = new LineChart.Series<>();
        LineChart.Series<Double, Double> wholesalerSeries = new LineChart.Series<>();
        LineChart.Series<Double, Double> retailerSeries = new LineChart.Series<>();
        createData(factorySeries, warehouseSeries, wholesalerSeries, retailerSeries);
        lineChartData.add(factorySeries);
        lineChartData.add(warehouseSeries);
        lineChartData.add(wholesalerSeries);
        lineChartData.add(retailerSeries);
        return lineChartData;
    }

    private void createData(XYChart.Series<Double, Double> factorySeries, XYChart.Series<Double, Double> warehouseSeries, XYChart.Series<Double, Double> wholesalerSeries, XYChart.Series<Double, Double> retailerSeries) {
        List<AverageRoundFake> roundsToShow =  averageRounds.stream().filter(averageRoundFake ->
                displayedAverages.contains(averageRoundFake.getFacilityType())&&averageRoundFake.getRoundId()<=currentRound).collect(Collectors.toList());
        roundsToShow.forEach(averageRoundFake -> {
            insertDataIntoSerie(factorySeries, warehouseSeries, wholesalerSeries, retailerSeries, averageRoundFake);
        });
    }

    private void insertDataIntoSerie(XYChart.Series<Double, Double> factorySeries, XYChart.Series<Double, Double> warehouseSeries, XYChart.Series<Double, Double> wholesalerSeries, XYChart.Series<Double, Double> retailerSeries, AverageRoundFake averageRoundFake) {
        switch (averageRoundFake.getFacilityType()){
            case FACTORY:
                factorySeries.getData().add(new XYChart.Data<>((double) averageRoundFake.getRoundId(), averageRoundFake.getAttribute(attribute)));break;
            case WHOLESALER:
                wholesalerSeries.getData().add(new XYChart.Data<>((double) averageRoundFake.getRoundId(), averageRoundFake.getAttribute(attribute)));break;
            case RETAILER:
                retailerSeries.getData().add(new XYChart.Data<>((double) averageRoundFake.getRoundId(), averageRoundFake.getAttribute(attribute)));break;
            case REGIONALWAREHOUSE:
                warehouseSeries.getData().add(new XYChart.Data<>((double) averageRoundFake.getRoundId(), averageRoundFake.getAttribute(attribute)));break;
            default:
                break;
        }
    }

    private void add(ObservableList<XYChart.Series<Double, Double>> lineChartData, LineChart.Series<Double, Double> series) {
        if (!series.getData().isEmpty()) {
            lineChartData.add(series);
        }
    }

    public void removeDisplayedFacility(GameValue facility) {
        displayedAverages.remove(facility);
    }

    public void addDisplayedFacility(GameValue facility) {
        displayedAverages.add(facility);
    }

    public void addDisplayedFacility(Facility facility) {
        displayedFacilities.add(facility);
    }

    public void removeDisplayedFacility(Facility facility) {
        displayedFacilities.remove(facility);
    }

    public void setAttribute(GameValue value) {
        this.attribute = value;
    }
}
