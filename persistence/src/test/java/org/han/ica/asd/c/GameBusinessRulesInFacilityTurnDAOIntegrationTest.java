package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.dao_model.GameBusinessRulesInFacilityTurn;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

class GameBusinessRulesInFacilityTurnDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesInFacilityTurnDAOIntegrationTest.class.getName());
	private static final GameBusinessRulesInFacilityTurn GAME_BUSINESS_RULES_IN_FACILITY_TURN = new GameBusinessRulesInFacilityTurn(1,1,2,"BeerGameZutphen13_12_2018","Henk","als voorraad minder dan 10 dan bestellen bij frits","");
	private static final GameBusinessRulesInFacilityTurn GAME_BUSINESS_RULES_IN_FACILITY_TURN2 = new GameBusinessRulesInFacilityTurn(2,2,3,"BeerGameZutphen13_12_2018","Henk","als voorraad minder dan 10 dan bestellen bij frits","");;

	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnDAO;

	@BeforeEach
	void setUp() {
		DBConnectionTest.getInstance().createNewDatabase();
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
			}
		});
		gameBusinessRulesInFacilityTurnDAO = injector.getInstance(GameBusinessRulesInFacilityTurnDAO.class);
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createTurnTest() {
		gameBusinessRulesInFacilityTurnDAO.createTurn(GAME_BUSINESS_RULES_IN_FACILITY_TURN);
		GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurnDb = gameBusinessRulesInFacilityTurnDAO.readTurn("BeerGameZutphen13_12_2018",1,1,2);

		Assert.assertEquals(GAME_BUSINESS_RULES_IN_FACILITY_TURN.getGameId(),gameBusinessRulesInFacilityTurnDb.getGameId());
		Assert.assertEquals(GAME_BUSINESS_RULES_IN_FACILITY_TURN.getFacilityIdDeliver(),gameBusinessRulesInFacilityTurnDb.getFacilityIdDeliver());
		Assert.assertEquals(GAME_BUSINESS_RULES_IN_FACILITY_TURN.getFacilityIdOrder(),gameBusinessRulesInFacilityTurnDb.getFacilityIdOrder());
		Assert.assertEquals(GAME_BUSINESS_RULES_IN_FACILITY_TURN.getGameAgentName(),gameBusinessRulesInFacilityTurnDb.getGameAgentName());
		Assert.assertEquals(GAME_BUSINESS_RULES_IN_FACILITY_TURN.getGameBusinessRule(),gameBusinessRulesInFacilityTurnDb.getGameBusinessRule());
		Assert.assertEquals(GAME_BUSINESS_RULES_IN_FACILITY_TURN.getGameAST(),gameBusinessRulesInFacilityTurnDb.getGameAST());
		Assert.assertEquals(GAME_BUSINESS_RULES_IN_FACILITY_TURN.getRoundId(),gameBusinessRulesInFacilityTurnDb.getRoundId());
	}


	@Test
	void deleteTurnTest() {
		gameBusinessRulesInFacilityTurnDAO.createTurn(GAME_BUSINESS_RULES_IN_FACILITY_TURN);
		gameBusinessRulesInFacilityTurnDAO.createTurn(GAME_BUSINESS_RULES_IN_FACILITY_TURN2);

		//Check if record is created in the database
		Assert.assertEquals("BeerGameZutphen13_12_2018",gameBusinessRulesInFacilityTurnDAO.readTurn("BeerGameZutphen13_12_2018",1,1,2).getGameId());

		gameBusinessRulesInFacilityTurnDAO.deleteTurn("BeerGameZutphen13_12_2018",1,1,2);
		Assert.assertEquals(null,gameBusinessRulesInFacilityTurnDAO.readTurn("BeerGameZutphen13_12_2018",1,1,2));

	}
}