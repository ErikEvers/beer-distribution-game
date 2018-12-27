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

import java.util.logging.Logger;

class FacilityDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(FacilityDAOIntegrationTest.class.getName());
    private static final FacilityType FACILITY_TYPE = new FacilityType("Factory", 1, 1, 1, 1, 1, 1);
    private static final FacilityType FACILITY_TYPE2 = new FacilityType("Wholesale", 1, 1, 1, 1, 1, 1);
    private static final FacilityType FACILITY_TYPE2_UPDATE = new FacilityType("Distributor", 1, 1, 1, 1, 1, 1);
    private static final Facility FACILITY = new Facility(FACILITY_TYPE, 1);
    private static final Facility FACILITY2 = new Facility(FACILITY_TYPE2, 2);
    private static final Facility FACILITY2_UPDATE = new Facility(FACILITY_TYPE2_UPDATE, 2);

    private FacilityDAO facilityDAO;

    @BeforeEach
    void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
                bind(FacilityTypeDAO.class);
            }
        });

        DBConnectionTest.getInstance().createNewDatabase();
        DaoConfig.setCurrentGameId("BeerGameTest");

        facilityDAO = injector.getInstance(FacilityDAO.class);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void createFacilityTest() {

        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame().size());
        facilityDAO.createFacility(FACILITY);
        Assert.assertEquals(1, facilityDAO.readAllFacilitiesInGame().size());
        Facility facilityDb = facilityDAO.readSpecificFacility(1);

        Assert.assertEquals(FACILITY.getFacilityId(), facilityDb.getFacilityId());
        Assert.assertEquals(FACILITY.getFacilityType(), facilityDb.getFacilityType());
    }

    @Test
    void updateFacilityTest() {

        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame().size());
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(1, facilityDAO.readAllFacilitiesInGame().size());
        facilityDAO.updateFacility(FACILITY2_UPDATE);
        Assert.assertEquals(1, facilityDAO.readAllFacilitiesInGame().size());

        Facility facilityDb = facilityDAO.readSpecificFacility(2);

        Assert.assertEquals(FACILITY2_UPDATE.getFacilityId(), facilityDb.getFacilityId());
        Assert.assertEquals(FACILITY2_UPDATE.getFacilityType(), facilityDb.getFacilityType());
    }

    @Test
    void deleteSpecificFacilityTest() {

        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame().size());
        facilityDAO.createFacility(FACILITY);
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(2, facilityDAO.readAllFacilitiesInGame().size());
        facilityDAO.deleteSpecificFacility(FACILITY);
        Assert.assertEquals(1, facilityDAO.readAllFacilitiesInGame().size());

        Facility facilityDb = facilityDAO.readSpecificFacility(1);

        Assert.assertEquals(FACILITY2.getFacilityId(), facilityDb.getFacilityId());
        Assert.assertEquals(FACILITY2.getFacilityType(), facilityDb.getFacilityType());
    }

    @Test
    void deleteAllFacilitiesInGameTest() {

        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame().size());
        facilityDAO.createFacility(FACILITY);
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(2, facilityDAO.readAllFacilitiesInGame().size());
        facilityDAO.deleteAllFacilitiesInGame();
        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame().size());
    }
}