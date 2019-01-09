package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


public class BeerGameDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(BeerGameDAOIntegrationTest.class.getName());
	private BeergameDAO beergameDAO;

	private BeerGame beerGame;


	@BeforeEach
	public void setUp() {
		beerGame = new BeerGame(UUID.randomUUID().toString(),"BeergameZutphen",new Date().toString(),null);
		beerGame.getPlayers().add(new Player("1","127.0.01",new Facility(),"Carsten Flokstra",true));
		beerGame.getRounds().add(new Round());
		beerGame.setConfiguration(new Configuration(40,1,1,1,1,1,99,false,false));
		DBConnectionTest.getInstance().cleanup();
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
		DaoConfig.clearCurrentGameId();
	}

	@Test
	public void createBeergameTest() {
		beergameDAO.createBeergame("BeergameZutphen");
		Assert.assertEquals(1,beergameDAO.readBeergames().size());
	}

	@Test
	public void createBeergameWithModelTest(){
		beergameDAO.createBeergame(beerGame);

		Assert.assertEquals(beerGame.getGameId(),beergameDAO.getGameLog().getGameId());
		Assert.assertEquals(beerGame.getGameName(),beergameDAO.getGameLog().getGameName());
		Assert.assertEquals(beerGame.getGameDate(),beergameDAO.getGameLog().getGameDate());
		Assert.assertEquals(beerGame.getGameEndDate(),beergameDAO.getGameLog().getGameEndDate());

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
		List<BeerGame> beergames = beergameDAO.readBeergames();
		beergameDAO.deleteBeergame(beergames.get(0).getGameId());
		Assert.assertEquals(1,beergameDAO.readBeergames().size());
	}


	@Test
	public void getGameLog() {
		beergameDAO.createBeergame("BeergameZutphen");
		Assert.assertEquals("BeergameZutphen",beergameDAO.getGameLog().getGameName());
	}

	@Test
	public void getGameLogOfOngoingGame(){
		beergameDAO.createBeergame("BeergameZutphen");
		beergameDAO.updateEnddate();
		beergameDAO.createBeergame("BeergameArnhem");
		beergameDAO.createBeergame("BeergameNijmegen");
		beergameDAO.createBeergame("BeergameDeventer");
		Assert.assertEquals(3,beergameDAO.getGameLogFromOngoingGame().size());
	}
}