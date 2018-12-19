package org.han.ica.asd.c.gamelogic;
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
    public void setup() {
        communication = mock(IConnectedForPlayer.class);
        persistence = mock(IPersistence.class);
        participantsPool = mock(ParticipantsPool.class);
        gameLogic = new GameLogic("test", communication, persistence, participantsPool, new BeerGame("0", "", "", ""));
    }

    @Test
    public void placeOrderCallsPersistence() {
        Map<Facility, Map<Facility, Integer>> turn = new HashMap<>();
        gameLogic.placeOrder(turn);
        verify(persistence, times(1)).saveTurnData(turn);
    }

    @Test
    public void placeOrderCallsCommunication() {
        Map<Facility, Map<Facility, Integer>> turn = new HashMap<>();
        gameLogic.placeOrder(turn);
        verify(communication, times(1)).sendTurnData(turn);
    }

    @Test
    public void seeOtherFacilitiesCallsPersistence() {
        gameLogic.seeOtherFacilities();
        verify(persistence, times(1)).fetchRoundData(anyString(), anyInt());
    }

    @Test
    public void letAgentTakeOverPlayerReplacesPlayer() {
        gameLogic.letAgentTakeOverPlayer(mock(AgentParticipant.class));
        verify(participantsPool, times(1)).replacePlayerWithAgent(any());
    }

    @Test
    public void letPlayerTakeOverAgentReplacesAgent() {
        gameLogic.letPlayerTakeOverAgent();
        verify(participantsPool, times(1)).replaceAgentWithPlayer();
    }

    @Test
    public void addLocalParticipantCallsParticipantsPool() {
        IParticipant participant = mock(IParticipant.class);
        gameLogic.addLocalParticipant(participant);
        verify(participantsPool, times(1)).addParticipant(participant);
    }

    @Test
    public void removeAgentByPlayerIdGetsPlayerFromDatabase() {
        when(persistence.getPlayerById(anyString())).thenReturn(new PlayerFake());
        gameLogic.removeAgentByPlayerId(anyString());
        verify(persistence, times(1)).getPlayerById(anyString());
    }

    @Test
    public void removeAgentByPlayerIdReplacesAgentAtParticipantsPool() {
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

    @Test
    public void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForStockByFacility() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithoutBacklog();

        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(65, calculatedRound.getStockByFacility(manufacturer));
        Assert.assertEquals(10, calculatedRound.getStockByFacility(regionalWarehouse));
        Assert.assertEquals(55, calculatedRound.getStockByFacility(wholesale));
        Assert.assertEquals(25, calculatedRound.getStockByFacility(retailer));
    }

    @Test
    public void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnDeliverByFacility() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithoutBacklog();

        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(25, calculatedRound.getTurnDeliverByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, calculatedRound.getTurnDeliverByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(30, calculatedRound.getTurnDeliverByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, calculatedRound.getTurnDeliverByFacility(retailer, wholesale));
        Assert.assertEquals(30, calculatedRound.getTurnDeliverByFacility(demand, retailer));
    }

    @Test
    public void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnReceivedByFacility() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithoutBacklog();

        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(25, calculatedRound.getTurnReceivedByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, calculatedRound.getTurnReceivedByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(30, calculatedRound.getTurnReceivedByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, calculatedRound.getTurnReceivedByFacility(retailer, wholesale));
        Assert.assertEquals(30, calculatedRound.getTurnReceivedByFacility(demand, retailer));
    }

    @Test
    public void testIfCalculatingInventoryGoesCorrectlyWithoutBacklogForTurnBackLogByFacility() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithoutBacklog();

        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(manufacturer));
        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(regionalWarehouse));
        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(wholesale));
        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(retailer));
        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(demand));
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

    @Test
    public void testIfCalculatingInventoryGoesCorrectlyWithBacklogForStockByFacility() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(65, calculatedRound.getStockByFacility(manufacturer));
        Assert.assertEquals( 0, calculatedRound.getStockByFacility(regionalWarehouse));
        Assert.assertEquals(65, calculatedRound.getStockByFacility(wholesale));
        Assert.assertEquals(25, calculatedRound.getStockByFacility(retailer));
    }

    @Test
    public void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnDeliverByFacility() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(25, calculatedRound.getTurnDeliverByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, calculatedRound.getTurnDeliverByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(40, calculatedRound.getTurnDeliverByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, calculatedRound.getTurnDeliverByFacility(retailer, wholesale));
        Assert.assertEquals(30, calculatedRound.getTurnDeliverByFacility(demand, retailer));
    }

    @Test
    public void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnReceivedByFacility() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(25, calculatedRound.getTurnReceivedByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, calculatedRound.getTurnReceivedByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(40, calculatedRound.getTurnReceivedByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, calculatedRound.getTurnReceivedByFacility(retailer, wholesale));
        Assert.assertEquals(30, calculatedRound.getTurnReceivedByFacility(demand, retailer));
    }

    @Test
    public void testIfCalculatingInventoryGoesCorrectlyWithBacklogForTurnBacklogByFacility() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectWithBacklog();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(manufacturer));
        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(regionalWarehouse));
        Assert.assertEquals(110, calculatedRound.getTurnBacklogByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(retailer));
        Assert.assertEquals(false, calculatedRound.isTurnBackLogfilledByFacility(demand));
    }

    public Round setupPreviousRoundObjectForBudgetCalculation() {
        Round round = new RoundFake("0", 0);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
        round.addTurnOrder(wholesale, regionalWarehouse, 50);
        round.addTurnOrder(retailer, wholesale, 35);
        round.addTurnOrder(demand, retailer, 30);

        return round;
    }

    @Test
    public void testIfCalculatingRemainingBudgetGoesCorrectly() {
        setupCalculateRoundTests();

        Round previousRound = setupPreviousRoundObjectForBudgetCalculation();
        gameLogic.getBeerGame().addRound(previousRound);

        Round calculatedRound = setupCalculatedRoundObject();

        calculatedRound = gameLogic.calculateRound(calculatedRound);

        //stockcost = 65 * 5 = 325; backlogcost = 0 * 25 = 0; OutgoingOrders = 25 * 3 = 75; Incoming orders = 0 * 3 = 0;
        //remaining budget = 500 - 325 - (75 - 0) = 100
        Assert.assertEquals(100, calculatedRound.getRemainingBudget(manufacturer));

        //stockcost = 0 * 5 = 0; backlogcost = 10 * 25 = 250; OutgoingOrders = 0 * 4 = 0; incomingorders = 40 * 4 = 160;
        //remaining budget = (500 - 250) - (0 - 160) = 410
        Assert.assertEquals(410, calculatedRound.getRemainingBudget(regionalWarehouse));

        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0; OutgoingOrders = 40 * 5 = 200; Incoming orders = 35 * 5 = 175;
        //remaining budget = (500 - 225) - (200 - 175) = 250
        Assert.assertEquals(250, calculatedRound.getRemainingBudget(wholesale));

        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0; OutgoingOrders = 35 * 6 = 210; Incoming orders = 30 * 6 = 180;
        //remaining budget = (500 - 225) - (210 - 180) - 0 = 245
        Assert.assertEquals(245, calculatedRound.getRemainingBudget(retailer));
    }
}
