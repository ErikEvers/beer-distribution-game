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
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;

class TurnHandlerTest {
    private TurnHandler turnHandler;

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
				turnHandler = injector.getInstance(TurnHandler.class);
    }

    @Test
    void testCombineTwoTurns() {
				Facility mainFacility = new Facility(mock(FacilityType.class), 1);
				Facility facility = new Facility(mock(FacilityType.class), 2);

				// Our models
				Round expectedRoundModel = new Round();
				Round round1 = new Round();
				Round round2 = new Round();

				// Add orders for the first facility
				List<FacilityTurnOrder> orderList = new ArrayList<>();
				orderList.add(new FacilityTurnOrder(mainFacility.getFacilityId(), facility.getFacilityId(), 1));
				round1.setFacilityOrders(orderList);
				expectedRoundModel.setFacilityOrders(orderList);

				// Add turns for the first facility
				List<FacilityTurn> facilityList = new ArrayList<>();
				facilityList.add(new FacilityTurn(mainFacility.getFacilityId(), 1, 10, 0, 10, false));
				round1.setFacilityTurns(facilityList);
				expectedRoundModel.setFacilityTurns(facilityList);

				// Add orders for the second facility
				orderList = new ArrayList<>();
				orderList.add(new FacilityTurnOrder(facility.getFacilityId(), mainFacility.getFacilityId(), 1));
				round2.setFacilityOrders(orderList);
				expectedRoundModel.setFacilityOrders(orderList);

				// Add turns for the second facility
				facilityList = new ArrayList<>();
				facilityList.add(new FacilityTurn(facility.getFacilityId(), 1, 10, 0, 10, false));
				round2.setFacilityTurns(facilityList);
				expectedRoundModel.setFacilityTurns(facilityList);

				// Perform processing
				Round actualRoundModel = new Round();
				actualRoundModel = turnHandler.processFacilityTurn(actualRoundModel, round1);
				actualRoundModel = turnHandler.processFacilityTurn(actualRoundModel, round2);

				// compare
				assertEquals(2, actualRoundModel.getFacilityOrders().size());
				assertEquals(2, actualRoundModel.getFacilityTurns().size());
				assertEquals(expectedRoundModel.getFacilityOrders(), actualRoundModel.getFacilityOrders());
				assertEquals(expectedRoundModel.getFacilityTurns(), actualRoundModel.getFacilityTurns());
    }
}
