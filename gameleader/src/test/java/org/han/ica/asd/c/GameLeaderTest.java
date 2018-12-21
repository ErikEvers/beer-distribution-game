package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.domain_objects.*;
import org.han.ica.asd.c.observers.ITurnModelObserver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.InstanceOf;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.*;

public class GameLeaderTest {
    private ITurnModelObserver iTurnModelObserver;

    //@Mock
    private ILeaderGameLogic gameLogic;

    //@Mock
    private TurnHandler turnHandlerMock;

    //@Mock
    private IPersistence iPersistence;

    //private Configuration con;

    @Mock
    private Facility facil;

    @Mock
    private BeerGame gameTest;

    //private String t;

    @Mock
    private Round r;

    @Mock
    private Provider<BeerGame> beerGameProvider;

    @Mock
    private Provider<Round> roundProvider;

    //@Mock
    private IConnectorForLeader iConnectorForLeader;

    private GameLeader gameLeader;

    private Round facilityTurnModel;

    @Mock
    private Player player;
    @Mock
    private Leader leader;
    @Mock
    private Configuration con;


    private Round round = new Round();

    List<Facility> facilities = new ArrayList<>();
    List<Round> rounds;
    List<Player> players;

//    public GameLeaderTest() {
//        roundProvider = null;

//    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        gameTest = new BeerGame();

        //facilities = new ArrayList<>();
        rounds = new ArrayList<>();
        players = new ArrayList<>();
        rounds.add(r);

        players.add(player);
        players.add(player);
        players.add(player);

//        player.setPlayerId("1");
//        facil.setPlayer(player);

        facilities.add(facil);
        facilities.add(facil);

        //facilities.get(0).getPlayer().setPlayerId("1");

        //Configuration con = mock(Configuration.class);

        con.setFacilities(facilities);



        gameTest.setConfiguration(con);

        when(con.getFacilities()).thenReturn(facilities);
        when(leader.getPlayer()).thenReturn(player);
        when(player.getPlayerId()).thenReturn("1");
        //when(facilities.get(0).getPlayer().getPlayerId()).thenReturn("1");

//        facilities.set(0, facil);
//
//        facilities.get(0).setPlayer(player);
//
////        facilities.get(0).getPlayer().setPlayerId("1");
//
//        players.set(0, player);

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
//                bind(BeerGame.class).toProvider(beerGameProvider);
//                bind(Round.class).toProvider(roundProvider);
            }
        });
        gameLeader = injector.getInstance(GameLeader.class);

        facilityTurnModel = new Round();
    }

//    @Test
//    public void testGameLeader_CreateUsingConstructor_Created() {
//        facilities.add(facil);
//
//        BeerGame beerGame = new BeerGame("test", "test", "test", "test");
//
//        Configuration con = mock(Configuration.class);
//
//        con.setFacilities(facilities);
//
//        beerGame.setConfiguration(con);
//
//
//        new GameLeader(beerGame, iConnectorForLeader);
//    }
//
//    @Test
//    public void testGameLeaderHasNotGottenAllDataSoNextMethodNotCalled() {
//        rounds.add(r);
//        facilities.add(facil);
//
//        BeerGame beerGame = new BeerGame("test", "test", "test", "test");
//
//        Configuration con = mock(Configuration.class);
//
//        con.setFacilities(facilities);
//
//        beerGame.setConfiguration(con);
//
//        gameLeader = new GameLeader(beerGame, iConnectorForLeader);
//
//        //gameLeader.setTurnHandler(turnHandlerMock);
//
//        gameLeader.turnModelReceived(facilityTurnModel);
//
//        verify(turnHandlerMock, times(1)).processFacilityTurn(facilityTurnModel);
//
//        System.out.println(gameLeader.getTurnsExpectedPerRound());
//        System.out.println(gameLeader.getTurnsReceivedInCurrentRound());
//
//        Assert.assertNotEquals(gameLeader.getTurnsExpectedPerRound(), gameLeader.getTurnsReceivedInCurrentRound());
//    }

    @Test
    public void facilitiesIs2AndTurnModelReceivedIsCalledTwice_TurnsReceived_IS_Zero() {
        gameLeader.init();

        int roundBefore = gameLeader.getTurnsExpectedPerRound();

        gameLeader.turnModelReceived(facilityTurnModel);
        gameLeader.turnModelReceived(facilityTurnModel);

        int roundAfter = gameLeader.getTurnsReceivedInCurrentRound();

        System.out.println(roundBefore);
        System.out.println(roundAfter);

        Assert.assertEquals(roundAfter, 0);
    }

    @Test
    public void facilitiesIs2AndTurnModelReceivedIsCalledOnce_TurnsReceivedIs_NOT_Zero() {
        gameLeader.init();

        int roundBefore = gameLeader.getTurnsExpectedPerRound();

        gameLeader.turnModelReceived(facilityTurnModel);

        int roundAfter = gameLeader.getTurnsReceivedInCurrentRound();

        System.out.println(roundBefore);
        System.out.println(roundAfter);

        Assert.assertNotEquals(roundAfter, 0);
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
    public void DisconnectedTest() {
        gameLeader.init();

        gameLeader.iAmDisconnected();

        //verify(this.gameLeader, times(1)).playerIsDisconnected(null);

    }

    @Test
    public void DisconnectedWrongTestCallPlayerIsDisconnectedMethod() {
        Player playerTest = mock(Player.class);

        Facility facility = mock(Facility.class);

        playerTest.setPlayerId("b");

        players.add(playerTest);

        gameTest.setPlayers(players);

        Facility facilityTest = mock(Facility.class);

        List<Facility> facilitiesTest = new ArrayList<>();

        facility.setPlayer(playerTest);

        facilities.add(facility);
        facilities.get(0).setPlayer(playerTest);
        facilities.add(facil);

        //facilitiesTest.add(facilityTest);
        //facilities.add(facil);

        when(con.getFacilities()).thenReturn(facilities);
        when(facilities.get(0).getPlayer()).thenReturn(player);
        when(playerTest.getPlayerId()).thenReturn("b");
        //when(con.getFacilities().get(anyInt()).getPlayer().getPlayerId()).thenReturn("1");

        gameLeader.init();

        gameLeader.iAmDisconnected();

        verify(gameLogic, times(1)).addLocalParticipant(null);

        verify(this.gameLeader, times(1)).playerIsDisconnected(null);

    }

    @Test
    public void playerIsDisconnectedTest() {
//        Facility facilityTest = mock(Facility.class);
//
//        List<Facility> facilitiesTest = new ArrayList<>();
//
//        facilitiesTest.add(facilityTest);
//
//        when(con.getFacilities()).thenReturn(facilitiesTest);
//        when(facilitiesTest.get(0).getPlayer()).thenReturn(player);
//        when(player.getPlayerId()).thenReturn("b");
//
//        when(gameTest.getConfiguration()).thenReturn(con);
//        when(con.getFacilities()).thenReturn(facilities);
//        when(facilities.size()).thenReturn(1);
//
//        gameLeader.playerIsDisconnected("1");
    }
}
