package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GameAgentDAOIntegrationTest {
    private static final List<GameBusinessRules> GAME_BUSINESS_RULES = new ArrayList<>();
    private static final GameAgent GAMEAGENT = new GameAgent("Agent1", 1, GAME_BUSINESS_RULES);
    private static final GameAgent GAMEAGENT2 = new GameAgent("Agent2", 2, GAME_BUSINESS_RULES);
    private static final GameAgent GAMEAGENT2_UPDATE = new GameAgent("Agent2_Updated", 2, GAME_BUSINESS_RULES);

    private GameAgentDAO gameAgentDAO;

    @BeforeEach
    void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
            }
        });

        DBConnectionTest.getInstance().createNewDatabase();
        gameAgentDAO = injector.getInstance(GameAgentDAO.class);
        DaoConfig.setCurrentGameId("gameId");
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void createGameAgent() {
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame().size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame().get(0);

        Assert.assertEquals(GAMEAGENT.getFacilityId(),gameAgentDb.getFacilityId());
        Assert.assertEquals(GAMEAGENT.getGameAgentName(),gameAgentDb.getGameAgentName());
        Assert.assertEquals(GAMEAGENT.getGameBusinessRules().size(),gameAgentDb.getGameBusinessRules().size());
    }

    @Test
    void deleteSpecificGameagent() {
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(2,gameAgentDAO.readGameAgentsForABeerGame().size());

        gameAgentDAO.deleteSpecificGameagent(GAMEAGENT);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame().size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame().get(0);

        Assert.assertEquals(GAMEAGENT2.getFacilityId(),gameAgentDb.getFacilityId());
        Assert.assertEquals(GAMEAGENT2.getGameAgentName(),gameAgentDb.getGameAgentName());
        Assert.assertEquals(GAMEAGENT2.getGameBusinessRules().size(),gameAgentDb.getGameBusinessRules().size());
    }

    @Test
    void deleteAllGameagentsInABeergame() {
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(2,gameAgentDAO.readGameAgentsForABeerGame().size());

        gameAgentDAO.deleteAllGameagentsInABeergame();
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
    }

    @Test
    void updateGameagent() {
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame().size());

        gameAgentDAO.updateGameagent(GAMEAGENT2_UPDATE);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame().size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame().get(0);

        Assert.assertEquals(GAMEAGENT2_UPDATE.getFacilityId(),gameAgentDb.getFacilityId());
        Assert.assertEquals(GAMEAGENT2_UPDATE.getGameAgentName(),gameAgentDb.getGameAgentName());
        Assert.assertEquals(GAMEAGENT2_UPDATE.getGameBusinessRules().size(),gameAgentDb.getGameBusinessRules().size());
    }

    @Test
    void gameIdNotSetExceptionTest() throws GameIdNotSetException{
        DaoConfig.setCurrentGameId(null);
        gameAgentDAO.createGameAgent(GAMEAGENT2);
    }
}