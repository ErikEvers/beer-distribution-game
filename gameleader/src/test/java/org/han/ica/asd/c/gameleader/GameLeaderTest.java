package org.han.ica.asd.c.gameleader;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gameleader.testutil.CommunicationStub;
import org.han.ica.asd.c.gameleader.testutil.GameLogicStub;
import org.han.ica.asd.c.gameleader.testutil.PersistenceStub;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class GameLeaderTest {
    @Mock
    private Facility facil;

    private BeerGame gameTest;

    @Mock
    private Player player;

    @Mock
    private Leader leader;

    @Mock
    private Configuration con;

    private List<Facility> facilities = new ArrayList<>();

    private List<Round> rounds = new ArrayList<>();

    @Mock
    private List<Player> players;

    private IConnectorForLeader iConnectorForLeader;

    private Round facilityTurnModel;

    private ILeaderGameLogic gameLogic;

    private TurnHandler turnHandlerMock;

    private IPersistence iPersistence;

    private GameLeader gameLeader;

    private IPlayerComponent iPlayerComponent;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        gameTest = new BeerGame();
        facilityTurnModel = new Round();

        rounds.add(mock(Round.class));

        facilities.add(facil);
        facilities.add(facil);

        players.add(player);
        players.add(player);
        players.add(player);

        player.setPlayerId("1");

        con.setFacilities(facilities);
        gameTest.setConfiguration(con);

        when(con.getFacilities()).thenReturn(facilities);
        when(leader.getPlayer()).thenReturn(player);
        when(player.getPlayerId()).thenReturn("1");
        when(player.getFacility()).thenReturn(facil);

        gameTest.setRounds(rounds);
        leader.setPlayer(player);
        gameTest.setPlayers(players);
        gameTest.setLeader(leader);

        iConnectorForLeader = spy(CommunicationStub.class);
        gameLogic = spy(GameLogicStub.class);
        iPersistence = spy(PersistenceStub.class);
        turnHandlerMock = spy(TurnHandler.class);
        iPlayerComponent = spy(IPlayerComponent.class);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IConnectorForLeader.class).toInstance(iConnectorForLeader);
                bind(ILeaderGameLogic.class).toInstance(gameLogic);
                bind(IPersistence.class).toInstance(iPersistence);
                bind(TurnHandler.class).toInstance(turnHandlerMock);
                bind(IPlayerComponent.class).annotatedWith(Names.named("PlayerComponent")).toInstance(iPlayerComponent);

                bind(BeerGame.class).toProvider(() -> gameTest);
            }
        });

        gameLeader = spy(injector.getInstance(GameLeader.class));
    }

    @Test
    public void facilitiesIs2AndTurnModelReceivedIsCalledTwice_TurnsReceived_IS_Zero() {
        gameLeader.init("", new RoomModel(), new BeerGame());
        gameLeader.turnModelReceived(facilityTurnModel);
        gameLeader.turnModelReceived(facilityTurnModel);

        //Assertions.assertEquals(0, gameLeader.getTurnsReceivedInCurrentRound());
    }

    @Test
    public void facilitiesIs2AndTurnModelReceivedIsCalledOnce_TurnsReceivedIs_NOT_Zero() {
        gameLeader.init("", new RoomModel(), new BeerGame());
        gameLeader.turnModelReceived(facilityTurnModel);

        Assertions.assertNotEquals(0, gameLeader.getTurnsReceivedInCurrentRound());
    }

    @Test
    public void verifyThatMethodsAreCalled() {
        gameLeader.init("", new RoomModel(), new BeerGame());

        gameLeader.turnModelReceived(facilityTurnModel);
        gameLeader.turnModelReceived(facilityTurnModel);

//        verify(gameLogic, times(1)).calculateRound(any(Round.class));
//        verify(turnHandlerMock, times(2)).processFacilityTurn(any(Round.class), any(Round.class));
//        verify(iPersistence, times(2)).saveFacilityTurn(any(Round.class));
//        verify(iPersistence, times(1)).saveRoundData(any(Round.class));
//        verify(iConnectorForLeader, times(1)).sendRoundDataToAllPlayers(any(Round.class));
    }

    @Test
    public void notifyReconnected() {
        gameLeader.init("", new RoomModel(), new BeerGame());
        gameLeader.notifyPlayerReconnected(any(String.class));

        verify(gameLogic, times(1)).removeAgentByPlayerId(null);

        Assertions.assertNotNull(gameLeader.notifyPlayerReconnected(any(String.class)));
    }

    @Test
    public void DisconnectedNotEqual_addLocalParticipant_NotCalled() {
        List<Player> players = new ArrayList<>();

        players.add(player);
        gameTest.setPlayers(players);
        gameLeader.init("", new RoomModel(), new BeerGame());
        gameLeader.iAmDisconnected();

        verify(gameLogic, times(0)).addLocalParticipant(any(Agent.class));
    }

    @Test
    public void DisconnectedWrongTestCallPlayerIsDisconnectedMethod() {
        Facility facilityTest = mock(Facility.class);
        facilities.add(facilityTest);
        con.setFacilities(facilities);

        when(con.getFacilities()).thenReturn(facilities);
        when(player.getPlayerId()).thenReturn("b");
        when(players.get(0)).thenReturn(player);
        when(players.get(1)).thenReturn(player);
        when(players.get(2)).thenReturn(player);
        when(player.getFacility()).thenReturn(facil);

        Agent gameAgent = mock(Agent.class);
        doReturn(gameAgent).when(gameLeader).getAgentByFacility(anyInt());
        when(gameAgent.getGameAgentName()).thenReturn("test");

        gameLeader.init("", new RoomModel(), new BeerGame());
        gameLeader.playerIsDisconnected("b");

        //verify(gameLogic, times(1)).addLocalParticipant(any(Agent.class));
    }
}
