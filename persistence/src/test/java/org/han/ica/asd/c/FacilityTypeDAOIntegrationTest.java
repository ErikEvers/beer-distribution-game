package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.FacilityType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacilityTypeDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(FacilityTypeDAOIntegrationTest.class.getName());
    private static final FacilityType FACILITY_TYPE = new FacilityType("Factory", "gameId1", 10, 10, 5, 5, 100, 5);
    private static final FacilityType FACILITY_TYPE2 = new FacilityType("Wholesale", "gameId1", 15, 15, 10, 10, 150, 10);
    private static final FacilityType FACILITY_TYPE2_UPDATE = new FacilityType("Wholesale", "gameId1", 20, 20, 20, 20, 20, 20);

    private FacilityTypeDAO facilityTypeDAO;

    @BeforeEach
    public void setUp() {
        DBConnectionTest.getInstance().createNewDatabase();
    }

    @AfterEach
    public void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    public void createFacilityTypeTest(){
        facilityTypeDAO = new FacilityTypeDAO();

        DatabaseConnection connection = DBConnectionTest.getInstance();
        setDatabaseConnection(facilityTypeDAO, connection);

        facilityTypeDAO.createFacilityType(FACILITY_TYPE);

        FacilityType facilityTypeFromDB = facilityTypeDAO.readSpecificFacilityType("gameId1", "Factory");

        Assert.assertEquals(FACILITY_TYPE.getFacilityName(), facilityTypeFromDB.getFacilityName());
        Assert.assertEquals(FACILITY_TYPE.getGameId(), facilityTypeFromDB.getGameId());
        Assert.assertEquals(FACILITY_TYPE.getOpenOrderCosts(), facilityTypeFromDB.getOpenOrderCosts());
        Assert.assertEquals(FACILITY_TYPE.getStartingBudget(), facilityTypeFromDB.getStartingBudget());
        Assert.assertEquals(FACILITY_TYPE.getStartingOrder(), facilityTypeFromDB.getStartingOrder());
        Assert.assertEquals(FACILITY_TYPE.getStockHoldingCosts(), facilityTypeFromDB.getStockHoldingCosts());
        Assert.assertEquals(FACILITY_TYPE.getValueIncomingGoods(), facilityTypeFromDB.getValueIncomingGoods());
        Assert.assertEquals(FACILITY_TYPE.getValueOutgoingGoods(), facilityTypeFromDB.getValueOutgoingGoods());
    }

    @Test
    public void updateFacilityTypeTest() {
        facilityTypeDAO = new FacilityTypeDAO();

        DatabaseConnection connection = DBConnectionTest.getInstance();
        setDatabaseConnection(facilityTypeDAO, connection);

        facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
        facilityTypeDAO.updateFacilityType(FACILITY_TYPE2_UPDATE);

        FacilityType facilityTypeFromDB = facilityTypeDAO.readSpecificFacilityType("gameId1", "Wholesale");

        Assert.assertEquals(FACILITY_TYPE2_UPDATE.getFacilityName(), facilityTypeFromDB.getFacilityName());
        Assert.assertEquals(FACILITY_TYPE2_UPDATE.getGameId(), facilityTypeFromDB.getGameId());
        Assert.assertEquals(FACILITY_TYPE2_UPDATE.getOpenOrderCosts(), facilityTypeFromDB.getOpenOrderCosts());
        Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStartingBudget(), facilityTypeFromDB.getStartingBudget());
        Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStartingOrder(), facilityTypeFromDB.getStartingOrder());
        Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStockHoldingCosts(), facilityTypeFromDB.getStockHoldingCosts());
        Assert.assertEquals(FACILITY_TYPE2_UPDATE.getValueIncomingGoods(), facilityTypeFromDB.getValueIncomingGoods());
        Assert.assertEquals(FACILITY_TYPE2_UPDATE.getValueOutgoingGoods(), facilityTypeFromDB.getValueOutgoingGoods());
    }


    private void setDatabaseConnection(IBeerDisitributionGameDAO gameDAO, DatabaseConnection connection) {
        try {
            Field connField = gameDAO.getClass().getDeclaredField("databaseConnection");
            connField.setAccessible(true);
            connField.set(gameDAO, connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, e.toString(),e);
        }
    }
}