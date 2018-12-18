package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.FacilityTurnDAO;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.FacilityLinkedToDB;
import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.RoundDB;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Logger;



class FacilityTurnDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(FacilityTurnDAOIntegrationTest.class.getName());
	private static final FacilityTurnDB FACILITY_TURN = new FacilityTurnDB("BeerGameZutphen13_12_2018",1,2,1,50,50,50,50,50);
	private static final FacilityTurnDB FACILITY_TURN2 = new FacilityTurnDB("BeerGameZutphen13_12_2018",2,3,1,50,50,50,50,50);
	private static final FacilityTurnDB FACILITY_TURN3 = new FacilityTurnDB("BeerGameZutphen13_12_2018",1,2,1,150,150,150,150,150);
	private FacilityTurnDAO facilityTurnDAO;
	private RoundDAO roundDAO;

	@BeforeEach
	void setUp() {
		DBConnectionTest.getInstance().createNewDatabase();
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
			}
		});
		facilityTurnDAO = injector.getInstance(FacilityTurnDAO.class);
		roundDAO = injector.getInstance(RoundDAO.class);
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createTurnTest() {
		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		facilityTurnDAO.createTurn(FACILITY_TURN);

		FacilityTurnDB facilityTurnDb = facilityTurnDAO.fetchTurn(new RoundDB("BeerGameZutphen13_12_2018",1),new FacilityLinkedToDB("BeerGameZutphen13_12_2018",1,2,false));

		Assert.assertEquals(FACILITY_TURN.getGameId(),facilityTurnDb.getGameId());
		Assert.assertEquals(FACILITY_TURN.getFacilityIdDeliver(),facilityTurnDb.getFacilityIdDeliver());
		Assert.assertEquals(FACILITY_TURN.getFacilityIdOrder(),facilityTurnDb.getFacilityIdOrder());
		Assert.assertEquals(FACILITY_TURN.getOpenOrder(),facilityTurnDb.getOrder());
		Assert.assertEquals(FACILITY_TURN.getOrder(),facilityTurnDb.getOrder());
		Assert.assertEquals(FACILITY_TURN.getOutgoingGoods(),facilityTurnDb.getOutgoingGoods());
		Assert.assertEquals(FACILITY_TURN.getStock(),facilityTurnDb.getStock());
		Assert.assertEquals(FACILITY_TURN.getRemainingBudget(),facilityTurnDb.getRemainingBudget());
		Assert.assertEquals(FACILITY_TURN.getRoundId(),facilityTurnDb.getRoundId());
	}

	@Test
	void fetchTurnsTest() {
		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		facilityTurnDAO.createTurn(FACILITY_TURN);
		facilityTurnDAO.createTurn(FACILITY_TURN2);

		List<FacilityTurnDB> facilityTurnDb = facilityTurnDAO.fetchTurns("BeerGameZutphen13_12_2018",1);
		Assert.assertEquals(2,facilityTurnDb.size());
	}

	@Test
	void updateTurnTest() {
		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		facilityTurnDAO.createTurn(FACILITY_TURN);
		facilityTurnDAO.updateTurn(FACILITY_TURN3);

		FacilityTurnDB facilityTurnDb = facilityTurnDAO.fetchTurn(new RoundDB("BeerGameZutphen13_12_2018",1),new FacilityLinkedToDB("BeerGameZutphen13_12_2018",1,2,false));

		//Validate if the data is not inserted but updated
		Assert.assertEquals(1,facilityTurnDAO.fetchTurns("BeerGameZutphen13_12_2018",1).size());

		//Test if the data is updated
		Assert.assertEquals(FACILITY_TURN3.getGameId(),facilityTurnDb.getGameId());
		Assert.assertEquals(FACILITY_TURN3.getFacilityIdDeliver(),facilityTurnDb.getFacilityIdDeliver());
		Assert.assertEquals(FACILITY_TURN3.getFacilityIdOrder(),facilityTurnDb.getFacilityIdOrder());
		Assert.assertEquals(FACILITY_TURN3.getOpenOrder(),facilityTurnDb.getOrder());
		Assert.assertEquals(FACILITY_TURN3.getOrder(),facilityTurnDb.getOrder());
		Assert.assertEquals(FACILITY_TURN3.getOutgoingGoods(),facilityTurnDb.getOutgoingGoods());
		Assert.assertEquals(FACILITY_TURN3.getStock(),facilityTurnDb.getStock());
		Assert.assertEquals(FACILITY_TURN3.getRemainingBudget(),facilityTurnDb.getRemainingBudget());
		Assert.assertEquals(FACILITY_TURN3.getRoundId(),facilityTurnDb.getRoundId());
	}

	@Test
	void deleteTurnTest() {
		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		facilityTurnDAO.createTurn(FACILITY_TURN);
		facilityTurnDAO.deleteTurn(FACILITY_TURN);


		List<FacilityTurnDB> facilityTurnDb = facilityTurnDAO.fetchTurns("BeerGameZutphen13_12_2018",1);
		Assert.assertEquals(0,facilityTurnDb.size());
	}
}