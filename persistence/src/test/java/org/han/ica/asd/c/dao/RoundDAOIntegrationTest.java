package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;


class RoundDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(RoundDAOIntegrationTest.class.getName());
	private RoundDAO roundDAO;
	private FacilityTurn facilityTurn;
	private FacilityTurnOrder facilityTurnOrder;
	private FacilityTurnDeliver facilityTurnDeliver;
	private BeergameDAO beergameDAO;

	@BeforeEach
	void setUp() {
		DBConnectionTest.getInstance().createNewDatabase();
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
			}
		});
		beergameDAO = injector.getInstance(BeergameDAO.class);
		roundDAO = injector.getInstance(RoundDAO.class);
		DaoConfig.setCurrentGameId("BeerGameZutphen13_12_2018");
		facilityTurn = new FacilityTurn(1,1,1,1, 1,false);
		facilityTurnDeliver = new FacilityTurnDeliver(1,2,0,4);
		facilityTurnOrder = new FacilityTurnOrder(1,2,50);
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createRound() {
		beergameDAO.createBeergame(DaoConfig.getCurrentGameId());
		roundDAO.createRound(1);
		roundDAO.createFacilityDeliver(1,facilityTurnDeliver);
		roundDAO.createFacilityOrder(1,facilityTurnOrder);
		roundDAO.createFacilityTurn(1,facilityTurn);

		Round round = roundDAO.getRound(1);

		Assert.assertEquals(1,round.getRoundId());
		Assert.assertEquals(facilityTurn.getRemainingBudget(),round.getFacilityTurns().get(0).getRemainingBudget());
		Assert.assertEquals(facilityTurn.getStock(),round.getFacilityTurns().get(0).getStock());
		Assert.assertEquals(facilityTurn.getBackorders(),round.getFacilityTurns().get(0).getBackorders());
		Assert.assertEquals(facilityTurn.getFacilityId(),round.getFacilityTurns().get(0).getFacilityId());

		Assert.assertEquals(facilityTurnDeliver.getFacilityId(),round.getFacilityTurnDelivers().get(0).getFacilityId());
		Assert.assertEquals(facilityTurnDeliver.getFacilityIdDeliverTo(),round.getFacilityTurnDelivers().get(0).getFacilityIdDeliverTo());
		Assert.assertEquals(facilityTurnDeliver.getOpenOrderAmount(),round.getFacilityTurnDelivers().get(0).getOpenOrderAmount());
		Assert.assertEquals(facilityTurnDeliver.getDeliverAmount(),round.getFacilityTurnDelivers().get(0).getDeliverAmount());


		Assert.assertEquals(facilityTurnOrder.getFacilityId(),round.getFacilityOrders().get(0).getFacilityId());
		Assert.assertEquals(facilityTurnOrder.getFacilityIdOrderTo(),round.getFacilityOrders().get(0).getFacilityIdOrderTo());
		Assert.assertEquals(facilityTurnOrder.getOrderAmount(),round.getFacilityOrders().get(0).getOrderAmount());
	}

	@Test
	void deleteRound() {
		beergameDAO.createBeergame(DaoConfig.getCurrentGameId());
		roundDAO.createRound(1);
		roundDAO.createFacilityDeliver(1,facilityTurnDeliver);
		roundDAO.createFacilityOrder(1,facilityTurnOrder);
		roundDAO.createFacilityTurn(1,facilityTurn);

		Assert.assertEquals(1,roundDAO.getRounds().size());
		roundDAO.deleteRound(1);
		Assert.assertEquals(0,roundDAO.getRounds().size());


	}
}