//package org.han.ica.asd.c.gamelogic.roundcalculator;
//
//import org.han.ica.asd.c.gamelogic.GameLogic;
//import org.han.ica.asd.c.model.domain_objects.*;
//import org.junit.Assert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//class RoundCalculatorTest {
//    private Facility manufacturer;
//    private Facility regionalWarehouse;
//    private Facility wholesale;
//    private Facility retailer;
//    private Facility demand;
//
//    private GameLogic gameLogic;
//
//
//    private RoundCalculator roundCalculator; //TODO gamelogic en round methodes mocken
//
//    private Map<Facility, List<Facility>> facilityLinks;
//
//    @BeforeEach
//    public void setup() {
//        roundCalculator = new RoundCalculator();
//        gameLogic = new GameLogic(null,null, null, new BeerGame());
//
//        //TODO: Use the new Map implementation for FacilityLinkedTo
//        FacilityType facilityType = new FacilityType("Manufacturer", 3, 3,5, 25, 500, 10, 40);
//        FacilityType facilityType1 = new FacilityType("RegionalWarehouse", 4, 4,5, 25, 500, 10, 40);
//        FacilityType facilityType2 = new FacilityType("Wholesale", 5, 5,5, 25, 500, 10, 40);
//        FacilityType facilityType3 = new FacilityType("Retailer", 6, 6,5, 25, 500, 10, 40);
//        FacilityType facilityType4 = new FacilityType("Demand", 7, 7,5, 25, 500, 10, 1000);
//
//        manufacturer = new Facility(facilityType, 1);
//        regionalWarehouse = new Facility(facilityType1, 2);
//        wholesale = new Facility(facilityType2, 3);
//        retailer = new Facility(facilityType3, 4);
//        demand = new Facility(facilityType4, 5);
//
//        facilityLinks = new HashMap<>();
//
////        ArrayList<Facility> belowManufacturer = new ArrayList<>();
////        belowManufacturer.add(regionalWarehouse);
////        facilityLinksTo.put(manufacturer, belowManufacturer);
////
////        ArrayList<Facility> belowRegionalWarehouse = new ArrayList<>();
////        belowRegionalWarehouse.add(wholesale);
////        facilityLinksTo.put(regionalWarehouse, belowRegionalWarehouse);
////
////        ArrayList<Facility> belowWholesale = new ArrayList<>();
////        belowWholesale.add(retailer);
////        facilityLinksTo.put(wholesale, belowWholesale);
////
////        ArrayList<Facility> belowRetailer = new ArrayList<>();
////        belowRetailer.add(demand);
////        facilityLinksTo.put(retailer, belowRetailer);
////
//     /*   gameLogic.addFacilities(manufacturer, regionalWarehouse);
//        gameLogic.addFacilities(regionalWarehouse, wholesale);
//        gameLogic.addFacilities(wholesale, retailer);
//        gameLogic.addFacilities(retailer, demand);
//        gameLogic.addFacilities(demand, demand);*/
//
//        gameLogic.addFacilities(manufacturer, manufacturer);
//        gameLogic.addFacilities(regionalWarehouse, manufacturer);
//        gameLogic.addFacilities(wholesale, regionalWarehouse);
//        gameLogic.addFacilities(retailer, wholesale);
//        gameLogic.addFacilities(demand, retailer);
//
//        facilityLinks = gameLogic.getFacilityLinks();
//    }
//
//    private Round setupPreviousRoundObjectWithoutBacklog() {
//        Round round = new Round();
//        round.setRoundId(0);
//        round.addTurnOrder(manufacturer, manufacturer, 25);
//        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
//        round.addTurnOrder(wholesale, regionalWarehouse, 30);
//        round.addTurnOrder(retailer, wholesale, 15);
//        round.addTurnOrder(demand, retailer, 30);
//
//        List<FacilityTurn> turns = new ArrayList<>();
//
//        turns.add(new FacilityTurn(1, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(2, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(3, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(4, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(5, 0, 40, 0, 500, false));
//
//        round.setFacilityTurns(turns);
//        return round;
//    }
//
//    private Round setupCalculatedRoundObject() {
//        Round calculatedRound = new Round();
//        calculatedRound.setRoundId(1);
//        calculatedRound.addFacilityRemainingBudget(500, manufacturer);
//        calculatedRound.addFacilityRemainingBudget(500, regionalWarehouse);
//        calculatedRound.addFacilityRemainingBudget(500, wholesale);
//        calculatedRound.addFacilityRemainingBudget(500, retailer);
//        calculatedRound.addFacilityRemainingBudget(500, demand);
//
//        return calculatedRound;
//    }
//
//    private void TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(int expectedNumber, Facility testFacility) {
//        Round previousRound = setupPreviousRoundObjectWithoutBacklog();
//
//        //TODO: Make the rest of the tests like the one below. Replace the null value with the new FacilityLinksTo list.
//        Round calculatedRound = roundCalculator.calculateRound(previousRound, setupCalculatedRoundObject(), facilityLinks);
//
//        Assert.assertEquals(expectedNumber, calculatedRound.getStockByFacility(testFacility));
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForManufacturer() {
//        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(65, manufacturer);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForRegionalWarehouse() {
//        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(10, regionalWarehouse);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForWholesale() {
//        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(55, wholesale);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForRetailer() {
//        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(25, retailer);
//    }
//
//    private void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(int expectedNumber, Facility facilityOrder, Facility facilityDeliver) {
//        Round previousRound = setupPreviousRoundObjectWithoutBacklog();
//
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//
//        Assert.assertEquals(expectedNumber, calculatedRound.getTurnDeliverByFacility(facilityOrder, facilityDeliver));
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForManufacturer() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(25, manufacturer, manufacturer);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForRegionalWarehouse() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(0, regionalWarehouse, manufacturer);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForWholesale() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(30, wholesale, regionalWarehouse);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForRetailer() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(15, retailer, wholesale);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForDemand() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(30, demand, retailer);
//    }
//
//
//    private void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(Facility order, Facility deliver) {
//        Round previousRound = setupPreviousRoundObjectWithoutBacklog();
//
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//
//        Assert.assertTrue(calculatedRound.getTurnBacklogByFacility(order, deliver) < 1);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForManufacturer() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(manufacturer, regionalWarehouse);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForRegionalWarehouse() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(regionalWarehouse, wholesale);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForWholesale() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(wholesale, retailer);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForRetailer() {
//        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(retailer, demand);
//    }
//
//    private Round setupPreviousRoundObjectWithBacklog() {
//        Round round = new Round();
//        round.setRoundId(1);
//        round.addTurnOrder(manufacturer, manufacturer, 25);
//        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
//        round.addTurnOrder(wholesale, regionalWarehouse, 150);
//        round.addTurnOrder(retailer, wholesale, 15);
//        round.addTurnOrder(demand, retailer, 30);
//
//        List<FacilityTurn> turns = new ArrayList<>();
//
//        turns.add(new FacilityTurn(1, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(2, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(3, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(4, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(5, 0, 40, 0, 500, false));
//
//        round.setFacilityTurns(turns);
//
//        return round;
//    }
//
//    private void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(int expectedNumber, Facility order, Facility deliver) {
//        Round previousRound = setupPreviousRoundObjectWithBacklog();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//
//        Assert.assertEquals(expectedNumber, calculatedRound.getStockByFacility(order));
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForManufacturer() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(65, manufacturer, manufacturer);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForRegionalWarehouse() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(0, regionalWarehouse, manufacturer);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForWholesale() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(65, wholesale, regionalWarehouse);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForRetailer() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(25, retailer, wholesale);
//    }
//
//    private void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(int expectedNumber, Facility facilityOrder, Facility facilityDeliver) {
//        Round previousRound = setupPreviousRoundObjectWithBacklog();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//
//        Assert.assertEquals(expectedNumber, calculatedRound.getTurnDeliverByFacility(facilityDeliver, facilityOrder));
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForManufacturer() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(25, manufacturer, manufacturer);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForRegionalWarehouse() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(0, manufacturer, regionalWarehouse);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForWholesale() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(40, regionalWarehouse, wholesale);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForRetailer() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(15, wholesale, retailer);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForDemand() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(30, retailer, demand);
//    }
//
//    private void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(Facility order, Facility deliver) {
//        Round previousRound = setupPreviousRoundObjectWithBacklog();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//
//        Assert.assertTrue(calculatedRound.getTurnBacklogByFacility(order, deliver) < 1);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForManufacturer() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(manufacturer, regionalWarehouse);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForRegionalWarehouse() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(regionalWarehouse, wholesale);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForRetailer() {
//        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(retailer, demand);
//    }
//
//    @Test
//    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForWholesale() {
//        Round previousRound = setupPreviousRoundObjectWithBacklog();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//
//        Assert.assertEquals(110, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
//    }
//
//    private Round setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders() {
//        Round round = new Round();
//        round.setRoundId(0);
//        round.addTurnOrder(manufacturer, manufacturer, 25);
//        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
//        round.addTurnOrder(wholesale, regionalWarehouse, 50);
//        round.addTurnOrder(retailer, wholesale, 35);
//        round.addTurnOrder(demand, retailer, 30);
//
//        List<FacilityTurn> turns = new ArrayList<>();
//
//
//        turns.add(new FacilityTurn(1, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(2, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(3, 0, 40, 0, 500, false)); //TODO
//        turns.add(new FacilityTurn(4, 0, 40, 0, 500, false));
//        turns.add(new FacilityTurn(5, 0, 400, 0, 500, false));
//
//        round.setFacilityTurns(turns);
//
//        return round;
//    }
//
//    private void testIfCalculatingRemainingBudgetGoesCorrectly(int expectedNumber, Facility facility) {
//        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//
//        Assert.assertEquals(expectedNumber, calculatedRound.getRemainingBudgetByFacility(facility));
//    }
//
//    @Test
//    void testIfCalculatingRemainingBudgetGoesCorrectlyForManufacturer() {
//        //stockcost = 65 * 5 = 325; backlogcost = 0 * 25 = 0; OutgoingOrders = 25 * 3 = 75; Incoming orders = 0 * 3 = 0;
//        //remaining budget = 500 - 325 - (75 - 0) = 100
//        testIfCalculatingRemainingBudgetGoesCorrectly(100, manufacturer);
//    }
//
//    @Test
//    void testIfCalculatingRemainingBudgetGoesCorrectlyForRegionalWarehouse() {
//        //stockcost = 0 * 5 = 0; backlogcost = 10 * 25 = 250; OutgoingOrders = 0 * 4 = 0; incomingorders = 40 * 4 = 160;
//        //remaining budget = (500 - 250) - (0 - 160) = 410
//        testIfCalculatingRemainingBudgetGoesCorrectly(410, regionalWarehouse);
//    }
//
//    @Test
//    void testIfCalculatingRemainingBudgetGoesCorrectlyForWholesale() {
//        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0; OutgoingOrders = 40 * 5 = 200; Incoming orders = 35 * 5 = 175;
//        //remaining budget = (500 - 225) - (200 - 175) = 250
//        testIfCalculatingRemainingBudgetGoesCorrectly(250, wholesale);
//    }
//
//    @Test
//    void testIfCalculatingRemainingBudgetGoesCorrectlyForRetailer() {
//        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0; OutgoingOrders = 35 * 6 = 210; Incoming orders = 30 * 6 = 180;
//        //remaining budget = (500 - 225) - (210 - 180) - 0 = 245
//        testIfCalculatingRemainingBudgetGoesCorrectly(245, retailer);
//    }
//
//    private void testIfPreviousOpenOrdersAreBeingHandled(int expectedNumber, Facility facility) {
//        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//        gameLogic.getBeerGame().addRound(calculatedRound);
//
//        Round solvedBackOrderRound = new Round();
//        solvedBackOrderRound.setRoundId(2);
//        solvedBackOrderRound.setFacilityTurns(calculatedRound.getFacilityTurns());
//        //TODO check of dit zo nog werkt
//        calculatedRound.addTurnOrder(manufacturer, manufacturer, 20);
//        calculatedRound.addTurnOrder(regionalWarehouse, manufacturer, 30);
//        calculatedRound.addTurnOrder(wholesale, regionalWarehouse, 10);
//        calculatedRound.addTurnOrder(retailer, wholesale, 5);
//        calculatedRound.addTurnOrder(demand, retailer, 20);
//
//        solvedBackOrderRound = gameLogic.calculateRound(solvedBackOrderRound);
//
//        Assert.assertEquals(expectedNumber, solvedBackOrderRound.getStockByFacility(facility));
//    }
//
//    @Test
//    void testIfPreviousOpenOrdersAreBeingHandledForManufacturer() {
//        testIfPreviousOpenOrdersAreBeingHandled(55, manufacturer);
//    }
//
//    @Test
//    void testIfPreviousOpenOrdersAreBeingHandledForRegionalWarehouse() {
//        testIfPreviousOpenOrdersAreBeingHandled(10, regionalWarehouse);
//    }
//
//   /* @Test
//    void testIfPreviousOpenOrdersAreBeingHandledForWholesale() {
//        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//        gameLogic.getBeerGame().addRound(calculatedRound);
//
//        Round solvedBackOrderRound = new Round();
//        solvedBackOrderRound.setRoundId(2);
//        solvedBackOrderRound.setFacilityTurns(calculatedRound.getFacilityTurns());
//        //TODO check of dit zo nog werkt
//        calculatedRound.addTurnOrder(manufacturer, manufacturer, 20);
//        calculatedRound.addTurnOrder(regionalWarehouse, manufacturer, 30);
//        calculatedRound.addTurnOrder(wholesale, regionalWarehouse, 10);
//        calculatedRound.addTurnOrder(retailer, wholesale, 5);
//        calculatedRound.addTurnOrder(demand, retailer, 20);
//
//        solvedBackOrderRound = gameLogic.calculateRound(solvedBackOrderRound);
//
//        int wholesale2 = solvedBackOrderRound.getStockByFacility(wholesale);
//        System.out.println(":" + wholesale2);
//        Assert.assertEquals(60, wholesale2);
//        Assert.assertEquals(0, solvedBackOrderRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
//        Assert.assertTrue(solvedBackOrderRound.getTurnBacklogByFacility(wholesale, retailer) < 1);
//    }*/
//
//    @Test
//    void testIfPreviousOpenOrdersAreBeingHandledForRetailer() {
//        testIfPreviousOpenOrdersAreBeingHandled(30, retailer);
//    }
//
//    private void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(int expectedNumber, Facility facility) {
//        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//        gameLogic.getBeerGame().addRound(calculatedRound);
//
//        Round solvedBackOrderRound = new Round();
//        solvedBackOrderRound.setRoundId(2);
//        solvedBackOrderRound.setFacilityTurns(calculatedRound.getFacilityTurns());
//        //TODO check of dit zo nog werkt
//        calculatedRound.addTurnOrder(manufacturer, manufacturer, 20);
//        calculatedRound.addTurnOrder(regionalWarehouse, manufacturer, 30);
//        calculatedRound.addTurnOrder(wholesale, regionalWarehouse, 10);
//        calculatedRound.addTurnOrder(retailer, wholesale, 5);
//        calculatedRound.addTurnOrder(demand, retailer, 20);
//
//        solvedBackOrderRound = gameLogic.calculateRound(solvedBackOrderRound);
//
//        Assert.assertEquals(expectedNumber, solvedBackOrderRound.getStockByFacility(facility));
//    }
//
//    @Test
//    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForManufacturer() {
//        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(55, manufacturer);
//    }
//
//   /* @Test
//    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForRegionalWarehouse() {
//        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(0, regionalWarehouse);
//    }
//
//    @Test
//    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForWholesale() {
//        Round previousRound = setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders();
//        gameLogic.getBeerGame().addRound(previousRound);
//
//        Round calculatedRound = setupCalculatedRoundObject();
//
//        calculatedRound = gameLogic.calculateRound(calculatedRound);
//        gameLogic.getBeerGame().addRound(calculatedRound);
//
//        Round solvedBackOrderRound = new Round();
//        solvedBackOrderRound.setRoundId(2);
//        solvedBackOrderRound.setFacilityTurns(calculatedRound.getFacilityTurns());
//        //TODO check of dit zo nog werkt
//        calculatedRound.addTurnOrder(manufacturer, manufacturer, 20);
//        calculatedRound.addTurnOrder(regionalWarehouse, manufacturer, 30);
//        calculatedRound.addTurnOrder(wholesale, regionalWarehouse, 10);
//        calculatedRound.addTurnOrder(retailer, wholesale, 5);
//        calculatedRound.addTurnOrder(demand, retailer, 20);
//
//        solvedBackOrderRound = gameLogic.calculateRound(solvedBackOrderRound);
//        Assert.assertEquals(70, solvedBackOrderRound.getStockByFacility(wholesale));
//        Assert.assertEquals(0, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
//        Assert.assertEquals(30, solvedBackOrderRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
//    }*/
//
//    @Test
//    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForRetailer() {
//        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(30, retailer);
//    }
//}