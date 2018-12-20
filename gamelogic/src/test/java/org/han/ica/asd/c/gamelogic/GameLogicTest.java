package org.han.ica.asd.c.gamelogic;
import com.sun.org.apache.bcel.internal.generic.RET;
import org.han.ica.asd.c.gamelogic.participants.fakes.RoundFake;
import org.han.ica.asd.c.gamelogic.public_interfaces.IConnectedForPlayer;
import org.han.ica.asd.c.model.*;
import org.han.ica.asd.c.gamelogic.participants.IParticipant;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.gamelogic.participants.domain_models.PlayerParticipant;
import org.han.ica.asd.c.gamelogic.participants.fakes.PlayerFake;
import org.han.ica.asd.c.gamelogic.public_interfaces.IPersistence;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;


public class GameLogicTest {
    private GameLogic gameLogic;
    private ParticipantsPool participantsPool;
    private IConnectedForPlayer communication;
    private IPersistence persistence;
    private Facility manufacturer;
    private Facility regionalWarehouse;
    private Facility wholesale;
    private Facility retailer;
    private Facility demand;

    @BeforeEach
    void setup() {
        communication = mock(IConnectedForPlayer.class);
        persistence = mock(IPersistence.class);

        participantsPool = mock(ParticipantsPool.class);
        gameLogic = new GameLogic("test", communication, persistence, participantsPool, new BeerGame("0", "", "", ""));
    }

    @Test
    void placeOrderCallsPersistence() {
        Map<Facility, Map<Facility, Integer>> turn = new HashMap<>();
        gameLogic.placeOrder(turn);
        verify(persistence, times(1)).saveTurnData(turn);
    }

    @Test
    void placeOrderCallsCommunication() {
        Map<Facility, Map<Facility, Integer>> turn = new HashMap<>();
        gameLogic.placeOrder(turn);
        verify(communication, times(1)).sendTurnData(turn);
    }

    @Test
    void seeOtherFacilitiesCallsPersistence() {
        gameLogic.seeOtherFacilities();
        verify(persistence, times(1)).fetchRoundData(anyString(), anyInt());
    }

    @Test
    void letAgentTakeOverPlayerReplacesPlayer() {
        gameLogic.letAgentTakeOverPlayer(mock(AgentParticipant.class));
        verify(participantsPool, times(1)).replacePlayerWithAgent(any());
    }

    @Test
    void letPlayerTakeOverAgentReplacesAgent() {
        gameLogic.letPlayerTakeOverAgent();
        verify(participantsPool, times(1)).replaceAgentWithPlayer();
    }

    @Test
    void addLocalParticipantCallsParticipantsPool() {
        IParticipant participant = mock(IParticipant.class);
        gameLogic.addLocalParticipant(participant);
        verify(participantsPool, times(1)).addParticipant(participant);
    }

    @Test
    void removeAgentByPlayerIdGetsPlayerFromDatabase() {
        when(persistence.getPlayerById(anyString())).thenReturn(new PlayerFake());
        gameLogic.removeAgentByPlayerId(anyString());
        verify(persistence, times(1)).getPlayerById(anyString());
    }

    @Test
    void removeAgentByPlayerIdReplacesAgentAtParticipantsPool() {
        when(persistence.getPlayerById(anyString())).thenReturn(new PlayerFake());
        gameLogic.removeAgentByPlayerId(anyString());
        verify(participantsPool, times(1)).replaceAgentWithPlayer(any(PlayerParticipant.class));
    }

    private void setupCalculateRoundTests() {
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

        FacilityLinkedTo facilityLinkedTo = new FacilityLinkedTo("0", manufacturer, manufacturer, true);
        FacilityLinkedTo facilityLinkedTo1 = new FacilityLinkedTo("0", regionalWarehouse, manufacturer, true);
        FacilityLinkedTo facilityLinkedTo2 = new FacilityLinkedTo("0", wholesale, regionalWarehouse, true);
        FacilityLinkedTo facilityLinkedTo3 = new FacilityLinkedTo("0", retailer, wholesale, true);
        FacilityLinkedTo facilityLinkedTo4 = new FacilityLinkedTo("0", demand, retailer, true);

        gameLogic.addfacilities(facilityLinkedTo);
        gameLogic.addfacilities(facilityLinkedTo1);
        gameLogic.addfacilities(facilityLinkedTo2);
        gameLogic.addfacilities(facilityLinkedTo3);
        gameLogic.addfacilities(facilityLinkedTo4);
    }

    private Round setupPreviousRoundObjectWithoutBacklog() {
        Round round = new RoundFake("0", 0);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
        round.addTurnOrder(wholesale, regionalWarehouse, 30);
        round.addTurnOrder(retailer, wholesale, 15);
        round.addTurnOrder(demand, retailer, 30);

        return round;
    }

