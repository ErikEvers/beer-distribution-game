package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.dao_model.Configuration;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;


class ConfigurationDAOIntegrationTest {
	private static final Logger LOGGER = Logger.getLogger(BeerGameDAOIntegrationTest.class.getName());
	private static final Configuration CONFIGURATION = new Configuration("BeerGameZutphen13_12_2018",40,1,1,1,1,1,99,false,false);
	private static final Configuration CONFIGURATION2 = new Configuration("BeerGameArnhem13_12_2018",40,1,1,1,1,1,99,false,false);
	private static final Configuration CONFIGURATION3 = new Configuration("BeerGameZutphen13_12_2018",50,51,51,51,51,51,599,true,true);

	private ConfigurationDAO configurationDAO;

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
	}


	@AfterEach
	public void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}


	@Test
	void createConfigurationTest() {
		configurationDAO.createConfiguration(CONFIGURATION);
		Configuration configurationDb = configurationDAO.readConfiguration("BeerGameZutphen13_12_2018");

		//Test if object values are the equal
		Assert.assertEquals(CONFIGURATION.getGameId(),configurationDb.getGameId());
		Assert.assertEquals(CONFIGURATION.getAmountOfRounds(),configurationDb.getAmountOfRounds());
		Assert.assertEquals(CONFIGURATION.getMaximumOrderRetail(),configurationDb.getMaximumOrderRetail());
		Assert.assertEquals(CONFIGURATION.getMinimalOrderRetail(),configurationDb.getMinimalOrderRetail());
		Assert.assertEquals(CONFIGURATION.getAmountOfFactories(),configurationDb.getAmountOfFactories());
		Assert.assertEquals(CONFIGURATION.getAmountOfDistributors(),configurationDb.getAmountOfDistributors());
		Assert.assertEquals(CONFIGURATION.getAmountOfWholesales(),configurationDb.getAmountOfWholesales());
		Assert.assertEquals(CONFIGURATION.getAmountOfRetailers(),configurationDb.getAmountOfRetailers());
		Assert.assertEquals(CONFIGURATION.isContinuePlayingWhenBankrupt(),configurationDb.isContinuePlayingWhenBankrupt());
		Assert.assertEquals(CONFIGURATION.isInsightFacilities(),configurationDb.isInsightFacilities());
	}

	@Test
	void readConfigurationsTest() {
		configurationDAO.createConfiguration(CONFIGURATION);
		configurationDAO.createConfiguration(CONFIGURATION2);
		Assert.assertEquals(2,configurationDAO.readConfigurations().size());
	}

	@Test
	void updateConfigurationsTest() {
		configurationDAO.createConfiguration(CONFIGURATION);
		configurationDAO.updateConfigurations(CONFIGURATION3);

		Configuration configurationDb = configurationDAO.readConfiguration("BeerGameZutphen13_12_2018");

		//Test if there an insert in not triggered instead of an update
		Assert.assertEquals(1,configurationDAO.readConfigurations().size());

		//Test if the DAO updated the data correctly
		Assert.assertEquals(CONFIGURATION3.getGameId(),configurationDb.getGameId());
		Assert.assertEquals(CONFIGURATION3.getAmountOfRounds(),configurationDb.getAmountOfRounds());
		Assert.assertEquals(CONFIGURATION3.getMaximumOrderRetail(),configurationDb.getMaximumOrderRetail());
		Assert.assertEquals(CONFIGURATION3.getMinimalOrderRetail(),configurationDb.getMinimalOrderRetail());
		Assert.assertEquals(CONFIGURATION3.getAmountOfFactories(),configurationDb.getAmountOfFactories());
		Assert.assertEquals(CONFIGURATION3.getAmountOfDistributors(),configurationDb.getAmountOfDistributors());
		Assert.assertEquals(CONFIGURATION3.getAmountOfWholesales(),configurationDb.getAmountOfWholesales());
		Assert.assertEquals(CONFIGURATION3.getAmountOfRetailers(),configurationDb.getAmountOfRetailers());
		Assert.assertEquals(CONFIGURATION3.isContinuePlayingWhenBankrupt(),configurationDb.isContinuePlayingWhenBankrupt());
		Assert.assertEquals(CONFIGURATION3.isInsightFacilities(),configurationDb.isInsightFacilities());

	}

	@Test
	void deleteConfigurationsTest() {
		configurationDAO.createConfiguration(CONFIGURATION);
		configurationDAO.deleteConfigurations("BeerGameZutphen13_12_2018");
		Assert.assertEquals(0,configurationDAO.readConfigurations().size());
	}
}