package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.*;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class GameBusinessRulesInFacilityTurnDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesInFacilityTurnDAOIntegrationTest.class.getName());
	private static GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn;
	private List<Round> rounds;
	private List<GameBusinessRules> gameBusinessRules;

	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnDAO;
	private GameBusinessRulesDAO gameBusinessRulesDAO;

	private static final String GAME_ID = "BeerGameZutphen";
	private static final String GAME_AGENT_NAME = "Henk";
	

	@BeforeEach
	void setUp() {
		DBConnectionTest.getInstance().cleanup();
		GameBusinessRules businessRules = new GameBusinessRules("als voorraad minder dan 10 dan bestellen bij frits","gameAST");
		rounds = new ArrayList<>();
		gameBusinessRules = new ArrayList<>();
		gameBusinessRules.add(businessRules);
		rounds.add(new Round());
		DBConnectionTest.getInstance().createNewDatabase();
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
			}
		});
		gameBusinessRulesDAO = injector.getInstance(GameBusinessRulesDAO.class);
		DaoConfig.setCurrentGameId(GAME_ID);
		Injector injector1 = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
				bind(GameBusinessRulesDAO.class);
			}
		});
		gameBusinessRulesInFacilityTurnDAO = injector1.getInstance(GameBusinessRulesInFacilityTurnDAO.class);
		gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurn(1, 1, "Henk", gameBusinessRules);

	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createTurnTest() {
		gameBusinessRulesDAO.createGameBusinessRule(new GameAgent(GAME_AGENT_NAME ,new Facility(), gameBusinessRules),gameBusinessRules.get(0));
		gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);

		GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurnDb = gameBusinessRulesInFacilityTurnDAO.readTurn(1,1,GAME_AGENT_NAME);

		Assert.assertEquals(gameBusinessRulesInFacilityTurnDb.getFacilityId(),gameBusinessRulesInFacilityTurn.getFacilityId());
		Assert.assertEquals(gameBusinessRulesInFacilityTurnDb.getGameAgentName(),gameBusinessRulesInFacilityTurn.getGameAgentName());
		Assert.assertEquals(gameBusinessRulesInFacilityTurnDb.getGameBusinessRulesList().size(),gameBusinessRulesInFacilityTurn.getGameBusinessRulesList().size());
	}


	@Test
	void deleteTurnTest() {
		gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);

		//Check if record is created in the database
		Assert.assertNotNull(gameBusinessRulesInFacilityTurnDAO.readTurn(1,1,GAME_AGENT_NAME));

		gameBusinessRulesInFacilityTurnDAO.deleteTurn(GAME_ID,1,1);
		Assert.assertNull(gameBusinessRulesInFacilityTurnDAO.readTurn(1,1,GAME_AGENT_NAME));

	}
}