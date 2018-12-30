package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class GameBusinessRulesDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesDAOIntegrationTest.class.getName());
    private static final List<GameBusinessRules> gameBusinessRules = new ArrayList<>();
    private static final GameBusinessRules GAME_BUSINESS_RULES = new GameBusinessRules("BusinessRule","AST");
    private static final GameBusinessRules GAME_BUSINESS_RULES2 = new GameBusinessRules("BusinessRule2","AST2");
    private static final Facility testFacility = new Facility();
    private static final GameAgent GAME_AGENT = new GameAgent("gameAgentName", testFacility);

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
        DaoConfig.setCurrentGameId("BeerGameTest");
        testFacility.setFacilityId(1);
        GAME_AGENT.setGameBusinessRules(gameBusinessRules);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void createGameBusinessRule() {
        Assert.assertEquals(0,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
        gameBusinessRulesDAO.createGameBusinessRule(GAME_AGENT, GAME_BUSINESS_RULES);
        Assert.assertEquals(1,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
        GameBusinessRules gameBusinessRulesDb = gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).get(0);

        Assert.assertEquals(GAME_BUSINESS_RULES.getGameBusinessRule(),gameBusinessRulesDb.getGameBusinessRule());
        Assert.assertEquals(GAME_BUSINESS_RULES.getGameAST(),gameBusinessRulesDb.getGameAST());
    }

    @Test
    void deleteSpecificGamebusinessrule() {
        Assert.assertEquals(0,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
        gameBusinessRulesDAO.createGameBusinessRule(GAME_AGENT, GAME_BUSINESS_RULES);
        gameBusinessRulesDAO.createGameBusinessRule(GAME_AGENT, GAME_BUSINESS_RULES2);
        Assert.assertEquals(2,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
        gameBusinessRulesDAO.deleteSpecificGamebusinessrule(GAME_AGENT, GAME_BUSINESS_RULES);
        Assert.assertEquals(1,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());

        GameBusinessRules gameBusinessRulesDb = gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).get(0);

        Assert.assertEquals(GAME_BUSINESS_RULES2.getGameBusinessRule(),gameBusinessRulesDb.getGameBusinessRule());
        Assert.assertEquals(GAME_BUSINESS_RULES2.getGameAST(),gameBusinessRulesDb.getGameAST());
    }

    @Test
    void deleteAllGamebusinessrulesForGameagentInAGame() {
        Assert.assertEquals(0,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
        gameBusinessRulesDAO.createGameBusinessRule(GAME_AGENT, GAME_BUSINESS_RULES);
        gameBusinessRulesDAO.createGameBusinessRule(GAME_AGENT, GAME_BUSINESS_RULES2);
        Assert.assertEquals(2,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
        gameBusinessRulesDAO.deleteAllGamebusinessrulesForGameagentInAGame(GAME_AGENT);
        Assert.assertEquals(0,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
    }

    @Test
    void createDuplicateGameBusinessRuleTest() throws SQLException{
        Assert.assertEquals(0,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
        gameBusinessRulesDAO.createGameBusinessRule(GAME_AGENT, GAME_BUSINESS_RULES);
        gameBusinessRulesDAO.createGameBusinessRule(GAME_AGENT, GAME_BUSINESS_RULES);
        Assert.assertEquals(1,gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(GAME_AGENT).size());
    }
}