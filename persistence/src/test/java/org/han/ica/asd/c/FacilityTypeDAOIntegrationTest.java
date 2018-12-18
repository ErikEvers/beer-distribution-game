package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.FacilityType;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

class FacilityTypeDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(FacilityTypeDAOIntegrationTest.class.getName());
	private static final FacilityType FACILITY_TYPE = new FacilityType("Factory","BeerGameZutphen",50,50,50,50,50,50);
	private static final FacilityType FACILITY_TYPE2 = new FacilityType("Wholesale","BeerGameZutphen",50,50,50,50,50,50);
	private static final FacilityType FACILITY_TYPE2_UPDATE = new FacilityType("Wholesale","BeerGameZutphen",999,999,999,999,999,999);

	private FacilityTypeDAO facilityTypeDAO;

	@BeforeEach
	void setUp() {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
			}
		});

		DBConnectionTest.getInstance().createNewDatabase();
		facilityTypeDAO = injector.getInstance(FacilityTypeDAO.class);
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createFacilityType() {
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
		facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
		Assert.assertEquals(1,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
		facilityTypeDAO.updateFacilityType(FACILITY_TYPE2_UPDATE);
		Assert.assertEquals(1,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());

		FacilityType facilityTypeDb = facilityTypeDAO.readSpecificFacilityType("BeerGameZutphen", "Wholesale");

		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getGameId(),facilityTypeDb.getGameId());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getFacilityName(),facilityTypeDb.getFacilityName());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getOpenOrderCosts(),facilityTypeDb.getOpenOrderCosts());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStartingBudget(),facilityTypeDb.getStartingBudget());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getValueIncomingGoods(),facilityTypeDb.getValueIncomingGoods());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStockHoldingCosts(),facilityTypeDb.getStockHoldingCosts());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getValueOutgoingGoods(),facilityTypeDb.getValueOutgoingGoods());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStartingOrder(),facilityTypeDb.getStartingOrder());
	}

	@Test
	void deleteAllFacilitytypesForABeergame() {

		Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
		facilityTypeDAO.createFacilityType(FACILITY_TYPE);
		facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
		Assert.assertEquals(2,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
		facilityTypeDAO.deleteAllFacilitytypesForABeergame("BeerGameZutphen");
		Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
	}

	@Test
	void deleteSpecificFacilityType() {

        Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);
        facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
        Assert.assertEquals(2,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
        facilityTypeDAO.deleteSpecificFacilityType(FACILITY_TYPE.getGameId(), FACILITY_TYPE.getFacilityName());
        Assert.assertEquals(1,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
	}

	@Test
	void readAllFacilityTypes() {

        Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);
        Assert.assertEquals(1, facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
        facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
        Assert.assertEquals(2, facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
    }

	@Test
	void readSpecificFacilityType() {

        Assert.assertEquals(0, facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);
        Assert.assertEquals(1, facilityTypeDAO.readAllFacilityTypes("BeerGameZutphen").size());
        FacilityType facilityTypeDb = facilityTypeDAO.readSpecificFacilityType(FACILITY_TYPE.getGameId(), FACILITY_TYPE.getFacilityName());

        Assert.assertEquals(FACILITY_TYPE.getGameId(),facilityTypeDb.getGameId());
        Assert.assertEquals(FACILITY_TYPE.getFacilityName(),facilityTypeDb.getFacilityName());
        Assert.assertEquals(FACILITY_TYPE.getOpenOrderCosts(),facilityTypeDb.getOpenOrderCosts());
        Assert.assertEquals(FACILITY_TYPE.getStartingBudget(),facilityTypeDb.getStartingBudget());
        Assert.assertEquals(FACILITY_TYPE.getValueIncomingGoods(),facilityTypeDb.getValueIncomingGoods());
        Assert.assertEquals(FACILITY_TYPE.getStockHoldingCosts(),facilityTypeDb.getStockHoldingCosts());
        Assert.assertEquals(FACILITY_TYPE.getValueOutgoingGoods(),facilityTypeDb.getValueOutgoingGoods());
        Assert.assertEquals(FACILITY_TYPE.getStartingOrder(),facilityTypeDb.getStartingOrder());
    }
}