    private Round setupCalculatedRoundObject() {
        Round calculatedRound = new RoundFake("0", 1);
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

        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(expectedNumber, calculatedRound.getStockByFacility(testFacility));
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForManufacturer() {
        setupCalculateRoundTests();

        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(65, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForRegionalWarehouse() {
        setupCalculateRoundTests();

        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(10, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForWholesale() {
        setupCalculateRoundTests();

        TestIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility(55, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacilityForRetailer() {
        setupCalculateRoundTests();

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
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(25, manufacturer, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForRegionalWarehouse() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(0, regionalWarehouse, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForWholesale() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(30, wholesale, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForRetailer() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility(15, retailer, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacilityForDemand() {
        setupCalculateRoundTests();

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
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(25, manufacturer, manufacturer);
    }


    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForRegionalWarehouse() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(0, regionalWarehouse, manufacturer);
    }



    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForWholesale() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(30, wholesale, regionalWarehouse);
    }



    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForRetailer() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility(15, retailer, wholesale);
    }



    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacilityForDemand() {
        setupCalculateRoundTests();

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
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForRegionalWarehouse() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForWholesale() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForRetailer() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(retailer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacilityForDemand() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility(demand);
    }

    private Round setupPreviousRoundObjectWithBacklog() {
        Round round = new RoundFake("0", 1);
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
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(65, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForRegionalWarehouse() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(0, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForWholesale() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility(65, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacilityForRetailer() {
        setupCalculateRoundTests();

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
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(25, manufacturer, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForRegionalWarehouse() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(0, regionalWarehouse, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForWholesale() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(40, wholesale, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForRetailer() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility(15, retailer, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacilityForDemand() {
        setupCalculateRoundTests();

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
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(25, manufacturer, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForRegionalWarehouse() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(0, regionalWarehouse, manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForWholesale() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(40, wholesale, regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForRetailer() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility(15, retailer, wholesale);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacilityForDemand() {
        setupCalculateRoundTests();

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
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(manufacturer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForRegionalWarehouse() {
        setupCalculateRoundTests();


        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(regionalWarehouse);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForRetailer() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(retailer);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForDemand() {
        setupCalculateRoundTests();

        testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility(demand);
    }

    @Test
    void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacilityForWholesale() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(110, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
    }

    private Round setupPreviousRoundObjectForBudgetCalculationAndHandlingPreviousOpenOrders() {
        Round round = new RoundFake("0", 0);
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
        setupCalculateRoundTests();

        //stockcost = 65 * 5 = 325; backlogcost = 0 * 25 = 0; OutgoingOrders = 25 * 3 = 75; Incoming orders = 0 * 3 = 0;
        //remaining budget = 500 - 325 - (75 - 0) = 100
        testIfCalculatingRemainingBudgetGoesCorrectly(100, manufacturer);
    }

    @Test
    void testIfCalculatingRemainingBudgetGoesCorrectlyForRegionalWarehouse() {
        setupCalculateRoundTests();

        //stockcost = 0 * 5 = 0; backlogcost = 10 * 25 = 250; OutgoingOrders = 0 * 4 = 0; incomingorders = 40 * 4 = 160;
        //remaining budget = (500 - 250) - (0 - 160) = 410
        testIfCalculatingRemainingBudgetGoesCorrectly(410, regionalWarehouse);
    }

    @Test
    void testIfCalculatingRemainingBudgetGoesCorrectlyForWholesale() {
        setupCalculateRoundTests();

        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0; OutgoingOrders = 40 * 5 = 200; Incoming orders = 35 * 5 = 175;
        //remaining budget = (500 - 225) - (200 - 175) = 250
        testIfCalculatingRemainingBudgetGoesCorrectly(250, wholesale);
    }

    @Test
    void testIfCalculatingRemainingBudgetGoesCorrectlyForRetailer() {
        setupCalculateRoundTests();

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

        Round solvedBackorderRound = new RoundFake("0", 2);
        solvedBackorderRound.setRemainingBudget(calculatedRound.getRemainingBudget());
        solvedBackorderRound.setStock(calculatedRound.getStock());
        solvedBackorderRound = gameLogic.calculateRound(solvedBackorderRound);

        Assert.assertEquals(expectedNumber, solvedBackorderRound.getStockByFacility(facility));
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledForManufacturer() {
        setupCalculateRoundTests();

        testIfPreviousOpenOrdersAreBeingHandled(55, manufacturer);
    }



    @Test
    void testIfPreviousOpenOrdersAreBeingHandledForRegionalWarehouse() {
        setupCalculateRoundTests();

        testIfPreviousOpenOrdersAreBeingHandled(10, regionalWarehouse);
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledForWholesale() {
        setupCalculateRoundTests();

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

        Round solvedBackorderRound = new RoundFake("0", 2);
        solvedBackorderRound.setRemainingBudget(calculatedRound.getRemainingBudget());
        solvedBackorderRound.setStock(calculatedRound.getStock());
        solvedBackorderRound = gameLogic.calculateRound(solvedBackorderRound);

        Assert.assertEquals(60, solvedBackorderRound.getStockByFacility(wholesale));
        Assert.assertEquals(0, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
        Assert.assertFalse(solvedBackorderRound.isTurnBackLogFilledByFacility(wholesale));
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledForRetailer() {
        setupCalculateRoundTests();

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

        Round solvedBackorderRound = new RoundFake("0", 2);
        solvedBackorderRound.setRemainingBudget(calculatedRound.getRemainingBudget());
        solvedBackorderRound.setStock(calculatedRound.getStock());
        solvedBackorderRound = gameLogic.calculateRound(solvedBackorderRound);

        Assert.assertEquals(expectedNumber, solvedBackorderRound.getStockByFacility(facility));
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForManufacturer() {
        setupCalculateRoundTests();

        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(55, manufacturer);
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForRegionalWarehouse() {
        setupCalculateRoundTests();

        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(0, regionalWarehouse);
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForWholesale() {
        setupCalculateRoundTests();

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

        Round solvedBackorderRound = new RoundFake("0", 2);
        solvedBackorderRound.setRemainingBudget(calculatedRound.getRemainingBudget());
        solvedBackorderRound.setStock(calculatedRound.getStock());
        solvedBackorderRound = gameLogic.calculateRound(solvedBackorderRound);

        Assert.assertEquals(70, solvedBackorderRound.getStockByFacility(wholesale));
        Assert.assertEquals(0, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(30, solvedBackorderRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
    }

    @Test
    void testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrdersForRetailer() {
        setupCalculateRoundTests();

        testIfPreviousOpenOrdersAreBeingHandledAndCreateNewOpenOrders(30, retailer);
    }
}
