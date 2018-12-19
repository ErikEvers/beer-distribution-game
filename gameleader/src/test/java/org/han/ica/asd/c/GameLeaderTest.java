package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.*;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class GameLeaderTest {
    @Mock
    private ILeaderGameLogic gameLogic;

    @Mock
    private TurnHandler turnHandlerMock;

    @Mock
    private IPersistence iPersistence;

    private Configuration con;

    private Facility facil;

    @Mock
    private BeerGame gameTest;

    private String t;

    private Round r;

    private final Provider<BeerGame> beerGameProvider;
    private final Provider<Round> roundProvider;

    @Mock
    private IConnectorForLeader iConnectorForLeader;

    private GameLeader gameLeader;

    private Round facilityTurnModel;

    private List<Facility> facilities;

    private List<Round> rounds;

    public GameLeaderTest() {
        roundProvider = null;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        facilityTurnModel = new Round();
        rounds = new ArrayList<>();
        facilities = new ArrayList<>();
    }

    @Test
    public void testGameLeader_CreateUsingConstructor_Created() {
        facilities.add(facil);

        BeerGame beerGame = new BeerGame("test", "test", "test", "test");

        Configuration con = mock(Configuration.class);

        con.setFacilities(facilities);

        beerGame.setConfiguration(con);


        new GameLeader(beerGame, iConnectorForLeader);
    }

    @Test
    public void testGameLeaderHasNotGottenAllDataSoNextMethodNotCalled() {
        rounds.add(r);
        facilities.add(facil);

        BeerGame beerGame = new BeerGame("test", "test", "test", "test");

        Configuration con = mock(Configuration.class);

        con.setFacilities(facilities);

        beerGame.setConfiguration(con);

        gameLeader = new GameLeader(beerGame, iConnectorForLeader);

        //gameLeader.setTurnHandler(turnHandlerMock);

        gameLeader.turnModelReceived(facilityTurnModel);

        verify(turnHandlerMock, times(1)).processFacilityTurn(facilityTurnModel);

        System.out.println(gameLeader.getTurnsExpectedPerRound());
        System.out.println(gameLeader.getTurnsReceivedInCurrentRound());

        Assert.assertNotEquals(gameLeader.getTurnsExpectedPerRound(), gameLeader.getTurnsReceivedInCurrentRound());
    }

    @Test
    public void testGameRoundDataIsBiggerThanPreviously() {
        rounds.add(r);

        facilities.add(facil);

        BeerGame beerGame = mock(BeerGame.class);

        Provider<BeerGame> beerGameProvider = mock(Provider.class);

        Provider<Round> roundProvider = mock(Provider.class);


        int roundBefore = beerGameProvider.get().getRounds().size();

        Configuration con = new Configuration();

        con.setFacilities(facilities);

        beerGame.setConfiguration(con);

        gameLeader = new GameLeader(beerGameProvider, roundProvider);

        gameLeader.turnModelReceived(facilityTurnModel);

        int roundAfter = beerGameProvider.get().getRounds().size();

        Assert.assertNotEquals(roundAfter, roundBefore);
    }
}
