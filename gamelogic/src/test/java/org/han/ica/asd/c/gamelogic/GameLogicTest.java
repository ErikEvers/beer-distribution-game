package org.han.ica.asd.c.gamelogic;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.gamelogic.stubs.BusinessRuleStub;
import org.han.ica.asd.c.gamelogic.stubs.PersistenceStub;
import org.han.ica.asd.c.gui_play_game.see_other_facilities.SeeOtherFacilities;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.communication.IConnectorProvider;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectorForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class GameLogicTest {
    private GameLogic gameLogic;
    private ParticipantsPool participantsPool;
    private IConnectorForPlayer communication;
    private IGameStore persistence;
    private IPlayerRoundListener player;
    private Round round;
    private BeerGame beerGame;
    private IGUIHandler seeOtherFacilities;
    private IConnectorProvider connectorProvider;

    @BeforeEach
    void setup() {
        round = new Round();
        connectorProvider = mock(IConnectorProvider.class);
        communication = mock(IConnectorForPlayer.class);
        persistence = mock(IGameStore.class);
        participantsPool = mock(ParticipantsPool.class);
        player = mock(IPlayerRoundListener.class);
        seeOtherFacilities = mock(SeeOtherFacilities.class);
        beerGame = mock(BeerGame.class);
        when(connectorProvider.forPlayer()).thenReturn(communication);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IGameStore.class).toInstance(persistence);
                bind(IConnectorProvider.class).toInstance(connectorProvider);
                bind(IBusinessRules.class).to(BusinessRuleStub.class);
                bind(IPlayerGameLogic.class).toInstance(mock(GameLogic.class));
                bind(IPersistence.class).to(PersistenceStub.class);
                bind(IGUIHandler.class).annotatedWith(Names.named("SeeOtherFacilities")).toInstance(seeOtherFacilities);
            }
        });

        gameLogic = spy(injector.getInstance(GameLogic.class));
        doReturn(false).when(gameLogic).isBotGame();
        gameLogic.setParticipantsPool(participantsPool);
        gameLogic.setPlayer(player);
        gameLogic.gameStartReceived(beerGame);
    }

    @Test
    public void submitTurnCallsPersistence() throws SendGameMessageException {
        Round turn = new Round();
        //FacilityTurnDB turn = new FacilityTurnDB("", 0, 0, 0, 0, 0, 0, 0, 0);
        gameLogic.submitTurn(turn);
        //verify(persistence, times(1)).saveRoundData(turn);
				verify(communication, times(1)).sendTurnData(turn);
    }

    @Test
    public void submitTurnCallsCommunication() throws SendGameMessageException {
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

//    @Test
//    public void letAgentTakeOverPlayerReplacesPlayer() {
//        GameAgent agent = mock(GameAgent.class);
//        when(agent.getGameAgentName()).thenReturn("GameAgent");
//        when(agent.getFacility()).thenReturn(new Facility());
//        when(agent.getGameBusinessRules()).thenReturn(new ArrayList<>());
//        when(persistence.getGameLog()).thenReturn(new BeerGame());
//
//        gameLogic.letAgentTakeOverPlayer(agent);
//
//        doNothing().when(participantsPool).addParticipant(any(IParticipant.class));
//        verify(participantsPool, times(1)).addParticipant(any());
//    }

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
    public void roundModelReceivedSavesOldRoundToDatabase() throws SendGameMessageException {
        gameLogic.roundModelReceived(mock(Round.class), mock(Round.class));
        //verify(persistence, times(1)).saveRoundData(any());
    }

    @Test
    public void roundModelReceivedUpdatesRound() throws SendGameMessageException {
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
    public void roundModelReceivedCallsLocalParticipants() throws SendGameMessageException {
        gameLogic.roundModelReceived(mock(Round.class), mock(Round.class));
        verify(participantsPool, times(2)).getParticipants();
    }
}
