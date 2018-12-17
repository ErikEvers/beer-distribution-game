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

import java.util.ArrayList;
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
        gameLogic = new GameLogic("test", communication, persistence, participantsPool);
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
        FacilityType facilityType = new FacilityType("Manufacturer", "", 0, 0,5, 25, 500, 50);
        FacilityType facilityType1 = new FacilityType("RegionalWarehouse", "", 0, 0,5, 25, 500, 50);
        FacilityType facilityType2 = new FacilityType("Wholesale", "", 0, 0,5, 25, 500, 50);
        FacilityType facilityType3 = new FacilityType("Retailer", "", 0, 0,5, 25, 500, 50);
        FacilityType facilityType4 = new FacilityType("Demand", "", 0, 0,5, 25, 500, 50);

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

    @Test
    public void testIfCalculatingInventoryGoesCorrectly() {
        setupCalculateRoundTests();

        Round round = new RoundFake("0", 0);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
        round.addTurnOrder(wholesale, regionalWarehouse, 30);
        round.addTurnOrder(retailer, wholesale, 15);
        round.addTurnOrder(demand, retailer, 30);

        round.addFacilityStock(40, manufacturer);
        round.addFacilityStock(40, regionalWarehouse);
        round.addFacilityStock(40, wholesale);
        round.addFacilityStock(40, retailer);
        round.addFacilityStock(10000, demand);

        round = gameLogic.calculateRound(round);

        Assert.assertEquals(65, round.getStockByFacility(manufacturer));
        Assert.assertEquals(10, round.getStockByFacility(regionalWarehouse));
        Assert.assertEquals(55, round.getStockByFacility(wholesale));
        Assert.assertEquals(25, round.getStockByFacility(retailer));

        Assert.assertEquals(25, round.getTurnDeliverByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, round.getTurnDeliverByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(30, round.getTurnDeliverByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, round.getTurnDeliverByFacility(retailer, wholesale));
        Assert.assertEquals(30, round.getTurnDeliverByFacility(demand, retailer));

        Assert.assertEquals(25, round.getTurnDeliverByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, round.getTurnDeliverByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(30, round.getTurnDeliverByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, round.getTurnDeliverByFacility(retailer, wholesale));
        Assert.assertEquals(30, round.getTurnDeliverByFacility(demand, retailer));

        Assert.assertEquals(25, round.getTurnReceivedByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, round.getTurnReceivedByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(30, round.getTurnReceivedByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, round.getTurnReceivedByFacility(retailer, wholesale));
        Assert.assertEquals(30, round.getTurnReceivedByFacility(demand, retailer));

        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(manufacturer));
        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(regionalWarehouse));
        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(wholesale));
        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(retailer));
        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(demand));
    }

    @Test
    public void testIfBacklogAndDeliverCalculationGoesCorrectly() {
        setupCalculateRoundTests();

        Round round = new RoundFake("0", 0);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
        round.addTurnOrder(wholesale, regionalWarehouse, 150);
        round.addTurnOrder(retailer, wholesale, 15);
        round.addTurnOrder(demand, retailer, 30);

        round.addFacilityStock(40, manufacturer);
        round.addFacilityStock(40, regionalWarehouse);
        round.addFacilityStock(40, wholesale);
        round.addFacilityStock(40, retailer);
        round.addFacilityStock(10000, demand);

        round = gameLogic.calculateRound(round);

        Assert.assertEquals(65, round.getStockByFacility(manufacturer));
        Assert.assertEquals( 0, round.getStockByFacility(regionalWarehouse));
        Assert.assertEquals(65, round.getStockByFacility(wholesale));
        Assert.assertEquals(25, round.getStockByFacility(retailer));

        Assert.assertEquals(25, round.getTurnDeliverByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, round.getTurnDeliverByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(40, round.getTurnDeliverByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, round.getTurnDeliverByFacility(retailer, wholesale));
        Assert.assertEquals(30, round.getTurnDeliverByFacility(demand, retailer));

        Assert.assertEquals(25, round.getTurnReceivedByFacility(manufacturer, manufacturer));
        Assert.assertEquals( 0, round.getTurnReceivedByFacility(regionalWarehouse, manufacturer));
        Assert.assertEquals(40, round.getTurnReceivedByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(15, round.getTurnReceivedByFacility(retailer, wholesale));
        Assert.assertEquals(30, round.getTurnReceivedByFacility(demand, retailer));

        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(manufacturer));
        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(regionalWarehouse));
        Assert.assertEquals(110, round.getTurnBacklogByFacility(wholesale, regionalWarehouse));
        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(retailer));
        Assert.assertEquals(false, round.isTurnBackLogfilledByFacility(demand));
    }

    @Test
    public void testIfCalculatingRemainingBudgetGoesCorrectly() {
        setupCalculateRoundTests();

        Round round = new RoundFake("0", 0);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(regionalWarehouse, manufacturer, 0);
        round.addTurnOrder(wholesale, regionalWarehouse, 50);
        round.addTurnOrder(retailer, wholesale, 35);
        round.addTurnOrder(demand, retailer, 30);

        round.addFacilityStock(40, manufacturer);
        round.addFacilityStock(40, regionalWarehouse);
        round.addFacilityStock(40, wholesale);
        round.addFacilityStock(40, retailer);
        round.addFacilityStock(10000, demand);

        round.addFacilityRemainingStock(500, manufacturer);
        round.addFacilityRemainingStock(500, regionalWarehouse);
        round.addFacilityRemainingStock(500, wholesale);
        round.addFacilityRemainingStock(500, retailer);

        round = gameLogic.calculateRound(round);

        Assert.assertEquals(65, round.getStockByFacility(manufacturer));
        Assert.assertEquals(0, round.getStockByFacility(regionalWarehouse));
        Assert.assertEquals(45, round.getStockByFacility(wholesale));
        Assert.assertEquals(45, round.getStockByFacility(retailer));
        Assert.assertEquals(10, round.getTurnBacklogByFacility(wholesale, regionalWarehouse));

        //stockcost = 65 * 5 = 325; backlogcost = 0 * 25 = 0; remaining budget = 500 - 325 - 0 = 175
        Assert.assertEquals(175, round.getRemainingBudget(manufacturer));

        //stockcost = 0 * 5 = 0; backlogcost = 10 * 25 = 250; remaining budget = 500 - 250 - 0 = 250
        Assert.assertEquals(250, round.getRemainingBudget(regionalWarehouse));

        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0;  remaining budget = 500 - 225 - 0 = 275
        Assert.assertEquals(275, round.getRemainingBudget(wholesale));

        //stockcost = 45 * 5 = 225; backlogcost = 0 * 25 = 0;  remaining budget = 500 - 225 - 0 = 275
        Assert.assertEquals(275, round.getRemainingBudget(retailer));
    }
}
