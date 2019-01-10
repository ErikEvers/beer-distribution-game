package org.han.ica.asd.c.replay_data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.interfaces.gui_replay_game.IVisualisedPlayedGameData;
import org.han.ica.asd.c.interfaces.replay.IRetrieveReplayData;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplayComponent implements IVisualisedPlayedGameData {
    private static final int LOWEST_ROUND_POSSIBLE = 0;
    private static final int FIRST_ROUND_TO_DISPLAY = 1;
    private List<Facility> displayedFacilities;
    private List<GameValue> displayedAverages;
    private List<AverageRound> averageRounds;
    private List<Facility> facilities;
    private List<Round> rounds;
    private int currentRound;
    private int totalRounds;
    private GameValue displayedAttribute;

    @Inject
    public ReplayComponent(IRetrieveReplayData retrieveReplayData) {
        facilities = retrieveReplayData.getAllFacilities();
        rounds = retrieveReplayData.getAllRounds();

        displayedAverages = new ArrayList<>();
        averageRounds = new ArrayList<>();
        displayedFacilities = new ArrayList<>();

        currentRound = FIRST_ROUND_TO_DISPLAY;
        totalRounds = rounds.size();

        initializeAverageRounds();
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

    public List<Facility> getAllFacilities() {
        return facilities;
    }

    public ObservableList<XYChart.Series<Double, Double>> getChartData() {
        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
        LineChart.Series<Double, Double> factorySeries = new LineChart.Series<>();
        factorySeries.setName(GameValue.FACTORY.getValue()[0]);
        LineChart.Series<Double, Double> warehouseSeries = new LineChart.Series<>();
        warehouseSeries.setName(GameValue.REGIONALWAREHOUSE.getValue()[0]);
        LineChart.Series<Double, Double> wholesalerSeries = new LineChart.Series<>();
        wholesalerSeries.setName(GameValue.WHOLESALER.getValue()[0]);
        LineChart.Series<Double, Double> retailerSeries = new LineChart.Series<>();
        retailerSeries.setName(GameValue.RETAILER.getValue()[0]);
        createData(factorySeries, warehouseSeries, wholesalerSeries, retailerSeries);
        add(lineChartData, factorySeries);
        add(lineChartData, warehouseSeries);
        add(lineChartData, wholesalerSeries);
        add(lineChartData, retailerSeries);
        return lineChartData;
    }

    private void createData(XYChart.Series<Double, Double> factorySeries, XYChart.Series<Double, Double> warehouseSeries, XYChart.Series<Double, Double> wholesalerSeries, XYChart.Series<Double, Double> retailerSeries) {
        List<AverageRound> roundsToShow = averageRounds.stream().filter(averageRound ->
                displayedAverages.contains(averageRound.getFacilityType()) && averageRound.getRoundId() <= currentRound).collect(Collectors.toList());
        roundsToShow.forEach(averageRound ->
                insertDataIntoSerie(factorySeries, warehouseSeries, wholesalerSeries, retailerSeries, averageRound));
    }

    private void insertDataIntoSerie(XYChart.Series<Double, Double> factorySeries, XYChart.Series<Double, Double> warehouseSeries, XYChart.Series<Double, Double> wholesalerSeries, XYChart.Series<Double, Double> retailerSeries, AverageRound averageRound) {
        switch (averageRound.getFacilityType()) {
            case FACTORY:
                factorySeries.getData().add(new XYChart.Data<>((double) averageRound.getRoundId(), getAttribute(averageRound, displayedAttribute)));
                break;
            case WHOLESALER:
                wholesalerSeries.getData().add(new XYChart.Data<>((double) averageRound.getRoundId(), getAttribute(averageRound, displayedAttribute)));
                break;
            case RETAILER:
                retailerSeries.getData().add(new XYChart.Data<>((double) averageRound.getRoundId(), getAttribute(averageRound, displayedAttribute)));
                break;
            case REGIONALWAREHOUSE:
                warehouseSeries.getData().add(new XYChart.Data<>((double) averageRound.getRoundId(), getAttribute(averageRound, displayedAttribute)));
                break;
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

    public void setDisplayedAttribute(GameValue value) {
        this.displayedAttribute = value;
    }

    public double getAttribute(AverageRound round, GameValue wantedValue) {
        switch (wantedValue) {
            case BUDGET:
                return round.getBudget();
            case STOCK:
                return round.getStock();
            case BACKLOG:
                return round.getBackorders();
            case ORDERED:
                return round.getOrderAmount();
            case OUTGOINGGOODS:
                return round.getDeliverAmount();
            default:
                return 0;
        }
    }

    private void initializeAverageRounds() {
        List<Integer> factoryIds = getFacilityIds(GameValue.FACTORY);
        List<Integer> wholesalerIds = getFacilityIds(GameValue.WHOLESALER);
        List<Integer> retailerIds = getFacilityIds(GameValue.RETAILER);
        List<Integer> warehouseIds = getFacilityIds(GameValue.REGIONALWAREHOUSE);

        calculateAverageRounds(factoryIds, GameValue.FACTORY);
        calculateAverageRounds(wholesalerIds, GameValue.WHOLESALER);
        calculateAverageRounds(retailerIds, GameValue.RETAILER);
        calculateAverageRounds(warehouseIds, GameValue.REGIONALWAREHOUSE);
    }

    private void calculateAverageRounds(List<Integer> ids, GameValue gameValue) {
        rounds.forEach(round -> {
            List<FacilityTurn> turns = round.getFacilityTurns().stream().filter(facilityTurn -> ids.contains(facilityTurn.getFacilityId())).collect(Collectors.toList());
            List<FacilityTurnOrder> turnOrders = round.getFacilityOrders().stream().filter(facilityTurn -> ids.contains(facilityTurn.getFacilityId())).collect(Collectors.toList());
            List<FacilityTurnDeliver> turnDelivers = round.getFacilityTurnDelivers().stream().filter(facilityTurn -> ids.contains(facilityTurn.getFacilityId())).collect(Collectors.toList());
            int totalIds = ids.size();

            averageRounds.add(new AverageRound(round.getRoundId(),
                    gameValue,
                    calculateAverageBudget(totalIds, turns),
                    calculateAverageStock(totalIds, turns),
                    calculateAverageBackOrders(totalIds, turns),
                    calculateAverageOrders(totalIds, turnOrders),
                    calculateAverageDeliverAmount(totalIds, turnDelivers)));
        });
    }

    private float calculateAverageOrders(int totalIds, List<FacilityTurnOrder> turns){
        float totalOrderAmount = 0;
        for (FacilityTurnOrder turn : turns) {
            totalOrderAmount += turn.getOrderAmount();
        }

        return totalOrderAmount/totalIds;
    }

    private float calculateAverageBudget(int totalIds, List<FacilityTurn> turns){
        float totalBudget = 0;
        for (FacilityTurn turn : turns) {
            totalBudget += turn.getRemainingBudget();
        }

        return totalBudget/totalIds;
    }

    private float calculateAverageStock(int totalIds, List<FacilityTurn> turns){
        float totalStock = 0;
        for (FacilityTurn turn : turns) {
            totalStock += turn.getStock();
        }

        return totalStock/totalIds;
    }

    private float calculateAverageBackOrders(int totalIds, List<FacilityTurn> turns){
        float totalBackOrders = 0;
        for (FacilityTurn turn : turns) {
            totalBackOrders += turn.getBackorders();
        }

        return totalBackOrders/totalIds;
    }

    private float calculateAverageDeliverAmount(int totalIds, List<FacilityTurnDeliver> turns){
        float totalDeliverAmount = 0;
        for (FacilityTurnDeliver turn : turns) {
            totalDeliverAmount += turn.getDeliverAmount();
        }

        return totalDeliverAmount/totalIds;
    }

    private List<Integer> getFacilityIds(GameValue gameValue) {
        List<Facility> wantedFacilities = facilities.stream().filter(facility ->
                facility.getFacilityType().getFacilityName().equals(gameValue.getValue()[0])).collect(Collectors.toList());

        List<Integer> ids = new ArrayList<>();

        wantedFacilities.forEach(facility -> ids.add(facility.getFacilityId()));

        return ids;
    }
}
