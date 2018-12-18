package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class GameLeaderTest {
    @Mock
    private ILeaderGameLogic gameLogic;

    @Mock
    private TurnHandler turnHandlerMock;

    private Configuration con;

    private Facility facil;

    @Mock
    private BeerGame gameTest;

    private String t;

    private Round r;

    @Mock
    private IConnectorForLeader iConnectorForLeader;

    private GameLeader gameLeader;

    private FacilityTurn facilityTurnModel;

    private List<Facility> facilities;

    private List<Round> rounds;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        facilityTurnModel = new FacilityTurn();
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

        gameLeader.setTurnHandler(turnHandlerMock);

        gameLeader.turnModelReceived(facilityTurnModel);

        verify(turnHandlerMock, times(1)).processFacilityTurn(facilityTurnModel);

        System.out.println(gameLeader.getTurnsExpected());
        System.out.println(gameLeader.getTurnsReceived());

        Assert.assertNotEquals(gameLeader.getTurnsExpected(), gameLeader.getTurnsReceived());
    }

    @Test
    public void testGameRoundDataIsBiggerThanPreviously() {
        rounds.add(r);

        facilities.add(facil);

        BeerGame beerGame = new BeerGame("test", "test", "test", "test");

        int roundBefore = beerGame.getRounds().size();

        Configuration con = new Configuration();

        con.setFacilities(facilities);

        beerGame.setConfiguration(con);

        gameLeader = new GameLeader(beerGame, iConnectorForLeader);

        gameLeader.setGameLogic(gameLogic);

        gameLeader.setTurnHandler(turnHandlerMock);

        gameLeader.turnModelReceived(facilityTurnModel);

        int roundAfter = beerGame.getRounds().size();

        Assert.assertNotEquals(roundAfter, roundBefore);
    }
}
