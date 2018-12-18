package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.BeerGameDB;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Logger;


public class BeerGameDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(BeerGameDAOIntegrationTest.class.getName());
	private BeergameDAO beergameDAO;


	@BeforeEach
	public void setUp() {
		DBConnectionTest.getInstance().createNewDatabase();
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
			}
		});
		beergameDAO = injector.getInstance(BeergameDAO.class);
	}

	@AfterEach
	public void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	public void createBeergameTest() {
		beergameDAO.createBeergame("BeergameZutphen");
		Assert.assertEquals(1,beergameDAO.readBeergames().size());
	}

	@Test
	public void readBeergames() {
		beergameDAO.createBeergame("BeergameZutphen");
		beergameDAO.createBeergame("BeergameArnhem");
		Assert.assertEquals(2,beergameDAO.readBeergames().size());
	}


	@Test
	public void deleteBeergame() {
		beergameDAO.createBeergame("BeergameZutphen");
		beergameDAO.createBeergame("BeergameArnhem");
		List<BeerGameDB> beergames = beergameDAO.readBeergames();
		beergameDAO.deleteBeergame(beergames.get(0).getGameId());
		Assert.assertEquals(1,beergameDAO.readBeergames().size());
	}


	@Test
	public void getGameLog() {
		beergameDAO.createBeergame("BeergameZutphen");
		List<BeerGameDB> beergames = beergameDAO.readBeergames();
		Assert.assertEquals("BeergameZutphen",beergameDAO.getGameLog(beergames.get(0).getGameId()).getGameName());
	}
}