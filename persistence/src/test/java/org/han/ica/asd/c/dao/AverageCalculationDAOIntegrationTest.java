package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

class AverageCalculationDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(AverageCalculationDAOIntegrationTest.class.getName());

    @BeforeEach
    void setUp() {
        DBConnectionTest.getInstance().cleanup();
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
            }
        });
        DBConnectionTest.getInstance().createNewDatabase();
        DaoConfig.setCurrentGameId("BeerGameTest");
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void readAllFacilitiesWithFacilityType() {
    }

    @Test
    void readFacilityTurnForFacility() {
    }

    @Test
    void readFacilityTurnOrderForFacility() {
    }

    @Test
    void readFacilityTurnDeliverForFacility() {
    }
}