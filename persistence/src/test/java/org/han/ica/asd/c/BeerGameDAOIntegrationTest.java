package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;


@RunWith(PowerMockRunner.class)
public class BeerGameDAOIntegrationTest {

	private static final Logger LOGGER = Logger.getLogger(BeerGameDAOIntegrationTest.class.getName());
	private BeergameDAO beergameDAO;



	@Before
	public void setUp() {
		setUpDatabase();
	}

	@After
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
		beergameDAO.deleteBeergame("BeergameArnhem");
		Assert.assertEquals(1,beergameDAO.readBeergames().size());
	}


	@Test
	public void getGameLog() {
		beergameDAO = new BeergameDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(beergameDAO, connection);

		beergameDAO.createBeergame("BeergameZutphen");
		Assert.assertEquals("BeergameZutphen",beergameDAO.getGameLog("BeergameZutphen").getGameName());
	}


	private void setUpDatabase(){
		DBConnectionTest.getInstance().createNewDatabase();
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