package org.han.ica.asd.c.wrapper;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.model.dao_model.ConfigurationDB;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityLinkedTo;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class ConfigurationWrapperTest {
	private static final Logger LOGGER = Logger.getLogger(ConfigurationWrapperTest.class.getName());
	private Wrapper configurationWrapper;
	private Configuration configuration;
	private ConfigurationDB configurationDB;
	private List<Facility> facilities;
	private List<FacilityLinkedTo> facilityLinkedTos;
	private List<FacilityType> facilityTypes;

	@BeforeEach
	void setUp() {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure(){
				bind(Configuration.class).toInstance(new Configuration());
				bind(ConfigurationDB.class).toInstance(new ConfigurationDB());
			}
		});
		facilities = new ArrayList<>();
		facilityLinkedTos = new ArrayList<>();
		facilityTypes = new ArrayList<>();
		configurationWrapper = injector.getInstance(ConfigurationWrapper.class);
		configuration = new Configuration(1, 1, 1, 1, 1, 1, 1, true, true, facilities, facilityLinkedTos,facilityTypes);
		configurationDB = new ConfigurationDB("BeerGameZutphen",1, 1, 1, 1, 1, 1, 1, true, true);
	}


	@Test
	void wrapToDaoModelTest() {
		Configuration contemp = null;
		try {
			contemp = (Configuration) configurationWrapper.startWrapping(configurationDB);
		} catch (InvalidObjectException e) {
			LOGGER.log(Level.SEVERE,e.toString(),e);
		}
		Assert.assertEquals(contemp.getAmountOfDistributors(),configuration.getAmountOfDistributors());
		Assert.assertEquals(contemp.getAmountOfFactories(),configuration.getAmountOfFactories());
		Assert.assertEquals(contemp.getAmountOfRetailers(),contemp.getAmountOfRetailers());
		Assert.assertEquals(contemp.getAmountOfWholesales(),contemp.getAmountOfWholesales());
		Assert.assertEquals(contemp.getAmountOfRounds(),contemp.getAmountOfRounds());
	}

	@Test
	void wrapToDomainModelTest() {
		ConfigurationDB contemp = null;
		try {
			contemp = (ConfigurationDB) configurationWrapper.startWrapping(configuration);
		} catch (InvalidObjectException e) {
			LOGGER.log(Level.SEVERE,e.toString(),e);
		}

		Assert.assertEquals(contemp.getAmountOfDistributors(),configurationDB.getAmountOfDistributors());
		Assert.assertEquals(contemp.getAmountOfFactories(),configurationDB.getAmountOfFactories());
		Assert.assertEquals(contemp.getAmountOfRetailers(),configurationDB.getAmountOfRetailers());
		Assert.assertEquals(contemp.getAmountOfWholesales(),configurationDB.getAmountOfWholesales());
		Assert.assertEquals(contemp.getAmountOfRounds(),configurationDB.getAmountOfRounds());

	}
}