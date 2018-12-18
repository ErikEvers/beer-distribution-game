package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.Round;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;


class RoundDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(RoundDAOIntegrationTest.class.getName());
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
		roundDAO = injector.getInstance(RoundDAO.class);
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createRound() {
		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		Round roundDb = roundDAO.getRound("BeerGameZutphen13_12_2018",1);

		Assert.assertEquals(1,roundDb.getRoundId());
		Assert.assertEquals("BeerGameZutphen13_12_2018",roundDb.getGameId());
	}

	@Test
	void deleteRound() {
		roundDAO.createRound("BeerGameZutphen13_12_2018",1);
		Round roundDb = roundDAO.getRound("BeerGameZutphen13_12_2018",1);

		//Verify that the object is inserted
		Assert.assertEquals(1,roundDb.getRoundId());

		roundDAO.deleteRound("BeerGameZutphen13_12_2018",1);


		//Verify that the object is deleted
		Assert.assertEquals(null,roundDAO.getRound("BeerGameZutphen13_12_2018",1));


	}
}