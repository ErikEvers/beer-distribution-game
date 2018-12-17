package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.Configuration;
import org.han.ica.asd.c.model.FacilityType;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacilityTypeDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(FacilityTypeDAOIntegrationTest.class.getName());
    private static final Configuration CONFIGURATION = new Configuration("BeergameTest", 5, 5, 5, 5, 5, 5, 10, true,false);
    private static final FacilityType FACILITY_TYPE = new FacilityType("Factory", "BeergameTest", 10, 10, 5, 5, 100, 5);
    private static final FacilityType FACILITY_TYPE2 = new FacilityType("Wholesale", "BeergameTest", 15, 15, 10, 10, 150, 10);
    private static final FacilityType FACILITY_TYPE2_UPDATE = new FacilityType("Wholesale", "BeergameTest", 20, 20, 20, 20, 20, 20);

    private BeergameDAO beergameDAO;
    private ConfigurationDAO configurationDAO;
    private FacilityTypeDAO facilityTypeDAO;


    @BeforeEach
    public void setUp() {
        DBConnectionTest.getInstance().createNewDatabase();
    }

    @AfterEach
    public void tearDown() {
        DBConnectionTest.getInstance().cleanup();
        System.out.println("MAAK DE DATABASE LEEG");
    }

    @Test
    public void createFacilityTypeTest() {
        beergameDAO = new BeergameDAO();
        configurationDAO = new ConfigurationDAO();
        facilityTypeDAO = new FacilityTypeDAO();

        DatabaseConnection connection = DBConnectionTest.getInstance();
        setDatabaseConnection(beergameDAO, connection);
        beergameDAO.createBeergame("BeergameTest");
        Assert.assertEquals(1,beergameDAO.readBeergames().size());

        setDatabaseConnection(configurationDAO, connection);
        configurationDAO.createConfiguration(CONFIGURATION);
        Assert.assertEquals(1,configurationDAO.readConfigurations().size());

        setDatabaseConnection(facilityTypeDAO, connection);
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);

        FacilityType facilityTypeFromDB = facilityTypeDAO.readSpecificFacilityType(FACILITY_TYPE.getGameId(), FACILITY_TYPE.getFacilityName());

        Assert.assertEquals(FACILITY_TYPE.getFacilityName(), facilityTypeFromDB.getFacilityName());
        Assert.assertEquals(FACILITY_TYPE.getGameId(), facilityTypeFromDB.getGameId());
        Assert.assertEquals(FACILITY_TYPE.getOpenOrderCosts(), facilityTypeFromDB.getOpenOrderCosts());
        Assert.assertEquals(FACILITY_TYPE.getStartingBudget(), facilityTypeFromDB.getStartingBudget());
        Assert.assertEquals(FACILITY_TYPE.getStartingOrder(), facilityTypeFromDB.getStartingOrder());
        Assert.assertEquals(FACILITY_TYPE.getStockHoldingCosts(), facilityTypeFromDB.getStockHoldingCosts());
        Assert.assertEquals(FACILITY_TYPE.getValueIncomingGoods(), facilityTypeFromDB.getValueIncomingGoods());
        Assert.assertEquals(FACILITY_TYPE.getValueOutgoingGoods(), facilityTypeFromDB.getValueOutgoingGoods());
    }



    private void setDatabaseConnection(IBeerDisitributionGameDAO iBeerDisitributionGameDAO, DatabaseConnection connection) {
        try {
            Field connField = iBeerDisitributionGameDAO.getClass().getDeclaredField("databaseConnection");
            connField.setAccessible(true);
            connField.set(iBeerDisitributionGameDAO, connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, e.toString(),e);
        }
    }
}