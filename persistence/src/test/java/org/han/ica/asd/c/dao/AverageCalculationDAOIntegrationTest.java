package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Logger;

class AverageCalculationDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(AverageCalculationDAOIntegrationTest.class.getName());
    private static final FacilityType FACTORYTYPE = new FacilityType("Factory",1,1,1,1,1,1,1);
    private static final Facility FACTORYONE = new Facility(FACTORYTYPE, 1);
    private static final Facility FACTORYTWO = new Facility(FACTORYTYPE, 2);

    private AverageCalculationDAO averageCalculationDAO;
    private FacilityDAO facilityDAO;
    private FacilityTypeDAO facilityTypeDAO;

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
        averageCalculationDAO = injector.getInstance(AverageCalculationDAO.class);

        Injector injectorFaclityDAO = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
                bind(FacilityTypeDAO.class);
            }
        });

        facilityDAO = injectorFaclityDAO.getInstance(FacilityDAO.class);

        Injector injectorFacilityTypeDAO = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
            }
        });
        facilityTypeDAO = injectorFacilityTypeDAO.getInstance(FacilityTypeDAO.class);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void readAllFacilitiesWithFacilityType() {
        facilityTypeDAO.createFacilityType(FACTORYTYPE);
        facilityDAO.createFacility(FACTORYONE);
        facilityDAO.createFacility(FACTORYTWO);
        Assert.assertEquals(2, facilityDAO.readAllFacilitiesInGame().size());

        List<Integer> facilityIdsDb = averageCalculationDAO.readAllFacilitiesWithFacilityType("Factory");
        Assert.assertEquals(2, facilityIdsDb.size());
        Assert.assertEquals(1, facilityIdsDb.get(0).intValue());
        Assert.assertEquals(2, facilityIdsDb.get(1).intValue());
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