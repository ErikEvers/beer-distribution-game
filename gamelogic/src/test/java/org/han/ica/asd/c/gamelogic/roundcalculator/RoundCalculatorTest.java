package org.han.ica.asd.c.gamelogic.roundcalculator;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityLinkedTo;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundCalculatorTest {
    private Facility manufacturer;
    private Facility regionalWarehouse;
    private Facility wholesale;
    private Facility retailer;
    private Facility demand;
    private RoundCalculator roundCalculator;
    private List<FacilityLinkedTo> facilityLinkedToList;

    @BeforeEach
    public void setup() {
        //TODO: Use the new Map implementation for FacilityLinkedTo
        FacilityType facilityType = new FacilityType("Manufacturer", "", 3, 3,5, 25, 500, 10);
        FacilityType facilityType1 = new FacilityType("RegionalWarehouse", "", 4, 4,5, 25, 500, 10);
        FacilityType facilityType2 = new FacilityType("Wholesale", "", 5, 5,5, 25, 500, 10);
        FacilityType facilityType3 = new FacilityType("Retailer", "", 6, 6,5, 25, 500, 10);
        FacilityType facilityType4 = new FacilityType("Demand", "", 7, 7,5, 25, 500, 10);
        
        manufacturer = new Facility("0", 0, facilityType, "0", "0");
        regionalWarehouse = new Facility("0", 0, facilityType1, "0", "0");
        wholesale = new Facility("0", 0, facilityType2, "0", "0");
        retailer = new Facility("0", 0, facilityType3, "0", "0");
        demand = new Facility("0", 0, facilityType4, "0", "0");

        facilityLinkedToList = new ArrayList<>();
    }

    private Round setupPreviousRoundObjectWithoutBacklog() {
        Round round = new Round();
        round.setRoundId(0);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
        round.addTurnOrder(wholesale, regionalWarehouse, 30);
        round.addTurnOrder(retailer, wholesale, 15);
        round.addTurnOrder(demand, retailer, 30);

        return round;
    }

    private Round setupCalculatedRoundObject() {
        Round calculatedRound = new Round();
        calculatedRound.setRoundId(1);
        calculatedRound.addFacilityStock(40, manufacturer);
        calculatedRound.addFacilityStock(40, regionalWarehouse);
        calculatedRound.addFacilityStock(40, wholesale);
        calculatedRound.addFacilityStock(40, retailer);
        calculatedRound.addFacilityStock(10000, demand);

        calculatedRound.addFacilityRemainingBudget(500, manufacturer);
        calculatedRound.addFacilityRemainingBudget(500, regionalWarehouse);
        calculatedRound.addFacilityRemainingBudget(500, wholesale);
        calculatedRound.addFacilityRemainingBudget(500, retailer);
        calculatedRound.addFacilityRemainingBudget(500, demand);

        return calculatedRound;
    }

    private void TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(int expectedNumber, Facility testFacility) {
        Round previousRound = setupPreviousRoundObjectWithoutBacklog();

        //TODO: Make the rest of the tests like the one below. Replace the null value with the new FacilityLinksTo list.
        Round calculatedRound = roundCalculator.calculateRound(previousRound, setupCalculatedRoundObject(), null);



        Assert.assertEquals(expectedNumber, calculatedRound.getStockByFacility(testFacility));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForManufacturer() {
        

        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(65, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForRegionalWarehouse() {
        

        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(10, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForWholesale() {
        

        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(55, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForRetailer() {
        

        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(25, retailer);
    }

    private void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(int expectedNumber, Facility facilityOrder, Facility facilityDeliver) {
        Round previousRound = setupPreviousRoundObjectWithoutBacklog();

        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(expectedNumber, calculatedRound.getTurnDeliverByFacility(facilityOrder, facilityDeliver));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForManufacturer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(25, manufacturer, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForRegionalWarehouse() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(0, regionalWarehouse, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForWholesale() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(30, wholesale, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForRetailer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(15, retailer, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForDemand() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(30, demand, retailer);
    }

    private void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(int expectedNumber, Facility facilityOrder, Facility facilityDeliver) {
        Round previousRound = setupPreviousRoundObjectWithoutBacklog();

        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(expectedNumber, calculatedRound.getTurnReceivedByFacility(facilityOrder, facilityDeliver));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForManufacturer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(25, manufacturer, manufacturer);
    }


    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForRegionalWarehouse() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(0, regionalWarehouse, manufacturer);
    }



    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForWholesale() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(30, wholesale, regionalWarehouse);
    }



    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForRetailer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(15, retailer, wholesale);
    }



    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForDemand() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(30, demand, retailer);
    }

    private void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(Facility facility) {
        Round previousRound = setupPreviousRoundObjectWithoutBacklog();

        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertFalse(calculatedRound.isTurnBackLogFilledByFacility(facility));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForManufacturer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForRegionalWarehouse() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForWholesale() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForRetailer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(retailer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForDemand() {
        

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(demand);
    }

    private Round setupPreviousRoundObjectWithBacklog() {
        Round round = new Round();
        round.setRoundId(1);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
        round.addTurnOrder(wholesale, regionalWarehouse, 150);
        round.addTurnOrder(retailer, wholesale, 15);
        round.addTurnOrder(demand, retailer, 30);

        return round;
    }

    private void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(int expectedNumber, Facility testFacility) {
        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(expectedNumber, calculatedRound.getStockByFacility(testFacility));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForManufacturer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(65, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForRegionalWarehouse() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(0, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForWholesale() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(65, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForRetailer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(25, retailer);
    }

    private void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(int expectedNumber, Facility facilityOrder, Facility facilityDeliver) {
        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(expectedNumber, calculatedRound.getTurnDeliverByFacility(facilityOrder, facilityDeliver));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForManufacturer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(25, manufacturer, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForRegionalWarehouse() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(0, regionalWarehouse, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForWholesale() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(40, wholesale, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForRetailer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(15, retailer, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForDemand() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(30, demand, retailer);
    }

    private void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(int expectedNumber, Facility facilityOrder, Facility facilityDeliver) {
        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(expectedNumber, calculatedRound.getTurnReceivedByFacility(facilityOrder, facilityDeliver));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForManufacturer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(25, manufacturer, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForRegionalWarehouse() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(0, regionalWarehouse, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForWholesale() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(40, wholesale, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForRetailer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(15, retailer, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForDemand() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(30, demand, retailer);
    }

    private void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(Facility testFacility) {
        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertFalse(calculatedRound.isTurnBackLogFilledByFacility(testFacility));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForManufacturer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForRegionalWarehouse() {
        


        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForRetailer() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(retailer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForDemand() {
        

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(demand);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForWholesale() {
        

        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(110, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
    }

    private Round setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders() {
        Round round = new Round();
        round.setRoundId(0);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
        round.addTurnOrder(wholesale, regionalWarehouse, 50);
        round.addTurnOrder(retailer, wholesale, 35);
        round.addTurnOrder(demand, retailer, 30);

        return round;
    }

    private void testIfCalculatingRemainingBudgetGoesCorrectly(int expectedNumber, Facility facility) {
        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(100, calculatedRound.getRemainingBudgetByFacility(manufacturer));
    }

    @Test
    void testIfCalculatingRemainingBudgetGoesCorrectlyForManufacturer() {
        

        //stockcost = 65 * 5 = 325; backlogcost = 0 * 25 = 0; OutgoingOrders = 25 * 3 = 75; Incoming orders = 0 * 3 = 0;
        //remaining budget = 500 - 325 - (75 - 0) = 100
        testIfCalculatingRemainingBudgetGoesCorrectly(100, manufacturer);
    }

    @Test
    void testIfCalculatingRemainingBudgetGoesCorrectlyForRegionalWarehouse() {
        

        //stockcost = 0 * 5 = 0; backlogcost = 10 * 25 = 250; OutgoingOrders = 0 * 4 = 0; incomingorders = 40 * 4 = 160;
        //remaining budget = (500 - 250) - (0 - 160) = 410
        testIfCalculatingRemainingBudgetGoesCorrectly(410, regionalWarehouse);
    }

    @Test
    void testIfCalculatingRemainingBudgetGoesCorrectlyForWholesale() {
        

        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0; OutgoingOrders = 40 * 5 = 200; Incoming orders = 35 * 5 = 175;
        //remaining budget = (500 - 225) - (200 - 175) = 250
        testIfCalculatingRemainingBudgetGoesCorrectly(250, wholesale);
    }

    @Test
    void testIfCalculatingRemainingBudgetGoesCorrectlyForRetailer() {
        

        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0; OutgoingOrders = 35 * 6 = 210; Incoming orders = 30 * 6 = 180;
        //remaining budget = (500 - 225) - (210 - 180) - 0 = 245
        testIfCalculatingRemainingBudgetGoesCorrectly(245, retailer);
    }

    private void testIfPreviousOpenOrdersAreBeingHandled(int expectedNumber, Facility facility) {
        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);
        gameLogic.getBeerGame().addRound(calculatedRound);

        calculatedRound.addTurnOrder(manufacturer, manufacturer, 20);
        calculatedRound.addTurnOrder(regionalWarehouse, manufacturer, 30);
        calculatedRound.addTurnOrder(wholesale, regionalWarehouse, 10);
        calculatedRound.addTurnOrder(retailer, wholesale, 5);
        calculatedRound.addTurnOrder(demand, retailer, 20);

        Round solvedBackOrderRound = new Round();
        solvedBackOrderRound.setRoundId(2);
        solvedBackOrderRound.setRemainingBudget(calculatedRound.getRemainingBudget());
        solvedBackOrderRound.setStock(calculatedRound.getStock());
        solvedBackOrderRound = gameLogic.calculateRound(solvedBackOrderRound);

        Assert.assertEquals(expectedNumber, solvedBackOrderRound.getStockByFacility(facility));
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledForManufacturer() {
        

        testIfPreviousOpenOrdersAreBeingHandled(55, manufacturer);
    }



    @Test
    void testIfPreviousOpenOrdersAreBeingHandledForRegionalWarehouse() {
        

        testIfPreviousOpenOrdersAreBeingHandled(10, regionalWarehouse);
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledForWholesale() {
        

        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);
        gameLogic.getBeerGame().addRound(calculatedRound);

        calculatedRound.addTurnOrder(manufacturer, manufacturer, 20);
        calculatedRound.addTurnOrder(regionalWarehouse, manufacturer, 30);
        calculatedRound.addTurnOrder(wholesale, regionalWarehouse, 10);
        calculatedRound.addTurnOrder(retailer, wholesale, 5);
        calculatedRound.addTurnOrder(demand, retailer, 20);

        Round solvedBackOrderRound = new Round();
        solvedBackOrderRound.setRoundId(2);
        solvedBackOrderRound.setRemainingBudget(calculatedRound.getRemainingBudget());
        solvedBackOrderRound.setStock(calculatedRound.getStock());
        solvedBackOrderRound = gameLogic.calculateRound(solvedBackOrderRound);

        Assert.assertEquals(60, solvedBackOrderRound.getStockByFacility(wholesale));
        Assert.assertEquals(0, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
        Assert.assertFalse(solvedBackOrderRound.isTurnBackLogFilledByFacility(wholesale));
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledForRetailer() {
        

        testIfPreviousOpenOrdersAreBeingHandled(30, retailer);
    }

    private void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(int expectedNumber, Facility facility) {
        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);
        gameLogic.getBeerGame().addRound(calculatedRound);

        calculatedRound.addTurnOrder(manufacturer, manufacturer, 20);
        calculatedRound.addTurnOrder(regionalWarehouse, manufacturer, 30);
        calculatedRound.addTurnOrder(wholesale, regionalWarehouse, 50);
        calculatedRound.addTurnOrder(retailer, wholesale, 5);
        calculatedRound.addTurnOrder(demand, retailer, 20);

        Round solvedBackorderRound = new Round();
        solvedBackorderRound.setRoundId(2);
        solvedBackorderRound.setRemainingBudget(calculatedRound.getRemainingBudget());
        solvedBackorderRound.setStock(calculatedRound.getStock());
        solvedBackorderRound = gameLogic.calculateRound(solvedBackorderRound);

        Assert.assertEquals(expectedNumber, solvedBackorderRound.getStockByFacility(facility));
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForManufacturer() {
        

        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(55, manufacturer);
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForRegionalWarehouse() {
        

        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(0, regionalWarehouse);
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForWholesale() {
        

        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);
        gameLogic.getBeerGame().addRound(calculatedRound);

        calculatedRound.addTurnOrder(manufacturer, manufacturer, 20);
        calculatedRound.addTurnOrder(regionalWarehouse, manufacturer, 30);
        calculatedRound.addTurnOrder(wholesale, regionalWarehouse, 50);
        calculatedRound.addTurnOrder(retailer, wholesale, 5);
        calculatedRound.addTurnOrder(demand, retailer, 20);

        Round solvedBackOrderRound = new Round();
        solvedBackOrderRound.setRoundId(2);
        solvedBackOrderRound.setRemainingBudget(calculatedRound.getRemainingBudget());
        solvedBackOrderRound.setStock(calculatedRound.getStock());
        solvedBackOrderRound = gameLogic.calculateRound(solvedBackOrderRound);

        Assert.assertEquals(70, solvedBackOrderRound.getStockByFacility(wholesale));
        Assert.assertEquals(0, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(30, solvedBackOrderRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForRetailer() {
        

        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(30, retailer);
    }
}