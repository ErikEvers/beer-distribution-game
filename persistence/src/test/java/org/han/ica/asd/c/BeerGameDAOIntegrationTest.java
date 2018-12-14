package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.BeerGame;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BeerGameDAOIntegrationTest {

	private static final Logger LOGGER = Logger.getLogger(BeerGameDAOIntegrationTest.class.getName());
	private BeergameDAO beergameDAO;


	@BeforeEach
	public void setUp() {
		DBConnectionTest.getInstance().createNewDatabase();
	}

	@AfterEach
	public void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	public void createBeergameTest() {
		beergameDAO = new BeergameDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(beergameDAO, connection);

		beergameDAO.createBeergame("BeergameZutphen");
		Assert.assertEquals(1,beergameDAO.readBeergames().size());
	}

	@Test
	public void readBeergames() {
		beergameDAO = new BeergameDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(beergameDAO, connection);

		beergameDAO.createBeergame("BeergameZutphen");
		beergameDAO.createBeergame("BeergameArnhem");
		Assert.assertEquals(2,beergameDAO.readBeergames().size());
	}


	@Test
	public void deleteBeergame() {
		beergameDAO = new BeergameDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(beergameDAO, connection);

		beergameDAO.createBeergame("BeergameZutphen");
		beergameDAO.createBeergame("BeergameArnhem");
		List<BeerGame> beergames = beergameDAO.readBeergames();
		beergameDAO.deleteBeergame(beergames.get(0).getGameId());
		Assert.assertEquals(1,beergameDAO.readBeergames().size());
	}


	@Test
	public void getGameLog() {
		beergameDAO = new BeergameDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(beergameDAO, connection);

		beergameDAO.createBeergame("BeergameZutphen");
		List<BeerGame> beergames = beergameDAO.readBeergames();
		Assert.assertEquals("BeergameZutphen",beergameDAO.getGameLog(beergames.get(0).getGameId()).getGameName());
	}


	private void setDatabaseConnection(BeergameDAO beergameDAO, DatabaseConnection connection) {
		try {
			Field connField = beergameDAO.getClass().getDeclaredField("databaseConnection");
			connField.setAccessible(true);
			connField.set(beergameDAO, connection);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}

}