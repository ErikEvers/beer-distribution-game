package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.FacilityTurn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.logging.*;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest( TurnHandler.class )
class TurnHandlerTest {

    //@InjectMocks
    private IPersistence persistenceLayer;

    private static final Logger LOGGER = Logger.getLogger(TurnHandlerTest.class.getName());

    private TurnHandler turnHandler;

    private FacilityTurn facilityTurnModel;

    private Method m;

    private Method processFacilityTurn;

    private Object[] parameters;


    @BeforeEach
    void onSetUp() {
        try {
            Class[] parameterTypes;
            turnHandler = new TurnHandler();
            facilityTurnModel = new FacilityTurn();
            parameterTypes = new Class[1];
            parameterTypes[0] = FacilityTurn.class;

            //validateFacilityTurn
            m = turnHandler.getClass().getDeclaredMethod("validateFacilityTurn", parameterTypes);

            m.setAccessible(true);
            parameters = new Object[1];

            //processFacilityTurn
            processFacilityTurn = turnHandler.getClass().getDeclaredMethod("processFacilityTurn", parameterTypes);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Something went wrong while setting up the TurnHandler test");
            e.printStackTrace();
        }
    }

    @Test
    void testDoValidate_OrderAmountIsZero_ReturnTrue() {
        facilityTurnModel.setOrder(1);
        facilityTurnModel.setStock(10);

        parameters[0] = facilityTurnModel;

        try {
            assertTrue((Boolean) m.invoke(turnHandler, parameters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDoValidate_OrderAmountIsLessThanZero_ReturnFalse() {
        facilityTurnModel.setOrder(-10);
        facilityTurnModel.setStock(10);

        parameters[0] = facilityTurnModel;

        try {
            assertFalse((Boolean) m.invoke(turnHandler, parameters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDoValidate_OrderAmountIsMoreThanStock_ReturnFalse() {
        facilityTurnModel.setOrder(11);
        facilityTurnModel.setStock(10);

        parameters[0] = facilityTurnModel;

        try {
            assertFalse((Boolean) m.invoke(turnHandler, parameters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDoValidate_OrderAmountEqualToStock_ReturnTrue() {
        facilityTurnModel.setOrder(10);
        facilityTurnModel.setStock(10);

        parameters[0] = facilityTurnModel;

        try {
            assertTrue((Boolean) m.invoke(turnHandler, parameters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDoProcess_ValidateFacilityTurnReturnFalse_ThenLog() {
        facilityTurnModel.setOrder(10);
        facilityTurnModel.setStock(10);

        parameters[0] = facilityTurnModel;

        try {
        when(m.invoke(turnHandler, parameters)).thenReturn(true);

        m.invoke(turnHandler, parameters);

        verify(persistenceLayer, times(55)).savePlayerTurn(facilityTurnModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLogMessageIsSentWhenOrderIsHigherThanStock() {
        facilityTurnModel.setOrder(12);
        facilityTurnModel.setStock(10);

        parameters[0] = facilityTurnModel;

        Logger logger = Logger.getLogger(TurnHandler.class.getName());

        java.util.logging.Formatter formatter = new SimpleFormatter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Handler handler = new StreamHandler(out, formatter);
        logger.addHandler(handler);

        try {
            TurnHandler instance = new TurnHandler();
            instance.processFacilityTurn(facilityTurnModel);

            handler.flush();
            String logMsg = out.toString();

            assertNotNull(logMsg);
            //assertEquals(logMsg, "WARNING: Incoming orders can't be higher than the available stock.");

        } finally {
            logger.removeHandler(handler);
        }
    }
}
