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

    @Test
    public void testIfCalculatingInventoryGoesCorrectly() {
        Facility manufacturer = new Facility("0", 0, "0", "0", "0");
        Facility distributor = new Facility("0", 0, "0", "0", "0");
        Facility wholesale = new Facility("0", 0, "0", "0", "0");
        Facility retailer = new Facility("0", 0, "0", "0", "0");

        FacilityLinkedTo facilityLinkedTo = new FacilityLinkedTo("0", manufacturer, manufacturer, true);
        FacilityLinkedTo facilityLinkedTo1 = new FacilityLinkedTo("0", manufacturer, distributor, true);
        FacilityLinkedTo facilityLinkedTo2 = new FacilityLinkedTo("0", distributor, wholesale, true);
        FacilityLinkedTo facilityLinkedTo3 = new FacilityLinkedTo("0", wholesale, retailer, true);

        gameLogic.addfacilities(facilityLinkedTo);
        gameLogic.addfacilities(facilityLinkedTo1);
        gameLogic.addfacilities(facilityLinkedTo2);
        gameLogic.addfacilities(facilityLinkedTo3);

        Round round = new RoundFake("0", 0);
        round.addTurnOrder(manufacturer, manufacturer, 25);
        round.addTurnOrder(manufacturer, distributor, 0);
        round.addTurnOrder(distributor, wholesale, 30);
        round.addTurnOrder(wholesale, retailer, 15);

        round.addFacilityStock(40, manufacturer);
        round.addFacilityStock(40, distributor);
        round.addFacilityStock(40, wholesale);
        round.addFacilityStock(40, retailer);

        round = gameLogic.calculateRound(round);

        Assert.assertEquals(round.getStockByFacility(manufacturer), 15);
        Assert.assertEquals(round.getTurnDeliverByFacility(manufacturer, manufacturer), 25);
    }
}
