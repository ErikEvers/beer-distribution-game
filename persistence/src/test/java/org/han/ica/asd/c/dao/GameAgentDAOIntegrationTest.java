package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
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
    private static final FacilityType FACILITY_TYPE = new FacilityType("Factory", 1, 1, 1, 1, 1, 1, 1);
    private static final FacilityType FACILITY_TYPE2 = new FacilityType("Wholesale", 1, 1, 1, 1, 1, 1, 1);
    private static final Facility FACILITY = new Facility(FACILITY_TYPE, 1);
    private static final Facility FACILITY2 = new Facility(FACILITY_TYPE2, 2);
    private static final GameAgent GAMEAGENT = new GameAgent("Agent1", FACILITY, GAME_BUSINESS_RULES);
    private static final GameAgent GAMEAGENT2 = new GameAgent("Agent2", FACILITY2, GAME_BUSINESS_RULES);
    private static final GameAgent GAMEAGENT2_UPDATE = new GameAgent("Agent2_Updated", FACILITY2, GAME_BUSINESS_RULES);

    private GameAgentDAO gameAgentDAO;
    private FacilityDAO facilityDAO;
    private  FacilityTypeDAO facilityTypeDAO;

    @BeforeEach
    void setUp() {
        DBConnectionTest.getInstance().cleanup();
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
                bind(GameBusinessRulesDAO.class);
                bind(FacilityDAO.class);
            }
        });

        DBConnectionTest.getInstance().createNewDatabase();
        gameAgentDAO = injector.getInstance(GameAgentDAO.class);
        DaoConfig.setCurrentGameId("gameId");

        Injector injector1 = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
            }
        });

        facilityTypeDAO = injector1.getInstance(FacilityTypeDAO.class);
        facilityDAO = injector1.getInstance(FacilityDAO.class);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void createGameAgent() {
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);
        facilityDAO.createFacility(FACILITY);
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame().size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame().get(0);

        Assert.assertEquals(GAMEAGENT.getFacility().getFacilityId(),gameAgentDb.getFacility().getFacilityId());
        Assert.assertEquals(GAMEAGENT.getGameAgentName(),gameAgentDb.getGameAgentName());
        Assert.assertEquals(GAMEAGENT.getGameBusinessRules().size(),gameAgentDb.getGameBusinessRules().size());
    }

    @Test
    void deleteSpecificGameagent() {
        facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(2,gameAgentDAO.readGameAgentsForABeerGame().size());

        gameAgentDAO.deleteSpecificGameagent(GAMEAGENT);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame().size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame().get(0);

        Assert.assertEquals(GAMEAGENT2.getFacility().getFacilityId(),gameAgentDb.getFacility().getFacilityId());
        Assert.assertEquals(GAMEAGENT2.getGameAgentName(),gameAgentDb.getGameAgentName());
        Assert.assertEquals(GAMEAGENT2.getGameBusinessRules().size(),gameAgentDb.getGameBusinessRules().size());
    }

    @Test
    void deleteAllGameagentsInABeergame() {
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);
        facilityDAO.createFacility(FACILITY);
        facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
        gameAgentDAO.createGameAgent(GAMEAGENT);
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(2,gameAgentDAO.readGameAgentsForABeerGame().size());

        gameAgentDAO.deleteAllGameagentsInABeergame();
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
    }

    @Test
    void updateGameagent() {
        facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(0,gameAgentDAO.readGameAgentsForABeerGame().size());
        gameAgentDAO.createGameAgent(GAMEAGENT2);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame().size());

        gameAgentDAO.updateGameagent(GAMEAGENT2_UPDATE);
        Assert.assertEquals(1,gameAgentDAO.readGameAgentsForABeerGame().size());

        GameAgent gameAgentDb = gameAgentDAO.readGameAgentsForABeerGame().get(0);

        Assert.assertEquals(GAMEAGENT2_UPDATE.getFacility().getFacilityId(),gameAgentDb.getFacility().getFacilityId());
        Assert.assertEquals(GAMEAGENT2_UPDATE.getGameAgentName(),gameAgentDb.getGameAgentName());
        Assert.assertEquals(GAMEAGENT2_UPDATE.getGameBusinessRules().size(),gameAgentDb.getGameBusinessRules().size());
    }

    @Test
    void gameIdNotSetExceptionTest() throws GameIdNotSetException{
        DaoConfig.setCurrentGameId(null);
        gameAgentDAO.createGameAgent(GAMEAGENT2);
    }
}