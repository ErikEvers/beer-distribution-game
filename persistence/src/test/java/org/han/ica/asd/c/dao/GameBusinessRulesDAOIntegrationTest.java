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
    private static final GameBusinessRulesDB FACILITY_TYPE = new GameBusinessRulesDB(1,"BeerGameZutphen","AgentName","BusinessRule","AST");
    private static final GameBusinessRulesDB FACILITY_TYPE2 = new GameBusinessRulesDB(1,"BeerGameZutphen","AgentName","BusinessRule2","AST2");

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
    }

    @Test
    void deleteSpecificGamebusinessrule() {
    }

    @Test
    void deleteAllGamebusinessrulesForGameagentInAGame() {
    }
}