package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
import org.han.ica.asd.c.dao.ProgrammedBusinessRulesDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
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
import static org.powermock.api.mockito.PowerMockito.when;

class BusinessRuleStoreTest {

    private BusinessRuleStore businessRuleStore;
    private ProgrammedBusinessRulesDAO programmedBusinessRulesDAOMock;
    private ProgrammedAgentDAO programmedAgentDAOMock;
    private static List<ProgrammedBusinessRules> programmedBusinessRules = new ArrayList<>();
    private static List<ProgrammedAgent> programmedAgents = new ArrayList<>();
    private static final ProgrammedBusinessRules PROGRAMMED_BUSINESS_RULES = new ProgrammedBusinessRules("BusinessRule1", "AST1");
    private static final String PROGRAMMED_AGENT_NAME = "ProgrammedAgentName";
    private static final ProgrammedAgent PROGRAMMED_AGENT = new ProgrammedAgent(PROGRAMMED_AGENT_NAME, programmedBusinessRules);
    private static Map<String, String> businessRuleMap = new HashMap<>();

    @BeforeEach
    void setup(){
        programmedBusinessRules.add(PROGRAMMED_BUSINESS_RULES);
        programmedAgents.add(PROGRAMMED_AGENT);
        businessRuleMap.put(PROGRAMMED_BUSINESS_RULES.getProgrammedBusinessRule(), PROGRAMMED_BUSINESS_RULES.getProgrammedAST());

        programmedBusinessRulesDAOMock = mock(ProgrammedBusinessRulesDAO.class);

		when((programmedBusinessRulesDAOMock).readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT_NAME)).thenReturn(programmedBusinessRules);
		Mockito.doNothing().when(programmedBusinessRulesDAOMock).createProgrammedbusinessRule(any(ProgrammedBusinessRules.class), anyString());
		Mockito.doNothing().when(programmedBusinessRulesDAOMock).deleteAllProgrammedBusinessRulesForAProgrammedAgent(anyString());

		programmedAgentDAOMock = mock(ProgrammedAgentDAO.class);

		when((programmedAgentDAOMock).readAllProgrammedAgents()).thenReturn(programmedAgents);
		Mockito.doNothing().when(programmedAgentDAOMock).deleteProgrammedAgent(any(ProgrammedAgent.class));

		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).toInstance(DBConnectionTest.getInstance());
				bind(ProgrammedBusinessRulesDAO.class).toInstance(programmedBusinessRulesDAOMock);
				bind(ProgrammedAgentDAO.class).toInstance(programmedAgentDAOMock);
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
        Assert.assertEquals(programmedAgentsDB.size(), programmedAgents.size());
        Assert.assertEquals(programmedAgentsDB.get(0),PROGRAMMED_AGENT_NAME);
    }

    @Test
    void deleteProgrammedAgent() {
        businessRuleStore.deleteProgrammedAgent(anyString());
        verify(((ProgrammedAgentDAO)programmedAgentDAOMock), times(1)).deleteProgrammedAgent(any(ProgrammedAgent.class));
    }
}