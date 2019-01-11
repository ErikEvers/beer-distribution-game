package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacilityTypeDAOIntegrationTest {
	private static final FacilityType FACILITY_TYPE = new FacilityType("Factory",50,50,50,50,50,50,50);
	private static final FacilityType FACILITY_TYPE2 = new FacilityType("Wholesaler",50,50,50,50,50,50,50);
	private static final FacilityType FACILITY_TYPE2_UPDATE = new FacilityType("Wholesaler",50,50,999,999,999,999,999);

	private FacilityTypeDAO facilityTypeDAO;
	private BeergameDAO beergameDAO;

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
		facilityTypeDAO = injector.getInstance(FacilityTypeDAO.class);
		beergameDAO = injector.getInstance(BeergameDAO.class);
		DaoConfig.setCurrentGameId("BeerGameZutphen");
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}

	@Test
	void createFacilityType() {
		beergameDAO.createBeergame(DaoConfig.getCurrentGameId());
		facilityTypeDAO.createFacilityType(FACILITY_TYPE);
		FacilityType facilityType = facilityTypeDAO.readSpecificFacilityType("Factory");

		Assert.assertEquals(FACILITY_TYPE.getFacilityName(),facilityType.getFacilityName());
		Assert.assertEquals(FACILITY_TYPE.getOpenOrderCosts(),facilityType.getOpenOrderCosts());
		Assert.assertEquals(FACILITY_TYPE.getStartingBudget(),facilityType.getStartingBudget());
		Assert.assertEquals(FACILITY_TYPE.getValueIncomingGoods(),facilityType.getValueIncomingGoods());
		Assert.assertEquals(FACILITY_TYPE.getStockHoldingCosts(),facilityType.getStockHoldingCosts());
		Assert.assertEquals(FACILITY_TYPE.getValueOutgoingGoods(),facilityType.getValueOutgoingGoods());
		Assert.assertEquals(FACILITY_TYPE.getStartingOrder(),facilityType.getStartingOrder());
	}

	@Test
	void updateFacilityType() {
		facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
		Assert.assertEquals(1,facilityTypeDAO.readAllFacilityTypes().size());
		facilityTypeDAO.updateFacilityType(FACILITY_TYPE2_UPDATE);
		Assert.assertEquals(1,facilityTypeDAO.readAllFacilityTypes().size());

		FacilityType facilityType = facilityTypeDAO.readSpecificFacilityType("Wholesaler");

		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getFacilityName(),facilityType.getFacilityName());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getOpenOrderCosts(),facilityType.getOpenOrderCosts());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStartingBudget(),facilityType.getStartingBudget());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getValueIncomingGoods(),facilityType.getValueIncomingGoods());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStockHoldingCosts(),facilityType.getStockHoldingCosts());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getValueOutgoingGoods(),facilityType.getValueOutgoingGoods());
		Assert.assertEquals(FACILITY_TYPE2_UPDATE.getStartingOrder(),facilityType.getStartingOrder());
	}

	@Test
	void deleteAllFacilitytypesForABeergame() {

		Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes().size());
		facilityTypeDAO.createFacilityType(FACILITY_TYPE);
		facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
		Assert.assertEquals(2,facilityTypeDAO.readAllFacilityTypes().size());
		facilityTypeDAO.deleteAllFacilitytypesForABeergame(DaoConfig.getCurrentGameId());
		Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes().size());
	}

	@Test
	void deleteSpecificFacilityType() {

        Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes().size());
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);
        facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
        Assert.assertEquals(2,facilityTypeDAO.readAllFacilityTypes().size());
        facilityTypeDAO.deleteSpecificFacilityType(FACILITY_TYPE.getFacilityName());
        Assert.assertEquals(1,facilityTypeDAO.readAllFacilityTypes().size());
	}

	@Test
	void readAllFacilityTypes() {

        Assert.assertEquals(0,facilityTypeDAO.readAllFacilityTypes().size());
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);
        Assert.assertEquals(1, facilityTypeDAO.readAllFacilityTypes().size());
        facilityTypeDAO.createFacilityType(FACILITY_TYPE2);
        Assert.assertEquals(2, facilityTypeDAO.readAllFacilityTypes().size());
    }

	@Test
	void readSpecificFacilityType() {

        Assert.assertEquals(0, facilityTypeDAO.readAllFacilityTypes().size());
        facilityTypeDAO.createFacilityType(FACILITY_TYPE);
        Assert.assertEquals(1, facilityTypeDAO.readAllFacilityTypes().size());
        FacilityType facilityType = facilityTypeDAO.readSpecificFacilityType(FACILITY_TYPE.getFacilityName());

        Assert.assertEquals(FACILITY_TYPE.getFacilityName(),facilityType.getFacilityName());
        Assert.assertEquals(FACILITY_TYPE.getOpenOrderCosts(),facilityType.getOpenOrderCosts());
        Assert.assertEquals(FACILITY_TYPE.getStartingBudget(),facilityType.getStartingBudget());
        Assert.assertEquals(FACILITY_TYPE.getValueIncomingGoods(),facilityType.getValueIncomingGoods());
        Assert.assertEquals(FACILITY_TYPE.getStockHoldingCosts(),facilityType.getStockHoldingCosts());
        Assert.assertEquals(FACILITY_TYPE.getValueOutgoingGoods(),facilityType.getValueOutgoingGoods());
        Assert.assertEquals(FACILITY_TYPE.getStartingOrder(),facilityType.getStartingOrder());
    }
}