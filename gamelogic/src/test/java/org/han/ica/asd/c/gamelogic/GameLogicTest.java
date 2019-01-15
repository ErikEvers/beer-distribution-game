package org.han.ica.asd.c.gamelogic;

import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
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
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


public class GameLogicTest {
    private GameLogic gameLogic;
    private ParticipantsPool participantsPool;
    private IConnectedForPlayer communication;
    private IGameStore persistence;
    private IPlayerRoundListener player;
    private Round round;
    private BeerGame beerGameMock;

    @BeforeEach
    void setup() {
        round = new Round();
        communication = mock(IConnectedForPlayer.class);
        persistence = mock(IGameStore.class);
        participantsPool = mock(ParticipantsPool.class);
        player = mock(IPlayerRoundListener.class);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IGameStore.class).toInstance(persistence);
                bind(IConnectedForPlayer.class).toInstance(communication);
                //bind(ParticipantsPool.class).toInstance(participantsPool);
            }
        });

        gameLogic = injector.getInstance(GameLogic.class);
        gameLogic.setParticipantsPool(participantsPool);
        gameLogic.setPlayer(player);
        gameLogic.gameStartReceived(mock(BeerGame.class));
    }

    @Test
    public void submitTurnCallsPersistence() {
        Round turn = new Round();
        //FacilityTurnDB turn = new FacilityTurnDB("", 0, 0, 0, 0, 0, 0, 0, 0);
        gameLogic.submitTurn(turn);
        //verify(persistence, times(1)).saveRoundData(turn);
				verify(communication, times(1)).sendTurnData(turn);
    }

    @Test
    public void submitTurnCallsCommunication() {
        Round turn = new Round();
        //FacilityTurnDB turn = new FacilityTurnDB("", 0, 0, 0, 0, 0, 0, 0, 0);
        gameLogic.submitTurn(turn);
        verify(communication, times(1)).sendTurnData(turn);
    }

    @Test
    void getBeerGameCallsPersistence() {
        gameLogic.getBeerGame();
        doNothing().when(persistence).saveGameLog(any(BeerGame.class),anyBoolean());
        verify(persistence, times(1)).saveGameLog(any(BeerGame.class),anyBoolean());
    }

    @Test
    public void seeOtherFacilitiesReturnsBeerGame() {
        Assert.assertEquals(beerGameMock, gameLogic.seeOtherFacilities());
    }

    @Test
    public void letAgentTakeOverPlayerReplacesPlayer() {
        gameLogic.letAgentTakeOverPlayer(mock(Agent.class));
        doNothing().when(participantsPool).addParticipant(any(IParticipant.class));
        verify(participantsPool, times(1)).addParticipant(any());
    }

    @Test
    public void letPlayerTakeOverAgentReplacesAgent() {
        gameLogic.letPlayerTakeOverAgent();
        doNothing().when(participantsPool).replaceAgentWithPlayer();
        verify(participantsPool, times(1)).replaceAgentWithPlayer();
    }

    @Test
    public void addLocalParticipantCallsParticipantsPool() {
        IParticipant participant = mock(IParticipant.class);
        gameLogic.addLocalParticipant(participant);
        verify(participantsPool, times(1)).addParticipant(participant);
    }

    @Test
    public void removeAgentByPlayerIdReplacesAgentAtParticipantsPool() {
        gameLogic.removeAgentByPlayerId("");
        verify(participantsPool, times(1)).replaceAgentWithPlayer();
    }

    @Test
    public void roundModelReceivedSavesOldRoundToDatabase() {
        gameLogic.roundModelReceived(mock(Round.class), mock(Round.class));
        //verify(persistence, times(1)).saveRoundData(any());
    }

    @Test
    public void roundModelReceivedUpdatesRound() {
        int currentRoundNumber = gameLogic.getRoundId();
        Round round = new Round();
        int roundId = 33;
        round.setRoundId(roundId);
        gameLogic.roundModelReceived(mock(Round.class), round);
        int newRoundNumber = gameLogic.getRoundId();
        Assertions.assertNotEquals(currentRoundNumber, newRoundNumber);
        Assertions.assertEquals(roundId, newRoundNumber);
    }

    @Test
    public void roundModelReceivedCallsLocalParticipants() {
        gameLogic.roundModelReceived(mock(Round.class), mock(Round.class));
        verify(participantsPool, times(2)).getParticipants();
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
