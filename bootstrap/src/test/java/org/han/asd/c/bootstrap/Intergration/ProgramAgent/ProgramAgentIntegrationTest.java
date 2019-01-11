package org.han.asd.c.bootstrap.Intergration.ProgramAgent;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.asd.c.bootstrap.Intergration.DBConnectionIntegrationTest;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.persistence.BusinessRuleStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramAgentIntegrationTest {
    private IBusinessRules iBusinessRules;

    private IBusinessRuleStore iBusinessRuleStore;

    @BeforeEach
    void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).to(BusinessRuleHandler.class);
                bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStore.class);
                bind(IDatabaseConnection.class).to(DBConnectionIntegrationTest.class);
            }
        });
        DBConnectionIntegrationTest.getInstance().createNewDatabase();
        iBusinessRules = injector.getInstance(BusinessRuleHandler.class);
        iBusinessRuleStore = injector.getInstance(BusinessRuleStore.class);
    }

    @Test
    void testCreateDefaultProgramedAgent() {
        iBusinessRules.programAgent("Jeroen","default deliver 34 \ndefault order 34");

        List<String> agents = iBusinessRuleStore.getAllProgrammedAgents();

        List<String> businessRules = iBusinessRuleStore.readInputBusinessRules(agents.get(0));

        assertEquals(1,agents.size());
        assertEquals(2,businessRules.size());
    }

    @Test
    void testCreateProgramedAgentWithMultipleRules() {
        iBusinessRules.programAgent("Jeroen","default deliver 34 \ndefault order 34 \nif inventory is 34 then order 34");

        List<String> agents = iBusinessRuleStore.getAllProgrammedAgents();

        List<String> businessRules = iBusinessRuleStore.readInputBusinessRules(agents.get(0));

        assertEquals(1,agents.size());
        assertEquals(3,businessRules.size());
    }

    @Test
    void testCreateMultipleProgramedAgentsSavedBusinessRule() {
        iBusinessRules.programAgent("Jeroen","default deliver 34 \ndefault order 34 \n");
        iBusinessRules.programAgent("Klaas","default deliver 34 \ndefault order 34 \n");

        List<String> agents = iBusinessRuleStore.getAllProgrammedAgents();

        assertEquals(2,agents.size());
    }

    @Test
    void testCreateMultipleProgramedAgentsDifferentRules() {
        iBusinessRules.programAgent("Klaas","default deliver 34 \ndefault order 35 \n");
        iBusinessRules.programAgent("Jeroen","default deliver 60 \ndefault order 70 \nif inventory is 34 then order 65");

        List<String> agents = iBusinessRuleStore.getAllProgrammedAgents();

        List<String> businessRulesAgent1 = iBusinessRuleStore.readInputBusinessRules(agents.get(0));
        List<String> businessRulesAgent2 = iBusinessRuleStore.readInputBusinessRules(agents.get(1));

        assertEquals(2,agents.size());
        assertEquals(2,businessRulesAgent1.size());
        assertEquals(3,businessRulesAgent2.size());
    }

    @Test
    void testCreateProgramedAgentNotInserted() {
        iBusinessRules.programAgent("Klaas","default deadsasliver 34");

        List<String> agents = iBusinessRuleStore.getAllProgrammedAgents();

        assertEquals(0,agents.size());
    }

    @Test
    void testCreateTwoProgramedAgentsOneInserted() {
        iBusinessRules.programAgent("Klaas","default deadsasliver 34");
        iBusinessRules.programAgent("Jeroen","default deliver 34 \ndefault order 35 \n");

        List<String> agents = iBusinessRuleStore.getAllProgrammedAgents();

        List<String> businessRulesAgent = iBusinessRuleStore.readInputBusinessRules(agents.get(0));

        assertEquals(1,agents.size());
        assertEquals(2,businessRulesAgent.size());
    }

    @Test
    void testDeleteProgramedAgent() {
        iBusinessRules.programAgent("Jeroen","default deliver 34 \ndefault order 35 \n");

        List<String> agentsBefore = iBusinessRuleStore.getAllProgrammedAgents();

        iBusinessRuleStore.deleteProgrammedAgent(agentsBefore.get(0));

        List<String> agentsAfter = iBusinessRuleStore.getAllProgrammedAgents();

        assertEquals(1,agentsBefore.size());
        assertEquals(0,agentsAfter.size());
    }

    @Test
    void testDeleteProgramedAgentAndBusinessRules() {
        iBusinessRules.programAgent("Jeroen","default deliver 34 \ndefault order 35 \n");

        List<String> agentsBefore = iBusinessRuleStore.getAllProgrammedAgents();

        iBusinessRuleStore.deleteProgrammedAgent(agentsBefore.get(0));

        List<String>  businessRules = iBusinessRuleStore.readInputBusinessRules(agentsBefore.get(0));

        List<String> agentsAfter = iBusinessRuleStore.getAllProgrammedAgents();

        assertEquals(1,agentsBefore.size());
        assertEquals(0,agentsAfter.size());
        assertEquals(0,businessRules.size());
    }

    @Test
    void testEditProgramedAgent() {
        iBusinessRules.programAgent("Jeroen","default deliver 34 \ndefault order 35 \n");

        List<String> agentsBefore = iBusinessRuleStore.getAllProgrammedAgents();
        List<String>  businessRulesBefore = iBusinessRuleStore.readInputBusinessRules(agentsBefore.get(0));

        iBusinessRules.programAgent("Jeroen","default deliver 34 \ndefault order 35 \nif inventory is 34 then order 34");

        List<String> agentsAfter = iBusinessRuleStore.getAllProgrammedAgents();
        List<String>  businessRulesAfter = iBusinessRuleStore.readInputBusinessRules(agentsBefore.get(0));

        assertEquals(1,agentsBefore.size());
        assertEquals(2,businessRulesBefore.size());
        assertEquals(1,agentsAfter.size());
        assertEquals(3,businessRulesAfter.size());
    }
}
