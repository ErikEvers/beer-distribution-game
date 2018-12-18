package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.FacilityTurn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;;
import java.lang.reflect.Method;
import java.util.logging.*;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TurnHandlerTest {
    private static final Logger LOGGER = Logger.getLogger(TurnHandlerTest.class.getName());

    @Mock
    private IPersistence persistenceLayer;

    @Mock
    private TurnHandler turnHandlerMock;

    private TurnHandler turnHandler;

    private FacilityTurn facilityTurnModel;

    private Method m;

    private Object[] parameters;

    @BeforeEach
    void onSetUp() {
        MockitoAnnotations.initMocks(this);
        try {
            Class[] parameterTypes;
            turnHandler = new TurnHandler();
            facilityTurnModel = new FacilityTurn();
            parameterTypes = new Class[1];
            parameterTypes[0] = FacilityTurn.class;

            m = turnHandler.getClass().getDeclaredMethod("validateFacilityTurn", parameterTypes);

            m.setAccessible(true);
            parameters = new Object[1];
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
            LOGGER.log(Level.SEVERE, "You've changed parameters or method names");
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
            LOGGER.log(Level.SEVERE, "You've changed parameters or method names");
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
            LOGGER.log(Level.SEVERE, "You've changed parameters or method names");
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
            LOGGER.log(Level.SEVERE, "You've changed parameters or method names");
            e.printStackTrace();
        }
    }

    @Test
    void testDoesPersistenceLayerGetCalledWhenTrue() {
        facilityTurnModel.setOrder(10);
        facilityTurnModel.setStock(10);

        parameters[0] = facilityTurnModel;

        turnHandlerMock = new TurnHandler();

        turnHandlerMock.setPersistenceLayer(persistenceLayer);

        turnHandlerMock.processFacilityTurn(facilityTurnModel);

        verify(persistenceLayer, times(1)).savePlayerTurn(facilityTurnModel);
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
        } finally {
            logger.removeHandler(handler);
        }
    }
}
