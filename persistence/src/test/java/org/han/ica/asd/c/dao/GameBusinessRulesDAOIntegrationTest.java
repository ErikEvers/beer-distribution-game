package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesDB;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

class GameBusinessRulesDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesDAOIntegrationTest.class.getName());
    private static final GameBusinessRulesDB GAME_BUSINESS_RULES_DB = new GameBusinessRulesDB(1,"BeerGameZutphen","AgentName","BusinessRule","AST");
    private static final GameBusinessRulesDB GAME_BUSINESS_RULES_DB2 = new GameBusinessRulesDB(1,"BeerGameZutphen","AgentName","BusinessRule2","AST2");

    private GameBusinessRulesDAO gameBusinessRulesDAO;

    @BeforeEach
    void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
            }
        });

        DBConnectionTest.getInstance().createNewDatabase();
        gameBusinessRulesDAO = injector.getInstance(GameBusinessRulesDAO.class);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void createGameBusinessRule() {
        Assert.assertEquals(0,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame("BeerGameZutphen", "AgentName").size());
        gameBusinessRulesDAO.createGameBusinessRule(GAME_BUSINESS_RULES_DB);
        Assert.assertEquals(1,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame("BeerGameZutphen", "AgentName").size());
        GameBusinessRulesDB gameBusinessRulesDB = gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame("BeerGameZutphen","AgentName").get(0);

        Assert.assertEquals(GAME_BUSINESS_RULES_DB.getGameId(),gameBusinessRulesDB.getGameId());
        Assert.assertEquals(GAME_BUSINESS_RULES_DB.getFacilityId(),gameBusinessRulesDB.getFacilityId());
        Assert.assertEquals(GAME_BUSINESS_RULES_DB.getGameAgentName(),gameBusinessRulesDB.getGameAgentName());
        Assert.assertEquals(GAME_BUSINESS_RULES_DB.getGameBusinessRule(),gameBusinessRulesDB.getGameBusinessRule());
        Assert.assertEquals(GAME_BUSINESS_RULES_DB.getGameAST(),gameBusinessRulesDB.getGameAST());
    }

    @Test
    void deleteSpecificGamebusinessrule() {
        Assert.assertEquals(0,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame("BeerGameZutphen", "AgentName").size());
        gameBusinessRulesDAO.createGameBusinessRule(GAME_BUSINESS_RULES_DB);
        gameBusinessRulesDAO.createGameBusinessRule(GAME_BUSINESS_RULES_DB2);
        Assert.assertEquals(2,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame("BeerGameZutphen", "AgentName").size());
        gameBusinessRulesDAO.deleteSpecificGamebusinessrule(GAME_BUSINESS_RULES_DB);

        GameBusinessRulesDB gameBusinessRulesDB = gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame("BeerGameZutphen","AgentName").get(0);

        Assert.assertEquals(GAME_BUSINESS_RULES_DB2.getGameId(),gameBusinessRulesDB.getGameId());
        Assert.assertEquals(GAME_BUSINESS_RULES_DB2.getFacilityId(),gameBusinessRulesDB.getFacilityId());
        Assert.assertEquals(GAME_BUSINESS_RULES_DB2.getGameAgentName(),gameBusinessRulesDB.getGameAgentName());
        Assert.assertEquals(GAME_BUSINESS_RULES_DB2.getGameBusinessRule(),gameBusinessRulesDB.getGameBusinessRule());
        Assert.assertEquals(GAME_BUSINESS_RULES_DB2.getGameAST(),gameBusinessRulesDB.getGameAST());
    }

    @Test
    void deleteAllGamebusinessrulesForGameagentInAGame() {
    }
}