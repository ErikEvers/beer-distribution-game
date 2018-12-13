package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;


@RunWith(PowerMockRunner.class)
public class BeerGameDAOIntegrationTest {


	private BeergameDAO beergameDAO = new BeergameDAO();


	@Before
	public void setUp() {
		setUpDatabase();
	}

	@After
	public void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	public void createBeergame() {
		BeergameDAO beergameDAO = new BeergameDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		try {
			Field connField = beergameDAO.getClass().getDeclaredField("databaseConnection");
			connField.setAccessible(true);
			connField.set(beergameDAO, connection);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		beergameDAO.createBeergame("BeergameZutphen");
	}

	@Test
	public void readBeergames() {
	}

	@Test
	public void deleteBeergame() {
	}

	@Test
	public void getGameLog() {
	}

	public void setUpDatabase(){
		DBConnectionTest.getInstance().createNewDatabase();
	}

}