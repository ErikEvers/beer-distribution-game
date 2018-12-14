package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.FacilityLinkedTo;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



class FacilityTurnDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(FacilityTurnDAOIntegrationTest.class.getName());
	private static final FacilityTurn FACILITY_TURN = new FacilityTurn("BeerGameZutphen13_12_2018",1,1,2,50,50,50,50,50);
	private static final FacilityTurn FACILITY_TURN2 = new FacilityTurn("BeerGameZutphen13_12_2018",1,2,3,50,50,50,50,50);
	private static final FacilityTurn FACILITY_TURN3 = new FacilityTurn("BeerGameZutphen13_12_2018",1,1,2,150,150,150,150,150);

	private FacilityTurnDAO facilityTurnDAO;
	private RoundDAO roundDAO;

	@BeforeEach
	void setUp() {
		DBConnectionTest.getInstance().createNewDatabase();
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createTurnTest() {
		facilityTurnDAO = new FacilityTurnDAO();
		roundDAO = new RoundDAO();

		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(facilityTurnDAO, connection);
		setDatabaseConnection(roundDAO,connection);

		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		facilityTurnDAO.createTurn(FACILITY_TURN);

		FacilityTurn facilityTurnDb = facilityTurnDAO.fetchTurn(new Round("BeerGameZutphen13_12_2018",1),new FacilityLinkedTo("BeerGameZutphen13_12_2018",1,2,false));

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
		facilityTurnDAO = new FacilityTurnDAO();
		roundDAO = new RoundDAO();

		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(facilityTurnDAO, connection);
		setDatabaseConnection(roundDAO,connection);

		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		facilityTurnDAO.createTurn(FACILITY_TURN);
		facilityTurnDAO.createTurn(FACILITY_TURN2);

		List<FacilityTurn> facilityTurnDb = facilityTurnDAO.fetchTurns("BeerGameZutphen13_12_2018",1);
		Assert.assertEquals(2,facilityTurnDb.size());
	}

	@Test
	void updateTurnTest() {
		facilityTurnDAO = new FacilityTurnDAO();
		roundDAO = new RoundDAO();

		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(facilityTurnDAO, connection);
		setDatabaseConnection(roundDAO,connection);

		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		facilityTurnDAO.createTurn(FACILITY_TURN);
		facilityTurnDAO.updateTurn(FACILITY_TURN3);

		FacilityTurn facilityTurnDb = facilityTurnDAO.fetchTurn(new Round("BeerGameZutphen13_12_2018",1),new FacilityLinkedTo("BeerGameZutphen13_12_2018",1,2,false));

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
		facilityTurnDAO = new FacilityTurnDAO();
		roundDAO = new RoundDAO();

		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(facilityTurnDAO, connection);
		setDatabaseConnection(roundDAO,connection);

		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		facilityTurnDAO.createTurn(FACILITY_TURN);
		facilityTurnDAO.deleteTurn(FACILITY_TURN);


		List<FacilityTurn> facilityTurnDb = facilityTurnDAO.fetchTurns("BeerGameZutphen13_12_2018",1);
		Assert.assertEquals(0,facilityTurnDb.size());
	}

	private void setDatabaseConnection(IBeerDisitributionGameDAO gameDAO, DatabaseConnection connection) {
		try {
			Field connField = gameDAO.getClass().getDeclaredField("databaseConnection");
			connField.setAccessible(true);
			connField.set(gameDAO, connection);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}
}