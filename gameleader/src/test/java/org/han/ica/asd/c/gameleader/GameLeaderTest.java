package org.han.ica.asd.c.gameleader;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.gameleader.testutil.CommunicationStub;
import org.han.ica.asd.c.gameleader.testutil.GameLogicStub;
import org.han.ica.asd.c.gameleader.testutil.PersistenceStub;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class GameLeaderTest {
    private static final Logger LOGGER = Logger.getLogger(TurnHandlerTest.class.getName());

    private IConnectorForLeader connectorForLeader;

    private ILeaderGameLogic gameLogic;

    private BeerGame game;

    private TurnHandler turnHandler;

    private Round currentRoundData;

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
        turnHandler = mock(TurnHandler.class);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IPersistence.class).to(PersistenceStub.class);
                bind(IConnectorForLeader.class).to(CommunicationStub.class);
                bind(ILeaderGameLogic.class).to(GameLogicStub.class);
                bind(TurnHandler.class).toInstance(turnHandler);
            }
        });

        try {
            Class[] parameterTypes;
            gameLeader = injector.getInstance(GameLeader.class);
            currentRoundData = new Round();
            parameterTypes = new Class[1];
            parameterTypes[0] = Round.class;

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

        try {
            gameLeader.turnModelReceived(currentRoundData);

            Mockito.verify(turnHandler, Mockito.times(1)).processFacilityTurn(any(Round.class), any(Round.class));
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
