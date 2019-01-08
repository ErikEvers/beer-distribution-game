package org.han.ica.asd.c.gamelogic;

import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.gamelogic.participants.domain_models.PlayerParticipant;
import org.han.ica.asd.c.gamelogic.participants.fakes.PlayerFake;
import org.han.ica.asd.c.interfaces.gamelogic.IPersistence;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Round turn = new Round();
        //FacilityTurnDB turn = new FacilityTurnDB("", 0, 0, 0, 0, 0, 0, 0, 0);
        gameLogic.placeOrder(turn);
        verify(persistence, times(1)).saveTurnData(turn);
    }

    @Test
    public void placeOrderCallsCommunication() {
        Round turn = new Round();
        //FacilityTurnDB turn = new FacilityTurnDB("", 0, 0, 0, 0, 0, 0, 0, 0);
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
    public void getAllGamesCallsMethodOfSameNameOnceInIConnectedForPlayer() {
        //Act
        gameLogic.getAllGames();

        //Assert
        verify(communication, times(1)).getAllGames();
    }

    @Test
    public void connectToGameCallsMethodOfSameNameOnceInIConnectedForPlayer() {
        //Act
        gameLogic.connectToGame("");

        //Assert
        verify(communication, times(1)).connectToGame("");
    }

    @Test
    public void requestFacilityUsageCallsMethodOfSameNameOnceInIConnectedForPlayer() {
        //Arrange
        Facility facility = mock(Facility.class);

        //Act
        gameLogic.requestFacilityUsage(facility);

        //Assert
        verify(communication, times(1)).requestFacilityUsage(facility);
    }
}
