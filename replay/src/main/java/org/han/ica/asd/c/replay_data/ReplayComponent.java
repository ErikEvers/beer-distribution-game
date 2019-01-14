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
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ReplayComponent implements IVisualisedPlayedGameData {
    private static final int LOWEST_ROUND_POSSIBLE = 0;
    private static final int FIRST_ROUND_TO_DISPLAY = 1;
    private Facility displayedFacility;
    private List<GameValue> displayedAverages;
    private List<AverageRound> averageRounds;
    private List<Facility> facilities;
    private BeerGame beerGame;
    private List<Round> rounds;
    private int currentRound;
    private int totalRounds;
    private GameValue displayedAttribute;

    @Inject
    public ReplayComponent(IRetrieveReplayData retrieveReplayData) {
        facilities = retrieveReplayData.getAllFacilities();
        beerGame = retrieveReplayData.getBeerGame();
        rounds = beerGame.getRounds();

        displayedAverages = new ArrayList<>();
        averageRounds = new ArrayList<>();

        currentRound = FIRST_ROUND_TO_DISPLAY;
        totalRounds = rounds.size()-1;

        initializeAverageRounds();
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public String getTotalRoundsString() {
        return String.valueOf(totalRounds);
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public String getCurrentRoundString() {
        return String.valueOf(currentRound);
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public boolean incrementCurrentRound() {
        if (currentRound < totalRounds) {
            this.currentRound++;
            return true;
        }
        return false;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public boolean decrementCurrentRound() {
        if (currentRound > LOWEST_ROUND_POSSIBLE) {
            this.currentRound--;
            return true;
        }
        return false;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public void updateCurrentRound(int round) {
        if (round > totalRounds) currentRound = totalRounds;
        else if (round < LOWEST_ROUND_POSSIBLE) currentRound = LOWEST_ROUND_POSSIBLE;
        else currentRound = round;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public List<Facility> getAllFacilities() {
        return facilities;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public ObservableList<XYChart.Series<Double, Double>> getChartData() {
        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
        if(displayedAverages.isEmpty() && displayedFacility != null){
            getSpecificData(lineChartData);
        }
        else {
            getAverageData(lineChartData);
        }

        return lineChartData;
    }

    private void getSpecificData(ObservableList<XYChart.Series<Double, Double>> lineChartData){
        LineChart.Series<Double, Double> facilitySeries = new LineChart.Series<>();
        facilitySeries.setName(displayedFacility.getFacilityType().getFacilityName());
        createDataForSpecificFacility(facilitySeries);
        addSeriesToChart(lineChartData, facilitySeries);
    }

    private void getAverageData(ObservableList<XYChart.Series<Double, Double>> lineChartData){
        LineChart.Series<Double, Double> factorySeries = new LineChart.Series<>();
        factorySeries.setName(GameValue.FACTORY.getValue()[0]);
        LineChart.Series<Double, Double> warehouseSeries = new LineChart.Series<>();
        warehouseSeries.setName(GameValue.REGIONALWAREHOUSE.getValue()[0]);
        LineChart.Series<Double, Double> wholesalerSeries = new LineChart.Series<>();
        wholesalerSeries.setName(GameValue.WHOLESALER.getValue()[0]);
        LineChart.Series<Double, Double> retailerSeries = new LineChart.Series<>();
        retailerSeries.setName(GameValue.RETAILER.getValue()[0]);
        createData(factorySeries, warehouseSeries, wholesalerSeries, retailerSeries);
        addSeriesToChart(lineChartData, factorySeries);
        addSeriesToChart(lineChartData, warehouseSeries);
        addSeriesToChart(lineChartData, wholesalerSeries);
        addSeriesToChart(lineChartData, retailerSeries);
    }

    /***
     * Creates the data that is returned in the getChartData()
     * @param factorySeries
     * A XYChart series that will be filled with Factory data
     * @param warehouseSeries
     * A XYChart series that will be filled with Warehouse data
     * @param wholesalerSeries
     * A XYChart series that will be filled with WholeSaler data
     * @param retailerSeries
     * A XYChart series that will be filled with Retailer data
     */
    private void createData(XYChart.Series<Double, Double> factorySeries, XYChart.Series<Double, Double> warehouseSeries, XYChart.Series<Double, Double> wholesalerSeries, XYChart.Series<Double, Double> retailerSeries) {
        List<AverageRound> roundsToShow = averageRounds.stream().filter(averageRound ->
                displayedAverages.contains(averageRound.getFacilityType()) && averageRound.getRoundId() <= currentRound).collect(Collectors.toList());
        roundsToShow.forEach(averageRound ->
                insertDataIntoSeries(factorySeries, warehouseSeries, wholesalerSeries, retailerSeries, averageRound));
    }

    private void createDataForSpecificFacility(XYChart.Series<Double, Double> facilitySeries) {
        List<Round> roundsToShow = rounds.stream().filter(round ->
            round.getRoundId() <= currentRound).collect(Collectors.toList());
        roundsToShow.forEach(round -> {
                round.setFacilityTurns(round.getFacilityTurns().stream().filter(facilityTurn -> facilityTurn.getFacilityId() == displayedFacility.getFacilityId()).collect(Collectors.toList()));
                round.setFacilityOrders(round.getFacilityOrders().stream().filter(facilityTurnOrder -> facilityTurnOrder.getFacilityId() == displayedFacility.getFacilityId()).collect(Collectors.toList()));
                round.setFacilityTurnDelivers(round.getFacilityTurnDelivers().stream().filter(facilityTurnDeliver -> facilityTurnDeliver.getFacilityId() == displayedFacility.getFacilityId()).collect(Collectors.toList()));
            });
        roundsToShow.forEach(round -> facilitySeries.getData().add(new XYChart.Data<>((double) round.getRoundId(), getAttributeForFacility(round, displayedAttribute))));
    }

    /***
     * Inserts the data from a round to the correct series
     * @param factorySeries
     * A XYChart series that will be filled with Factory data
     * @param warehouseSeries
     * A XYChart series that will be filled with Warehouse data
     * @param wholesalerSeries
     * A XYChart series that will be filled with WholeSaler data
     * @param retailerSeries
     * A XYChart series that will be filled with Retailer data
     * @param averageRound
     * The data that will be inserted into the correct series
     */
    private void insertDataIntoSeries(XYChart.Series<Double, Double> factorySeries, XYChart.Series<Double, Double> warehouseSeries, XYChart.Series<Double, Double> wholesalerSeries, XYChart.Series<Double, Double> retailerSeries, AverageRound averageRound) {
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

    /***
     * Inserts a series into the Chart if the series is not empty
     * @param lineChartData
     * The Chart data
     * @param series
     * The series that can be added to the Chart data if the series is not empty
     */
    private void addSeriesToChart(ObservableList<XYChart.Series<Double, Double>> lineChartData, LineChart.Series<Double, Double> series) {
        if (!series.getData().isEmpty()) {
            lineChartData.add(series);
        }
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public void removeDisplayedFacility(GameValue facility) {
        displayedAverages.remove(facility);
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public void addDisplayedFacility(GameValue facility) {
        displayedAverages.add(facility);
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public void addDisplayedFacility(Facility facility) {
        displayedFacility = facility;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public void removeDisplayedFacility(Facility facility) {
        displayedFacility = null;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public void setDisplayedAttribute(GameValue value) {
        this.displayedAttribute = value;
    }

    @Override
    public BeerGame getBeerGameForCurrentRound() {
//        BeerGame returnGame = beerGame;
//        returnGame.getRounds().stream().filter(round -> round.getRoundId() == currentRound);
//        return returnGame;
        return beerGame;
    }

    /***
     *
     * @param round
     * The round from which the value is wanted
     * @param wantedValue
     * The GameValue for the wanted attribute
     * @return The value of the wanted Attribute from the AverageRound
     */
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

    private double getAttributeForFacility(Round round, GameValue value) {
        switch(value) {
            case BUDGET:
                return round.getFacilityTurns().get(0).getRemainingBudget();
            case STOCK:
                return round.getFacilityTurns().get(0).getStock();
            case BACKLOG:
                return round.getFacilityTurns().get(0).getBackorders();
            case ORDERED:
                return round.getFacilityOrders().get(0).getOrderAmount();
            case OUTGOINGGOODS:
                return round.getFacilityTurnDelivers().get(0).getDeliverAmount();
            default:
                return 0;
        }
    }

    /***
     * Initializes the list with AverageRounds
     * Calls the calculateAverageRounds function 4 times, once for each type of Facility available in a game
     */
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

    /***
     * Fills the list of AverageRounds with the calculated round
     * @param ids
     * All ids from a specific FacilityType
     * @param gameValue
     * The GameValue needed for the AverageRound constructor
     */
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

    /***
     * Calculates the average orders of a round
     * @param totalIds
     * The amount of ID's from this FacilityType, used to calculate the average
     * @param turns
     * All turns from a round
     * @return The calculated average from all turns in a round
     */
    private float calculateAverageOrders(int totalIds, List<FacilityTurnOrder> turns) {
        float totalOrderAmount = 0;
        for (FacilityTurnOrder turn : turns) {
            totalOrderAmount += turn.getOrderAmount();
        }

        return totalOrderAmount / totalIds;
    }

    /***
     * Calculates the average budget of a round
     * @param totalIds
     * The amount of ID's from this FacilityType, used to calculate the average
     * @param turns
     * All turns from a round
     * @return The calculated average from all turns in a round
     */
    private float calculateAverageBudget(int totalIds, List<FacilityTurn> turns) {
        float totalBudget = 0;
        for (FacilityTurn turn : turns) {
            totalBudget += turn.getRemainingBudget();
        }

        return totalBudget / totalIds;
    }

    /***
     * Calculates the average stock of a round
     * @param totalIds
     * The amount of ID's from this FacilityType, used to calculate the average
     * @param turns
     * All turns from a round
     * @return The calculated average from all turns in a round
     */
    private float calculateAverageStock(int totalIds, List<FacilityTurn> turns) {
        float totalStock = 0;
        for (FacilityTurn turn : turns) {
            totalStock += turn.getStock();
        }

        return totalStock / totalIds;
    }

    /***
     * Calculates the average back orders of a round
     * @param totalIds
     * The amount of ID's from this FacilityType, used to calculate the average
     * @param turns
     * All turns from a round
     * @return The calculated average from all turns in a round
     */
    private float calculateAverageBackOrders(int totalIds, List<FacilityTurn> turns) {
        float totalBackOrders = 0;
        for (FacilityTurn turn : turns) {
            totalBackOrders += turn.getBackorders();
        }

        return totalBackOrders / totalIds;
    }

    /***
     * Calculates the average deliver amount of a round
     * @param totalIds
     * The amount of ID's from this FacilityType, used to calculate the average
     * @param turns
     * All turns from a round
     * @return The calculated average from all turns in a round
     */
    private float calculateAverageDeliverAmount(int totalIds, List<FacilityTurnDeliver> turns) {
        float totalDeliverAmount = 0;
        for (FacilityTurnDeliver turn : turns) {
            totalDeliverAmount += turn.getDeliverAmount();
        }

        return totalDeliverAmount / totalIds;
    }

    /***
     *
     * @param gameValue
     * The GameValue of the wanted FacilityType
     * @return A list of Integers filled with the ID's of 1 FacilityType
     */
    private List<Integer> getFacilityIds(GameValue gameValue) {
        List<Facility> wantedFacilities = facilities.stream().filter(facility ->
                facility.getFacilityType().getFacilityName().equals(gameValue.getValue()[0])).collect(Collectors.toList());

        List<Integer> ids = new ArrayList<>();

        wantedFacilities.forEach(facility -> ids.add(facility.getFacilityId()));

        return ids;
    }
}
