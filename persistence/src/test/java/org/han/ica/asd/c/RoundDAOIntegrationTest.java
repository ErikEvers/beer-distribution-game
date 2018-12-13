package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.Round;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;


class RoundDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(RoundDAOIntegrationTest.class.getName());

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
	void createRound() {
		roundDAO = new RoundDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(roundDAO, connection);


		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		Round roundDb = roundDAO.getRound("BeerGameZutphen13_12_2018",1);

		Assert.assertEquals(1,roundDb.getRoundId());
		Assert.assertEquals("BeerGameZutphen13_12_2018",roundDb.getGameId());
	}

	@Test
	void deleteRound() {
		roundDAO = new RoundDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(roundDAO, connection);

		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		Round roundDb = roundDAO.getRound("BeerGameZutphen13_12_2018",1);

		//Verify that the object is inserted
		Assert.assertEquals(1,roundDb.getRoundId());

		roundDAO.deleteRound("BeerGameZutphen13_12_2018",1);


		//Verify that the object is deleted
		Assert.assertEquals(null,roundDAO.getRound("BeerGameZutphen13_12_2018",1));


	}

	private void setDatabaseConnection(IBeerDisitributionGameDAO gameDAO, DatabaseConnection connection) {
		try {
			Field connField = gameDAO.getClass().getDeclaredField("databaseConnection");
			connField.setAccessible(true);
			connField.set(gameDAO, connection);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}