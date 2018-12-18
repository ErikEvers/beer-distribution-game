package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameLeader.class)
public class GameLeaderTest {
    @Mock
    private ILeaderGameLogic gameLogic;

    @Mock
    private TurnHandler turnHandlerMock;

    @Mock
    private Configuration con;

    @Mock
    private Facility facil;

    @Mock
    private BeerGame gameTest;

    @Mock
    private String t;

    @Mock
    private Round r;

    @Mock
    private IConnectorForLeader iConnectorForLeader;

    @Mock
    private GameLeader gameLeader;

    private FacilityTurn facilityTurnModel;

    private List<Facility> facilities;

    private List<Round> rounds;

    @Before
    public void init() {
        facilityTurnModel = new FacilityTurn();
        rounds = new ArrayList<>();
        facilities = new ArrayList<>();
    }

    @Test
    public void testGameLeader_CreateUsingConstructor_Created() {
        rounds.add(r);
        facilities.add(facil);

        when(gameTest.getGameId()).thenReturn(t);
        when(gameTest.getRounds()).thenReturn(rounds);
        when(gameTest.getConfiguration()).thenReturn(con);
        when(con.getFacilities()).thenReturn(facilities);

        new GameLeader(gameTest, iConnectorForLeader);
    }

    @Test
    public void testGameLeaderHasNotGottenAllDataSoNextMethodNotCalled() {
        rounds.add(r);
        facilities.add(facil);
        facilities.add(facil);

        when(gameTest.getGameId()).thenReturn(t);
        when(gameTest.getRounds()).thenReturn(rounds);
        when(gameTest.getConfiguration()).thenReturn(con);
        when(con.getFacilities()).thenReturn(facilities);

        gameLeader = new GameLeader(gameTest, iConnectorForLeader);

        gameLeader.setTurnHandler(turnHandlerMock);

        gameLeader.turnModelReceived(facilityTurnModel);

        verify(turnHandlerMock, times(1)).processFacilityTurn(facilityTurnModel);

        Assert.assertNotEquals(gameLeader.getTurnsExpected(), gameLeader.getTurnsReceived());
    }

    @Test
    public void testGameFacilitiesAreEqualNextMethodCalledAndReceivedIsZero() {
        rounds.add(r);
        facilities.add(facil);
        facilities.add(facil);

        when(gameTest.getGameId()).thenReturn(t);
        when(gameTest.getRounds()).thenReturn(rounds);
        when(gameTest.getConfiguration()).thenReturn(con);
        when(con.getFacilities()).thenReturn(facilities);

        gameLeader = new GameLeader(gameTest, iConnectorForLeader);

        gameLeader.setGameLogic(gameLogic);

        gameLeader.setTurnHandler(turnHandlerMock);

        gameLeader.turnModelReceived(facilityTurnModel);
        gameLeader.turnModelReceived(facilityTurnModel);

        Assert.assertEquals(0, gameLeader.getTurnsReceived());
    }
}
