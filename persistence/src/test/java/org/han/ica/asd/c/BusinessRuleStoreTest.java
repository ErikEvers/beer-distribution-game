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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

class BusinessRuleStoreTest {

    private BusinessRuleStore businessRuleStore;
    private ProgrammedBusinessRulesDAO programmedBusinessRulesDAOMock;
    private ProgrammedAgentDAO programmedAgentDAOMock;
    private static List<ProgrammedBusinessRules> programmedBusinessRules;
    private static List<ProgrammedAgent> programmedAgents;
    private static final ProgrammedBusinessRules PROGRAMMED_BUSINESS_RULES = new ProgrammedBusinessRules("BusinessRule1", "AST1");
    private static final String PROGRAMMED_AGENT_NAME = "ProgrammedAgentName";
    private static final ProgrammedAgent PROGRAMMED_AGENT = new ProgrammedAgent(PROGRAMMED_AGENT_NAME, programmedBusinessRules);

    @BeforeEach
    void setup(){
        programmedBusinessRules.add(PROGRAMMED_BUSINESS_RULES);
        programmedAgents.add(PROGRAMMED_AGENT);

        programmedBusinessRulesDAOMock = mock(ProgrammedBusinessRulesDAO.class);

		Mockito.doNothing().when(programmedBusinessRulesDAOMock).readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT_NAME);
		when((programmedBusinessRulesDAOMock).readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT_NAME)).thenReturn(programmedBusinessRules);
		Mockito.doNothing().when(programmedBusinessRulesDAOMock).createProgrammedbusinessRule(PROGRAMMED_BUSINESS_RULES, PROGRAMMED_AGENT_NAME);
		Mockito.doNothing().when(programmedBusinessRulesDAOMock).deleteAllProgrammedBusinessRulesForAProgrammedAgent(PROGRAMMED_AGENT_NAME);

		programmedAgentDAOMock = mock(ProgrammedAgentDAO.class);

		Mockito.doNothing().when(programmedAgentDAOMock).readAllProgrammedAgents();
		when((programmedAgentDAOMock).readAllProgrammedAgents()).thenReturn(programmedAgents);
		Mockito.doNothing().when(programmedAgentDAOMock).deleteProgrammedAgent(PROGRAMMED_AGENT);

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
    }

    @Test
    void synchronizeBusinessRules() {
    }

    @Test
    void getAllProgrammedAgents() {
    }

    @Test
    void deleteProgrammedAgent() {
    }
}