package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
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
    private static final FacilityTurn FACILITY_TURN = new FacilityTurn(1,1,1,0, 1,false);
    private static final FacilityTurnDeliver FACILITY_TURN_DELIVER = new FacilityTurnDeliver(1,2,0,4);
    private static final FacilityTurnOrder FACILITY_TURN_ORDER1 = new FacilityTurnOrder(1,2,50);
    private static final FacilityTurnOrder FACILITY_TURN_ORDER2 = new FacilityTurnOrder(1,3,60);

    private AverageCalculationDAO averageCalculationDAO;
    private FacilityDAO facilityDAO;
    private FacilityTypeDAO facilityTypeDAO;
    private RoundDAO roundDAO;

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
        roundDAO = injector.getInstance(RoundDAO.class);
        facilityTypeDAO = injector.getInstance(FacilityTypeDAO.class);

        Injector injectorFaclityDAO = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
                bind(FacilityTypeDAO.class);
            }
        });

        facilityDAO = injectorFaclityDAO.getInstance(FacilityDAO.class);
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
        facilityTypeDAO.createFacilityType(FACTORYTYPE);
        facilityDAO.createFacility(FACTORYONE);
        roundDAO.createFacilityTurn(1,FACILITY_TURN);

        FacilityTurn facilityTurnDb = averageCalculationDAO.readFacilityTurnForFacility(1, 1);

        Assert.assertEquals(FACILITY_TURN.getFacilityId(), facilityTurnDb.getFacilityId());
        Assert.assertEquals(FACILITY_TURN.getRoundId(), facilityTurnDb.getRoundId());
        Assert.assertEquals(FACILITY_TURN.getRemainingBudget(), facilityTurnDb.getRemainingBudget());
        Assert.assertEquals(FACILITY_TURN.getStock(), facilityTurnDb.getStock());
    }

    @Test
    void readFacilityTurnOrderForFacility() {
        facilityTypeDAO.createFacilityType(FACTORYTYPE);
        facilityDAO.createFacility(FACTORYONE);
        roundDAO.createFacilityTurn(1,FACILITY_TURN);
        roundDAO.createFacilityOrder(1, FACILITY_TURN_ORDER1);
        roundDAO.createFacilityOrder(1, FACILITY_TURN_ORDER2);

        List<FacilityTurnOrder> facilityTurnOrderDb = averageCalculationDAO.readFacilityTurnOrderForFacility(1, 1);
        Assert.assertEquals(2, facilityTurnOrderDb.size());
        Assert.assertEquals(FACILITY_TURN_ORDER1.getOrderAmount(), facilityTurnOrderDb.get(0).getOrderAmount());
        Assert.assertEquals(FACILITY_TURN_ORDER2.getOrderAmount(), facilityTurnOrderDb.get(1).getOrderAmount());
    }

    @Test
    void readFacilityTurnDeliverForFacility() {
    }
}