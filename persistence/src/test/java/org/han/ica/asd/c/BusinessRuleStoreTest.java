package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.FacilityDAO;
import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
import org.han.ica.asd.c.dao.ProgrammedBusinessRulesDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedBusinessRules;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BusinessRuleStoreTest {

    private BusinessRuleStore businessRuleStore;
    private ProgrammedBusinessRulesDAO programmedBusinessRulesDAOMock;
    private ProgrammedAgentDAO programmedAgentDAOMock;
    private FacilityDAO facilityDAOMock;
    private static List<ProgrammedBusinessRules> programmedBusinessRules = new ArrayList<>();
    private static List<ProgrammedAgent> programmedAgents = new ArrayList<>();
    private static final ProgrammedBusinessRules PROGRAMMED_BUSINESS_RULES = new ProgrammedBusinessRules("BusinessRule1", "AST1");
    private static final String PROGRAMMED_AGENT_NAME = "ProgrammedAgentName";
    private static final ProgrammedAgent PROGRAMMED_AGENT = new ProgrammedAgent(PROGRAMMED_AGENT_NAME, programmedBusinessRules);
    private static Map<String, String> businessRuleMap = new HashMap<>();
    private static final FacilityType FACTORYTYPE = new FacilityType("Factory", 1, 1, 1, 1, 1, 1, 1);
    private static final FacilityType DISTRIBUTORTYPE = new FacilityType("Distributor", 1, 1, 1, 1, 1, 1, 1);
    private static final FacilityType WHOLESALERTYPE = new FacilityType("Wholesaler", 1, 1, 1, 1, 1, 1, 1);
    private static final FacilityType RETAILERTYPE = new FacilityType("Retailer", 1, 1, 1, 1, 1, 1, 1);
    private static final Facility FACTORY = new Facility(FACTORYTYPE, 1);
    private static final Facility DISTRIBUTOR = new Facility(DISTRIBUTORTYPE, 2);
    private static final Facility WHOLESALER = new Facility(WHOLESALERTYPE, 3);
    private static final Facility RETAILER = new Facility(RETAILERTYPE, 4);

    @BeforeEach
    void setup(){
        //Adding the BusinessRule/Agent to a list so it is conform to the implementation.
        programmedBusinessRules.add(PROGRAMMED_BUSINESS_RULES);
        programmedAgents.add(PROGRAMMED_AGENT);

        //Setup a BusinessRulesMap that can be send by a player in game.
        businessRuleMap.put(PROGRAMMED_BUSINESS_RULES.getProgrammedBusinessRule(), PROGRAMMED_BUSINESS_RULES.getProgrammedAST());

        //Setup for retrieving all the facilities of a facilityType within a game.
        List<Facility> facilitiesInGame = new ArrayList<>();

        facilitiesInGame.add(FACTORY);
        facilitiesInGame.add(DISTRIBUTOR);
        facilitiesInGame.add(WHOLESALER);
        facilitiesInGame.add(RETAILER);

        //The mock setup for the ProgrammedBusinessRulesDAO.
        programmedBusinessRulesDAOMock = mock(ProgrammedBusinessRulesDAO.class);

		when((programmedBusinessRulesDAOMock).readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT_NAME)).thenReturn(programmedBusinessRules);
		Mockito.doNothing().when(programmedBusinessRulesDAOMock).createProgrammedbusinessRule(any(ProgrammedBusinessRules.class), anyString());
		Mockito.doNothing().when(programmedBusinessRulesDAOMock).deleteAllProgrammedBusinessRulesForAProgrammedAgent(anyString());

        //The mock setup for the ProgrammedAgentDAO.
        programmedAgentDAOMock = mock(ProgrammedAgentDAO.class);

		when((programmedAgentDAOMock).readAllProgrammedAgents()).thenReturn(programmedAgents);
		Mockito.doNothing().when(programmedAgentDAOMock).deleteProgrammedAgent(any(ProgrammedAgent.class));

        //The mock setup for the FacilityDAO.
        facilityDAOMock = mock(FacilityDAO.class);

		when((facilityDAOMock).readAllFacilitiesInGame()).thenReturn(facilitiesInGame);

		//The injector required for the Dependency Injection.
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).toInstance(DBConnectionTest.getInstance());
				bind(ProgrammedBusinessRulesDAO.class).toInstance(programmedBusinessRulesDAOMock);
				bind(ProgrammedAgentDAO.class).toInstance(programmedAgentDAOMock);
				bind(FacilityDAO.class).toInstance(facilityDAOMock);
			}
		});

        businessRuleStore = injector.getInstance(BusinessRuleStore.class);
    }

    @Test
    void readInputBusinessRules() {
            List<String> businessRules = businessRuleStore.readInputBusinessRules(PROGRAMMED_AGENT_NAME);
            Assert.assertEquals(businessRules.get(0), PROGRAMMED_BUSINESS_RULES.getProgrammedBusinessRule());
    }

    @Test
    void synchronizeBusinessRules() {
        businessRuleStore.synchronizeBusinessRules(PROGRAMMED_AGENT_NAME, businessRuleMap);
		verify(((ProgrammedBusinessRulesDAO)programmedBusinessRulesDAOMock), times(1)).deleteAllProgrammedBusinessRulesForAProgrammedAgent(anyString());
		verify(((ProgrammedBusinessRulesDAO)programmedBusinessRulesDAOMock), times(1)).createProgrammedbusinessRule(any(ProgrammedBusinessRules.class), anyString());
    }

    @Test
    void getAllProgrammedAgents() {
        List<String> programmedAgentsDB = businessRuleStore.getAllProgrammedAgents();
        Assert.assertEquals(programmedAgents.size(),programmedAgentsDB.size());
        Assert.assertEquals(PROGRAMMED_AGENT_NAME,programmedAgentsDB.get(0));
    }

    @Test
    void deleteProgrammedAgent() {
        businessRuleStore.deleteProgrammedAgent(anyString());
        verify(((ProgrammedAgentDAO)programmedAgentDAOMock), times(1)).deleteProgrammedAgent(any(ProgrammedAgent.class));
    }

    @Test
    void getAllFacilities() {
        List<List<String>> facilities = businessRuleStore.getAllFacilities();
        Assert.assertEquals(4, facilities.size());
        Assert.assertEquals(1, facilities.get(0).size());
        Assert.assertEquals(1, facilities.get(1).size());
        Assert.assertEquals(1, facilities.get(2).size());
        Assert.assertEquals(1, facilities.get(3).size());
        Assert.assertEquals(Integer.toString(FACTORY.getFacilityId()), facilities.get(0).get(0));
        Assert.assertEquals(Integer.toString(DISTRIBUTOR.getFacilityId()), facilities.get(1).get(0));
        Assert.assertEquals(Integer.toString(WHOLESALER.getFacilityId()), facilities.get(2).get(0));
        Assert.assertEquals(Integer.toString(RETAILER.getFacilityId()), facilities.get(3).get(0));
    }
}