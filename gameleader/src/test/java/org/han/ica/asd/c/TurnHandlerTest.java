package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.*;
import static junit.framework.TestCase.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TurnHandlerTest {
    private static final Logger LOGGER = Logger.getLogger(TurnHandlerTest.class.getName());

    private IPersistence persistenceLayer;

    private TurnHandler turnHandler;

    private Round facilityTurnModel;

    private Method m;

    private Object[] parameters;

    @Mock
    private Map<Facility, Integer> turnOrderTestInteger;
    @Mock
    private Map<Facility, Map<Facility, Integer>> turnOrderFacility;

    @Mock
    private Facility facility;

    @BeforeEach
    void onSetUp() {
        MockitoAnnotations.initMocks(this);

        persistenceLayer = mock(IPersistence.class);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IPersistence.class).toInstance(persistenceLayer);
            }
        });

        turnHandler = injector.getInstance(TurnHandler.class);

        try {
            Class[] parameterTypes;
            facilityTurnModel = new Round();
            parameterTypes = new Class[2];
            parameterTypes[0] = Round.class;
            parameterTypes[1] = Round.class;

            m = turnHandler.getClass().getDeclaredMethod("processFacilityTurn", parameterTypes);

            m.setAccessible(true);
            parameters = new Object[2];
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Something went wrong while setting up the TurnHandler test");
            e.printStackTrace();
        }
    }

    @Test
    void testDoValidate_OrderAmountIsZero_ReturnTrue() {
        turnOrderTestInteger.put(facility, 1);
        turnOrderFacility.put(facility, turnOrderTestInteger);

        Round round = new Round();

        round.setTurnOrder(turnOrderFacility);
        parameters[0] = round;
        round.setTurnDeliver(turnOrderFacility);
        parameters[1] = round;

        round.setTurnReceived(turnOrderFacility);
        round.setTurnBackOrder(turnOrderFacility);
        round.setTurnStock(turnOrderTestInteger);

        try {
            //assertTrue((Boolean) m.invoke(turnHandler, parameters));
            Assert.assertThat(m.invoke(turnHandler, parameters), instanceOf(Round.class));

            Round roundTest = (Round) m.invoke(turnHandler, parameters);

            assertEquals(round.getTurnBackOrder(), roundTest.getTurnBackOrder());
            assertEquals(round.getTurnDeliver(), roundTest.getTurnDeliver());
            assertEquals(round.getTurnOrder(), roundTest.getTurnOrder());
            assertEquals(round.getTurnReceived(), roundTest.getTurnReceived());
            assertEquals(round.getTurnStock(), roundTest.getTurnStock());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "You've changed parameters or method names");
            e.printStackTrace();
        }
    }
}
