package org.han.ica.asd.c.wrapper;

import org.han.ica.asd.c.model.dao_model.ConfigurationDB;
import org.han.ica.asd.c.model.dao_model.IDaoModel;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.IDomainModel;

import javax.inject.Inject;

public class ConfigurationWrapper implements Wrapper {

	@Inject
	private ConfigurationDB configurationDB;

	@Inject
	private Configuration configuration;

	public ConfigurationWrapper(){
		//Empty constructor for GUICE
	}

	@Override
	public IDaoModel wrapToDaoModel(IDomainModel model) {
		configuration = (Configuration) model;
		configurationDB.setAmountOfRounds(configuration.getAmountOfRounds());
		configurationDB.setAmountOfDistributors(configuration.getAmountOfDistributors());
		configurationDB.setAmountOfFactories(configuration.getAmountOfFactories());
		configurationDB.setAmountOfRetailers(configuration.getAmountOfRetailers());
		configurationDB.setAmountOfWholesales(configuration.getAmountOfWholesales());
		configurationDB.setInsightFacilities(configuration.isInsightFacilities());
		configurationDB.setContinuePlayingWhenBankrupt(configuration.isContinuePlayingWhenBankrupt());
		configurationDB.setFacilities(configuration.getFacilities());
		configurationDB.setFacilitiesLinkedTo(configuration.getFacilitiesLinkedTo());
		configurationDB.setFacilityTypes(configuration.getFacilityTypes());

		return configurationDB;
	}

	@Override
	public IDomainModel wrapToDomainModel(IDaoModel model) {
		configurationDB = (ConfigurationDB) model;
		configuration.setAmountOfRounds(configurationDB.getAmountOfRounds());
		configuration.setAmountOfDistributors(configurationDB.getAmountOfDistributors());
		configuration.setAmountOfFactories(configurationDB.getAmountOfFactories());
		configuration.setAmountOfWholesales(configurationDB.getAmountOfWholesales());
		configuration.setAmountOfRetailers(configurationDB.getAmountOfRetailers());
		configuration.setInsightFacilities(configurationDB.isInsightFacilities());
		configuration.setContinuePlayingWhenBankrupt(configurationDB.isContinuePlayingWhenBankrupt());
		configuration.setFacilities(configurationDB.getFacilities());
		configuration.setFacilitiesLinkedTo(configurationDB.getFacilitiesLinkedTo());
		configuration.setFacilityTypes(configurationDB.getFacilityTypes());

		return configuration;
	}
}
