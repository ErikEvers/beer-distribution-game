package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.dao_model.Facility;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

class FacilityDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(FacilityDAOIntegrationTest.class.getName());
    private static final Facility FACILITY = new Facility("BeerGameZutphen", 1, "Factory", "playerId", "gameAgentName", false);
    private static final Facility FACILITY2 = new Facility("BeerGameZutphen", 2, "Wholesale", "playerId", "gameAgentName", false);
    private static final Facility FACILITY2_UPDATE = new Facility("BeerGameZutphen", 2, "Wholesale", "playerId", "gameAgentName", true);

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

        facilityDAO = injector.getInstance(FacilityDAO.class);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void createFacilityTest() {

        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
        facilityDAO.createFacility(FACILITY);
        Assert.assertEquals(1, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
        Facility facilityDb = facilityDAO.readSpecificFacility(1, "BeerGameZutphen");

        Assert.assertEquals(FACILITY.getGameId(), facilityDb.getGameId());
        Assert.assertEquals(FACILITY.getFacilityId(), facilityDb.getFacilityId());
        Assert.assertEquals(FACILITY.getFacilityType(), facilityDb.getFacilityType());
        Assert.assertEquals(FACILITY.getPlayerId(), facilityDb.getPlayerId());
        Assert.assertEquals(FACILITY.getGameAgentName(), facilityDb.getGameAgentName());
        Assert.assertEquals(FACILITY.isBankrupt(), facilityDb.isBankrupt());
    }

    @Test
    void updateFacilityTest() {

        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(1, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
        facilityDAO.updateFacility(FACILITY2_UPDATE);
        Assert.assertEquals(1, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());

        Facility facilityDb = facilityDAO.readSpecificFacility(2, "BeerGameZutphen");

        Assert.assertEquals(FACILITY2_UPDATE.getGameId(), facilityDb.getGameId());
        Assert.assertEquals(FACILITY2_UPDATE.getFacilityId(), facilityDb.getFacilityId());
        Assert.assertEquals(FACILITY2_UPDATE.getFacilityType(), facilityDb.getFacilityType());
        Assert.assertEquals(FACILITY2_UPDATE.getPlayerId(), facilityDb.getPlayerId());
        Assert.assertEquals(FACILITY2_UPDATE.getGameAgentName(), facilityDb.getGameAgentName());
        Assert.assertEquals(FACILITY2_UPDATE.isBankrupt(), facilityDb.isBankrupt());
    }

    @Test
    void deleteSpecificFacilityTest() {

        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
        facilityDAO.createFacility(FACILITY);
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(2, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
        facilityDAO.deleteSpecificFacility(2, "BeerGameZutphen");
        Assert.assertEquals(1, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());

        Facility facilityDb = facilityDAO.readSpecificFacility(1, "BeerGameZutphen");

        Assert.assertEquals(FACILITY.getGameId(), facilityDb.getGameId());
        Assert.assertEquals(FACILITY.getFacilityId(), facilityDb.getFacilityId());
        Assert.assertEquals(FACILITY.getFacilityType(), facilityDb.getFacilityType());
        Assert.assertEquals(FACILITY.getPlayerId(), facilityDb.getPlayerId());
        Assert.assertEquals(FACILITY.getGameAgentName(), facilityDb.getGameAgentName());
        Assert.assertEquals(FACILITY.isBankrupt(), facilityDb.isBankrupt());
    }

    @Test
    void deleteAllFacilitiesInGameTest() {

        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
        facilityDAO.createFacility(FACILITY);
        facilityDAO.createFacility(FACILITY2);
        Assert.assertEquals(2, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
        facilityDAO.deleteAllFacilitiesInGame("BeerGameZutphen");
        Assert.assertEquals(0, facilityDAO.readAllFacilitiesInGame("BeerGameZutphen").size());
    }
}