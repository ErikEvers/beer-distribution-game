package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.GameAgentDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameAgentDAOIntegrationTest {
    private static final String GAME_ID = "gameId";
    private static final GameAgent GAMEAGENT = new GameAgent(GAME_ID, "name1", 1);
    private static final GameAgent GAMEAGENT2 = new GameAgent(GAME_ID, "name2", 2);
    private static final GameAgent GAMEAGENT2_UPDATE = new GameAgent(GAME_ID, "name2_Updated", 2);

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
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void createGameAgent() {
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).get(0);

        Assert.assertEquals(GAMEAGENT.getGameId(),gameAgentDb.getGameId());
        Assert.assertEquals(GAMEAGENT.getFacilityId(),gameAgentDb.getFacilityId());
        Assert.assertEquals(GAMEAGENT.getGameAgentName(),gameAgentDb.getGameAgentName());
    }

    @Test
    void deleteSpecificGameagent() {
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(2,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());

        gameAgentDAO.deleteSpecificGameagent(GAMEAGENT);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).get(0);

        Assert.assertEquals(GAMEAGENT2.getGameId(),gameAgentDb.getGameId());
        Assert.assertEquals(GAMEAGENT2.getFacilityId(),gameAgentDb.getFacilityId());
        Assert.assertEquals(GAMEAGENT2.getGameAgentName(),gameAgentDb.getGameAgentName());
    }

    @Test
    void deleteAllGameagentsInABeergame() {
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(2,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());

        gameAgentDAO.deleteAllGameagentsInABeergame(GAME_ID);
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());
    }

    @Test
    void updateGameagent() {
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());

        gameAgentDAO.updateGameagent(GAMEAGENT2_UPDATE);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame(GAME_ID).get(0);

        Assert.assertEquals(GAMEAGENT2_UPDATE.getGameId(),gameAgentDb.getGameId());
        Assert.assertEquals(GAMEAGENT2_UPDATE.getFacilityId(),gameAgentDb.getFacilityId());
        Assert.assertEquals(GAMEAGENT2_UPDATE.getGameAgentName(),gameAgentDb.getGameAgentName());
    }
}