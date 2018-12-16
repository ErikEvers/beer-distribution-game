package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.powermock.api.mockito.PowerMockito.*;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(GameLeader.class)
public class GameLeaderTest {
    //private static final Logger LOGGER = Logger.getLogger(TurnHandlerTest.class.getName());

    @Mock
    IConnectorForLeader connectorForLeader;

    @Mock
    ILeaderGameLogic gameLogic;

    @InjectMocks
    GameLeader gameLeaderMock;

    //@Mock
    //private IConnectorForLeader iConnectorForLeader;


    //private IConnectorForLeader connectorForLeader;

//    private ILeaderGameLogic gameLogic;
//
//    private BeerGame game;
//
//    private TurnHandler turnHandler;
//
//    private Round currentRoundData;
//
//    private FacilityTurn facilityTurnModel;
//
//    private GameLeader gameLeader;
//
//    private Method turnModelReceived;
//
//    private Method processFacilityTurn;
//
//    private Method validateFacilityTurn;
//
//    private int turnsExpected;
//
//    private int turnsReceived;
//
//    private Object[] parameters;

    //
    //Field turnsRec;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGameLeader_CreateUsingConstructor_Created() {
        Configuration con = mock(Configuration.class);
        Facility facil = mock(Facility.class);
        BeerGame gameTest = mock(BeerGame.class);
        String t = mock(String.class);
        Round r = mock(Round.class);
        List<Round> rounds = new ArrayList<>();
        List<Facility> facilities = new ArrayList<>();
        rounds.add(r);
        facilities.add(facil);

        when(gameTest.getGameId()).thenReturn(t);
        when(gameTest.getRounds()).thenReturn(rounds);
        //when(gameTest.getRounds().size()).thenReturn(20);
        //when(rounds.size()).thenReturn(20);
        when(gameTest.getConfiguration()).thenReturn(con);
        when(con.getFacilities()).thenReturn(facilities);

//        gameTest.setConfiguration(con);
//        con.addFacility(facil);

        gameLeaderMock = new GameLeader(gameTest);
    }
}
