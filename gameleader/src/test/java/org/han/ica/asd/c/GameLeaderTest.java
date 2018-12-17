package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.*;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameLeader.class, IConnectorForLeader.class, IConnectorObserver.class})
public class GameLeaderTest {
    @Mock
    private IPersistence persistence;

    @Mock
    private TurnHandler turnHandler;

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
    private FacilityTurn facilityTurnModel;

    @Mock
    private GameLeader gameLeader;

    private Field turnReceivedPrivate;
    private Field turnExpectedPrivate;

    @Before
    public void init() {
        try {
            turnReceivedPrivate = GameLeader.class.getDeclaredField("turnsReceived");
            turnExpectedPrivate = GameLeader.class.getDeclaredField("turnsExpected");

            turnReceivedPrivate.setAccessible(true);
            turnExpectedPrivate.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGameLeader_CreateUsingConstructor_Created() {
        List<Round> rounds = new ArrayList<>();
        List<Facility> facilities = new ArrayList<>();
        rounds.add(r);
        facilities.add(facil);

        when(gameTest.getGameId()).thenReturn(t);
        when(gameTest.getRounds()).thenReturn(rounds);
        when(gameTest.getConfiguration()).thenReturn(con);
        when(con.getFacilities()).thenReturn(facilities);

        new GameLeader(gameTest, iConnectorForLeader);
    }

    @Test
    public void turnModelReceivedTest() {


        List<Round> rounds = new ArrayList<>();
        List<Facility> facilities = new ArrayList<>();
        rounds.add(r);
        facilities.add(facil);

        when(gameTest.getGameId()).thenReturn(t);
        when(gameTest.getRounds()).thenReturn(rounds);
        when(gameTest.getConfiguration()).thenReturn(con);
        when(con.getFacilities()).thenReturn(facilities);

        //gameLeader = new GameLeader(gameTest, iConnectorForLeader);

        //doNothing().when(persistence).savePlayerTurn(facilityTurnModel);

        //gameLeader1.turnModelReceived(facilityTurnModel);

//        try {
//            whenNew(GameLeader.class).withArguments(gameTest, iConnectorForLeader).thenReturn(gameLeader);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        facilityTurnModel.setOrder(20);
        facilityTurnModel.setStock(10);

        GameLeader gameLeader1 = new GameLeader(gameTest, iConnectorForLeader);

        //gameLeader = new GameLeader(gameTest, iConnectorForLeader);

        gameLeader1.turnModelReceived(facilityTurnModel);
        gameLeader1.turnModelReceived(facilityTurnModel);
        gameLeader1.turnModelReceived(facilityTurnModel);

        try {
            System.out.println(turnExpectedPrivate.get(gameLeader1));
            System.out.println(turnReceivedPrivate.get(gameLeader1));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
