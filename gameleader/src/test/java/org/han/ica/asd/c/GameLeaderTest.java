package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.model.domain_objects.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.*;

public class GameLeaderTest {
    @Mock
    private Facility facil;

    @Mock
    private BeerGame gameTest;

    @Mock
    private Round r;

    @Mock
    private Player player;

    @Mock
    private Leader leader;

    @Mock
    private Configuration con;

    private Round round = new Round();

    private List<Facility> facilities = new ArrayList<>();

    private List<Round> rounds = new ArrayList<>();

    private List<Player> players = new ArrayList<>();

    private IConnectorForLeader iConnectorForLeader;

    private GameLeader gameLeader;

    private Round facilityTurnModel;

    private ILeaderGameLogic gameLogic;

    private TurnHandler turnHandlerMock;

    private IPersistence iPersistence;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        gameTest = new BeerGame();
        facilityTurnModel = new Round();

        rounds.add(r);

        facilities.add(facil);
        facilities.add(facil);

        players.add(player);
        players.add(player);
        players.add(player);

        player.setPlayerId("1");

        facilities.get(0).setPlayer(player);
        facilities.get(1).setPlayer(player);

        con.setFacilities(facilities);
        gameTest.setConfiguration(con);

        when(con.getFacilities()).thenReturn(facilities);
        when(leader.getPlayer()).thenReturn(player);
        when(player.getPlayerId()).thenReturn("1");

        gameTest.setRounds(rounds);
        leader.setPlayer(player);
        gameTest.setPlayers(players);
        gameTest.setLeader(leader);

        iConnectorForLeader = mock(IConnectorForLeader.class);
        gameLogic = mock(ILeaderGameLogic.class);
        iPersistence = mock(IPersistence.class);
        turnHandlerMock = mock(TurnHandler.class);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IConnectorForLeader.class).toInstance(iConnectorForLeader);
                bind(ILeaderGameLogic.class).toInstance(gameLogic);
                bind(IPersistence.class).toInstance(iPersistence);
                bind(TurnHandler.class).toInstance(turnHandlerMock);
                bind(BeerGame.class).toInstance(gameTest);
                bind(Round.class).toInstance(round);
            }
        });
        gameLeader = injector.getInstance(GameLeader.class);
    }

    @Test
    public void facilitiesIs2AndTurnModelReceivedIsCalledTwice_TurnsReceived_IS_Zero() {
        gameLeader.init();
        gameLeader.turnModelReceived(facilityTurnModel);
        gameLeader.turnModelReceived(facilityTurnModel);

        Assert.assertEquals(gameLeader.getTurnsReceivedInCurrentRound(), 0);
    }

    @Test
    public void facilitiesIs2AndTurnModelReceivedIsCalledOnce_TurnsReceivedIs_NOT_Zero() {
        gameLeader.init();
        gameLeader.turnModelReceived(facilityTurnModel);

        Assert.assertNotEquals(gameLeader.getTurnsReceivedInCurrentRound(), 0);
    }

    @Test
    public void verifyThatMethodsAreCalled() {
        gameLeader.init();

        gameLeader.turnModelReceived(facilityTurnModel);
        gameLeader.turnModelReceived(facilityTurnModel);

        verify(gameLogic, times(1)).calculateRound(null);
        verify(iPersistence, times(1)).saveRoundData(null);
        verify(iConnectorForLeader, times(1)).sendRoundDataToAllPlayers(null);
    }

    @Test
    public void notifyReconnected() {
        gameLeader.init();
        gameLeader.notifyPlayerReconnected(any(String.class));

        verify(gameLogic, times(1)).removeAgentByPlayerId(null);

        Assert.assertThat(gameLeader.notifyPlayerReconnected(any(String.class)), instanceOf(BeerGame.class));
    }

    @Test
    public void DisconnectedNotEqual_addLocalParticipant_NotCalled() {
        gameLeader.init();
        gameLeader.iAmDisconnected();

        verify(gameLogic, times(0)).addLocalParticipant(any(AgentParticipant.class));
    }

    @Test
    public void DisconnectedWrongTestCallPlayerIsDisconnectedMethod() {
        Player playerTest = mock(Player.class);
        Facility facility = mock(Facility.class);
        playerTest.setPlayerId("b");
        players.add(playerTest);
        Facility facilityTest = mock(Facility.class);
        facility.setPlayer(playerTest);
        facilities.add(facilityTest);
        facilities.get(2).setPlayer(playerTest);
        con.setFacilities(facilities);

        when(con.getFacilities()).thenReturn(facilities);
        when(facilities.get(0).getPlayer()).thenReturn(player);
        when(facilities.get(1).getPlayer()).thenReturn(player);
        //when(facilities.get(2).getPlayer()).thenReturn(player);
        when(player.getPlayerId()).thenReturn("b");

        GameAgent gameAgent = mock(GameAgent.class);
        when(facilities.get(0).getAgent()).thenReturn(gameAgent);
        when(facilities.get(1).getAgent()).thenReturn(gameAgent);
        when(facilities.get(2).getAgent()).thenReturn(gameAgent);
        when(gameAgent.getGameAgentName()).thenReturn("test");

        gameLeader.init();
        gameLeader.playerIsDisconnected("b");

        verify(gameLogic, times(3)).addLocalParticipant(any(AgentParticipant.class));
    }
}
