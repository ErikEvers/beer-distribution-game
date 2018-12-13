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
import java.util.logging.Level;
import java.util.logging.Logger;



class FacilityTurnDAOTest {
	private static final Logger LOGGER = Logger.getLogger(FacilityTurnDAOTest.class.getName());
	public static final FacilityTurn FACILITY_TURN = new FacilityTurn("BeerGameZutphen13_12_2018",1,1,2,50,50,50,50,50);

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
	}

	@Test
	void fetchTurnTest() {
	}

	@Test
	void updateTurnTest() {
	}

	@Test
	void deleteTurnTest() {
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