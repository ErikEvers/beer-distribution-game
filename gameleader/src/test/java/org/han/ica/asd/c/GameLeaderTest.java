package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.model.BeerGame;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( TurnHandler.class )
public class GameLeaderTest {
    private static final Logger LOGGER = Logger.getLogger(TurnHandlerTest.class.getName());

    private IConnectorForLeader connectorForLeader;

    private ILeaderGameLogic gameLogic;

    private BeerGame game;

    private TurnHandler turnHandler;

    private Round currentRoundData;

    private FacilityTurn facilityTurnModel;

    private GameLeader gameLeader;

    private Method turnModelReceived;

    private Method processFacilityTurn;

    private Method validateFacilityTurn;

    private int turnsExpected;

    private int turnsReceived;
    
    private Object[] parameters;

    //
    Field turnsRec;

    @BeforeEach
    void onSetUp() {
        try {
//            game = Mockito.mock(BeerGame.class);
            Class[] parameterTypes;
            gameLeader = new GameLeader(game);
            turnHandler = new TurnHandler();
            facilityTurnModel = new FacilityTurn();
            parameterTypes = new Class[1];
            parameterTypes[0] = FacilityTurn.class;

//            //validateFacilityTurn
//            turnModelReceived = gameLeader.getClass().getDeclaredMethod("turnModelReceived", parameterTypes);
//
//            turnModelReceived.setAccessible(true);
//            parameters = new Object[1];
//
//            //turnhandler processFacilityTurn
//            processFacilityTurn = turnHandler.getClass().getDeclaredMethod("processFacilityTurn", parameterTypes);
//            processFacilityTurn.setAccessible(true);
//
//            //turnhandler validateFacilityTurn
//            validateFacilityTurn = turnHandler.getClass().getDeclaredMethod("validateFacilityTurn", parameterTypes);
//            validateFacilityTurn.setAccessible(true);
//
//            turnsRec = gameLeader.getClass().getDeclaredField("turnsReceived");
//            turnsRec.setAccessible(true);



        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Something went wrong while setting up the TurnHandler test");
            e.printStackTrace();
        }
    }

    @Test
    public void gameLeaderTest() {
        //gameLeader = mock(GameLeader.class);

        try {
            gameLeader.turnModelReceived(facilityTurnModel);

            verifyPrivate(turnHandler).invoke(processFacilityTurn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notifyPlayerDisconnectedTest() {

    }

    @Test
    public void notifyPlayerReconnectedTest() {

    }
}
