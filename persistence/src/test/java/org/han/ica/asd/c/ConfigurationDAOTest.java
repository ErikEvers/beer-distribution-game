package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.Configuration;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(PowerMockRunner.class)
class ConfigurationDAOTest {
	private static final Logger LOGGER = Logger.getLogger(BeerGameDAOIntegrationTest.class.getName());

	private ConfigurationDAO configurationDAO;
	private static final Configuration CONFIGURATION = new Configuration("BeerGameZutphen13_12_2018",40,1,1,1,1,1,99,false,false);



	@BeforeEach
	public void setUp() {
		setUpDatabase();
	}

	@AfterEach
	public void tearDown() {
		DBConnectionTest.getInstance().cleanup();
	}


	@Test
	void createConfigurationTest() {
		configurationDAO = new ConfigurationDAO();
		DatabaseConnection connection = DBConnectionTest.getInstance();
		setDatabaseConnection(configurationDAO, connection);

		configurationDAO.createConfiguration(CONFIGURATION);
		Configuration configurationDb = configurationDAO.readConfiguration("BeerGameZutphen13_12_2018");

		//Test if object values are the same
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
	}

	@Test
	void updateConfigurationsTest() {
	}

	@Test
	void deleteConfigurationsTest() {
	}

	private void setUpDatabase(){
		DBConnectionTest.getInstance().createNewDatabase();
	}

	private void setDatabaseConnection(ConfigurationDAO configurationDAO, DatabaseConnection connection) {
		try {
			Field connField = configurationDAO.getClass().getDeclaredField("databaseConnection");
			connField.setAccessible(true);
			connField.set(configurationDAO, connection);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}
}