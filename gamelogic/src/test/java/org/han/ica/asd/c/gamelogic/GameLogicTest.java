package org.han.ica.asd.c.gamelogic;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


public class GameLogicTest {
    private GameLogic gameLogic;
    private ParticipantsPool participantsPool;
    private IConnectedForPlayer communication;
    private IGameStore persistence;
    private BeerGame beerGameMock;

    @BeforeEach
    public void setup() {
        communication = mock(IConnectedForPlayer.class);
        persistence = mock(IGameStore.class);
        participantsPool = mock(ParticipantsPool.class);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IGameStore.class).toInstance(persistence);
                bind(IConnectedForPlayer.class).toInstance(communication);
            }
        });
        gameLogic = injector.getInstance(GameLogic.class);
        gameLogic.setParticipantsPool(participantsPool);

        beerGameMock = mock(BeerGame.class);
        gameLogic.gameStartReceived(beerGameMock);
    }

    @Test
    public void submitTurnCallsPersistence() {
        Round turn = new Round();
        //FacilityTurnDB turn = new FacilityTurnDB("", 0, 0, 0, 0, 0, 0, 0, 0);
        gameLogic.submitTurn(turn);
        verify(persistence, times(1)).saveRoundData(turn);
    }

    @Test
    public void submitTurnCallsCommunication() {
        Round turn = new Round();
        //FacilityTurnDB turn = new FacilityTurnDB("", 0, 0, 0, 0, 0, 0, 0, 0);
        gameLogic.submitTurn(turn);
        verify(communication, times(1)).sendTurnData(turn);
    }

    @Test
    public void seeOtherFacilitiesReturnsBeerGame() {
        Assert.assertEquals(beerGameMock, gameLogic.seeOtherFacilities());
    }

    @Test
    public void letAgentTakeOverPlayerReplacesPlayer() {
        gameLogic.letAgentTakeOverPlayer(mock(Agent.class));
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

    /* TODO: Fix these tests and the code for these tests.
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
        verify(participantsPool, times(1)).replaceAgentWithPlayer(any(PlayerFake.class));
    }*/

    @Test
    public void roundModelReceivedSavesOldRoundToDatabase() {
        gameLogic.roundModelReceived(mock(Round.class));
        verify(persistence, times(1)).saveRoundData(any());
    }

    @Test
    public void roundModelReceivedIncrementsRound() {
        int currentRoundNumber = gameLogic.getRound();
        gameLogic.roundModelReceived(mock(Round.class));
        int newRoundNumber = gameLogic.getRound();
        Assert.assertEquals(currentRoundNumber + 1, newRoundNumber);
    }

    @Test
    public void roundModelReceivedCallsLocalParticipants() {
        gameLogic.roundModelReceived(mock(Round.class));
        verify(participantsPool, times(1)).excecuteRound(any(Round.class));
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

    @Test
    public void getAllFacilitiesCallsMethodOfSameNameOnceInIConnectedForPlayer() {
        //Act
        gameLogic.getAllFacilities();

        //Assert
        verify(communication, times(1)).getAllFacilities();
    }

    @Test
    public void selectAgentCallsSaveSelectedAgentOnceInIPersistence() {
        //Arrange
        ProgrammedAgent programmedAgent = mock(ProgrammedAgent.class);

        //Act
        gameLogic.selectAgent(programmedAgent);

        //Assert
        verify(persistence, times(1)).saveSelectedAgent(programmedAgent);
    }

    @Test
    public void selectAgentCallsSendSelectedAgentOnceInIConnectedForPlayer() {
        //Arrange
        ProgrammedAgent programmedAgent = mock(ProgrammedAgent.class);

        //Act
        gameLogic.selectAgent(programmedAgent);

        //Assert
        verify(communication, times(1)).sendSelectedAgent(programmedAgent);
    }
}
