package org.han.ica.asd.c.gameleader;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.gameleader.testutil.CommunicationStub;
import org.han.ica.asd.c.gameleader.testutil.GameLogicStub;
import org.han.ica.asd.c.gameleader.testutil.PersistenceStub;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.TestCase.*;

class TurnHandlerTest {

    private static final Logger LOGGER = Logger.getLogger(TurnHandlerTest.class.getName());

    private TurnHandler turnHandler;

    private Round roundModel;

    private Method processFacilityTurn;

    private Object[] parameters;

    @BeforeEach
    void onSetUp() {
				Injector injector = Guice.createInjector(new AbstractModule() {
					@Override
					protected void configure() {
						bind(IPersistence.class).to(PersistenceStub.class);
						bind(IConnectorForLeader.class).to(CommunicationStub.class);
						bind(ILeaderGameLogic.class).to(GameLogicStub.class);
					}
				});
        try {
            Class[] parameterTypes;
						turnHandler = injector.getInstance(TurnHandler.class);
            roundModel = new Round();
            parameterTypes = new Class[2];
            parameterTypes[0] = Round.class;
            parameterTypes[1] = Round.class;


            parameters = new Object[2];

            //processFacilityTurn
            processFacilityTurn = turnHandler.getClass().getDeclaredMethod("processFacilityTurn", parameterTypes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Something went wrong while setting up the TurnHandler test");
            e.printStackTrace();
        }
    }

//    @Test
//    void testDoValidate_OrderAmountIsZero_ReturnTrue() {
//				Facility mainFacility = new Facility();
//				Facility facility = new Facility();
//				Map<Facility, Integer> ordersMap = new HashMap<>();
//				ordersMap.put(facility, 1);
//
//				Map<Facility, Map<Facility, Integer>> orderMap = new HashMap<>();
//				orderMap.put(mainFacility, ordersMap);
//				roundModel.setTurnOrder(orderMap);
//
//				Map<Facility, Integer> stockMap = new HashMap<>();
//				stockMap.put(mainFacility, 10);
//				roundModel.setTurnStock(stockMap);
//
//        parameters[0] = new Round();
//        parameters[1] = roundModel;
//
//        try {
//            assertEquals(roundModel, processFacilityTurn.invoke(turnHandler, parameters));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
