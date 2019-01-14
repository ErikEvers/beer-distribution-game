package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ConfigurationDAOIntegrationTest {
	private static final FacilityType FACILITYTYPE = new FacilityType("", 1,1,1,1,1,1,1);
	private static final Facility FACILITY = new Facility(FACILITYTYPE, 1);
	private static final Facility FACILITY2 = new Facility(FACILITYTYPE, 2);
	private static final List<Facility> FACILITIES = new ArrayList<>(Arrays.asList(FACILITY,FACILITY2));
	private static final Map<Facility, List<Facility>> FACILITIES_LINKED = new HashMap<>();
	private static final Configuration CONFIGURATION = new Configuration(40,1,1,1,1,1,99,false,false, FACILITIES, FACILITIES_LINKED);
	private static final Configuration CONFIGURATION2 = new Configuration(40,1,1,1,1,1,99,false,false, new ArrayList<>(), FACILITIES_LINKED);
	private static final Configuration CONFIGURATION3 = new Configuration(50,51,51,51,51,51,599,true,true, new ArrayList<>(), FACILITIES_LINKED);


	private ConfigurationDAO configurationDAO;
	private BeergameDAO beergameDAO;

	@BeforeEach
	public void setUp() {
		DBConnectionTest.getInstance().createNewDatabase();
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
			}
		});
		configurationDAO = injector.getInstance(ConfigurationDAO.class);
		beergameDAO = injector.getInstance(BeergameDAO.class);
		DaoConfig.setCurrentGameId("BeerGameZutphen");
		FACILITIES_LINKED.put(FACILITY, new ArrayList<>(Collections.singletonList(FACILITY2)));
	}


	@AfterEach
	public void tearDown() {
		DBConnectionTest.getInstance().cleanup();
		DaoConfig.clearCurrentGameId();
	}


	@Test
	void createConfigurationTest() {
		beergameDAO.createBeergame(DaoConfig.getCurrentGameId());
		configurationDAO.createConfiguration(CONFIGURATION);
		Configuration configuration = configurationDAO.readConfiguration();

		//Test if object values are the equal

		Assert.assertEquals(CONFIGURATION.getAmountOfRounds(),configuration.getAmountOfRounds());
		Assert.assertEquals(CONFIGURATION.getMaximumOrderRetail(),configuration.getMaximumOrderRetail());
		Assert.assertEquals(CONFIGURATION.getMinimalOrderRetail(),configuration.getMinimalOrderRetail());
		Assert.assertEquals(CONFIGURATION.getAmountOfFactories(),configuration.getAmountOfFactories());
		Assert.assertEquals(CONFIGURATION.getAmountOfWarehouses(),configuration.getAmountOfWarehouses());
		Assert.assertEquals(CONFIGURATION.getAmountOfWholesalers(),configuration.getAmountOfWholesalers());
		Assert.assertEquals(CONFIGURATION.getAmountOfRetailers(),configuration.getAmountOfRetailers());
		Assert.assertEquals(CONFIGURATION.isContinuePlayingWhenBankrupt(),configuration.isContinuePlayingWhenBankrupt());
		Assert.assertEquals(CONFIGURATION.isInsightFacilities(),configuration.isInsightFacilities());
	}

	@Test
	void readConfigurationsTest() {
		configurationDAO.createConfiguration(CONFIGURATION);
		DaoConfig.setCurrentGameId("BeerGameZutphen2");
		configurationDAO.createConfiguration(CONFIGURATION2);
		Assert.assertEquals(2,configurationDAO.readConfigurations().size());
	}

	@Test
	void updateConfigurationsTest() {
		configurationDAO.createConfiguration(CONFIGURATION);
		configurationDAO.updateConfigurations(CONFIGURATION3);

		Configuration configurationDb = configurationDAO.readConfiguration();

		//Test if there an insert in not triggered instead of an update
		Assert.assertEquals(1,configurationDAO.readConfigurations().size());

		//Test if the DAO updated the data correctly
		Assert.assertEquals(CONFIGURATION3.getAmountOfRounds(),configurationDb.getAmountOfRounds());
		Assert.assertEquals(CONFIGURATION3.getMaximumOrderRetail(),configurationDb.getMaximumOrderRetail());
		Assert.assertEquals(CONFIGURATION3.getMinimalOrderRetail(),configurationDb.getMinimalOrderRetail());
		Assert.assertEquals(CONFIGURATION3.getAmountOfFactories(),configurationDb.getAmountOfFactories());
		Assert.assertEquals(CONFIGURATION3.getAmountOfWarehouses(),configurationDb.getAmountOfWarehouses());
		Assert.assertEquals(CONFIGURATION3.getAmountOfWholesalers(),configurationDb.getAmountOfWholesalers());
		Assert.assertEquals(CONFIGURATION3.getAmountOfRetailers(),configurationDb.getAmountOfRetailers());
		Assert.assertEquals(CONFIGURATION3.isContinuePlayingWhenBankrupt(),configurationDb.isContinuePlayingWhenBankrupt());
		Assert.assertEquals(CONFIGURATION3.isInsightFacilities(),configurationDb.isInsightFacilities());
	}

	@Test
	void deleteConfigurationsTest() {
		configurationDAO.createConfiguration(CONFIGURATION);
		configurationDAO.deleteConfigurations();
		Assert.assertEquals(0,configurationDAO.readConfigurations().size());
	}
}