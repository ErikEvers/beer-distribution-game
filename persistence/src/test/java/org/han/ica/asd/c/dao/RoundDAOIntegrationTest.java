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
	private Round round;

	@BeforeEach
	void setUp() {
		DBConnectionTest.getInstance().cleanup();
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
		round = new Round();
		round.setRoundId(1);
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createRound() {
		beergameDAO.createBeergame(DaoConfig.getCurrentGameId());
		roundDAO.createRound(round);
		roundDAO.createFacilityDeliver(1,facilityTurnDeliver);
		roundDAO.createFacilityOrder(1,facilityTurnOrder);
		roundDAO.createFacilityTurn(1,facilityTurn);

		Round roundDb = roundDAO.getRound(1);

		Assert.assertEquals(1,roundDb.getRoundId());
		Assert.assertEquals(facilityTurn.getRemainingBudget(),roundDb.getFacilityTurns().get(0).getRemainingBudget());
		Assert.assertEquals(facilityTurn.getStock(),roundDb.getFacilityTurns().get(0).getStock());
		Assert.assertEquals(facilityTurn.getBackorders(),roundDb.getFacilityTurns().get(0).getBackorders());
		Assert.assertEquals(facilityTurn.getFacilityId(),roundDb.getFacilityTurns().get(0).getFacilityId());

		Assert.assertEquals(facilityTurnDeliver.getFacilityId(),roundDb.getFacilityTurnDelivers().get(0).getFacilityId());
		Assert.assertEquals(facilityTurnDeliver.getFacilityIdDeliverTo(),roundDb.getFacilityTurnDelivers().get(0).getFacilityIdDeliverTo());
		Assert.assertEquals(facilityTurnDeliver.getOpenOrderAmount(),roundDb.getFacilityTurnDelivers().get(0).getOpenOrderAmount());
		Assert.assertEquals(facilityTurnDeliver.getDeliverAmount(),roundDb.getFacilityTurnDelivers().get(0).getDeliverAmount());


		Assert.assertEquals(facilityTurnOrder.getFacilityId(),roundDb.getFacilityOrders().get(0).getFacilityId());
		Assert.assertEquals(facilityTurnOrder.getFacilityIdOrderTo(),roundDb.getFacilityOrders().get(0).getFacilityIdOrderTo());
		Assert.assertEquals(facilityTurnOrder.getOrderAmount(),roundDb.getFacilityOrders().get(0).getOrderAmount());
	}

	@Test
	void deleteRound() {
		beergameDAO.createBeergame(DaoConfig.getCurrentGameId());
		roundDAO.createRound(round);
		roundDAO.createFacilityDeliver(1,facilityTurnDeliver);
		roundDAO.createFacilityOrder(1,facilityTurnOrder);
		roundDAO.createFacilityTurn(1,facilityTurn);

		Assert.assertEquals(1,roundDAO.getRounds().size());
		roundDAO.deleteRound(1);
		Assert.assertEquals(0,roundDAO.getRounds().size());


	}
}