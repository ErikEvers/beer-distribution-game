package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.FacilityType;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

class FacilityTypeDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(FacilityTypeDAOIntegrationTest.class.getName());
	private static final FacilityType FACILITY_TYPE = new FacilityType("Factory","BeerGameZutphen",50,50,50,50,50,50);

	private FacilityTypeDAO facilityTypeDAO;

	@BeforeEach
	void setUp() {
		DBConnectionTest.getInstance().createNewDatabase();
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createFacilityType() {
		facilityTypeDAO = new FacilityTypeDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(facilityTypeDAO,connection);

		facilityTypeDAO.createFacilityType(FACILITY_TYPE);
		FacilityType facilityTypeDb = facilityTypeDAO.readSpecificFacilityType("BeerGameZutphen","Factory");

		Assert.assertEquals(FACILITY_TYPE.getGameId(),facilityTypeDb.getGameId());
		Assert.assertEquals(FACILITY_TYPE.getFacilityName(),facilityTypeDb.getFacilityName());
		Assert.assertEquals(FACILITY_TYPE.getOpenOrderCosts(),facilityTypeDb.getOpenOrderCosts());
		Assert.assertEquals(FACILITY_TYPE.getStartingBudget(),facilityTypeDb.getStartingBudget());
		Assert.assertEquals(FACILITY_TYPE.getValueIncomingGoods(),facilityTypeDb.getValueIncomingGoods());
		Assert.assertEquals(FACILITY_TYPE.getStockHoldingCosts(),facilityTypeDb.getStockHoldingCosts());
		Assert.assertEquals(FACILITY_TYPE.getValueOutgoingGoods(),facilityTypeDb.getValueOutgoingGoods());
		Assert.assertEquals(FACILITY_TYPE.getStartingOrder(),facilityTypeDb.getStartingOrder());
	}

	@Test
	void updateFacilityType() {
	}

	@Test
	void deleteAllFacilitytypesForABeergame() {
		facilityTypeDAO = new FacilityTypeDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(facilityTypeDAO,connection);


		Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
		facilityTypeDAO.createFacilityType(FACILITY_TYPE);
		Assert.assertEquals(1,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
		facilityTypeDAO.deleteAllFacilitytypesForABeergame("BeerGameZutphen");
		Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
	}

	@Test
	void deleteSpecificFacilityType() {
	}

	@Test
	void deleteAllFacilityTypesForABeergame() {
	}

	@Test
	void readAllFacilityTypes() {
	}

	@Test
	void readSpecificFacilityType() {
	}

	private void setDatabaseConnection(FacilityTypeDAO facilityTypeDAO, DatabaseConnection connection) {
		try {
			Field connField = facilityTypeDAO.getClass().getDeclaredField("databaseConnection");
			connField.setAccessible(true);
			connField.set(facilityTypeDAO, connection);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}
}