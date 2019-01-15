package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedBusinessRules;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ProgrammedAgentDAOIntegrationTest {
    private static List<ProgrammedBusinessRules> programmedBusinessRules = new ArrayList<>();
    private static final ProgrammedAgent PROGRAMMED_AGENT = new ProgrammedAgent("Name1", programmedBusinessRules);
    private static final ProgrammedAgent PROGRAMMED_AGENT_UPDATE = new ProgrammedAgent("Name1_Updated", programmedBusinessRules);
    private static final ProgrammedAgent PROGRAMMED_AGENT2 = new ProgrammedAgent("Name2", programmedBusinessRules);

    private ProgrammedAgentDAO programmedAgentDAO;

    @BeforeEach
    void setUp() {
        DBConnectionTest.getInstance().cleanup();
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
                bind(ProgrammedBusinessRulesDAO.class);
            }
        });

        DBConnectionTest.getInstance().createNewDatabase();
        programmedAgentDAO = injector.getInstance(ProgrammedAgentDAO.class);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
        DaoConfig.clearCurrentGameId();
    }

    @Test
    void createProgrammedAgent() {
        Assert.assertEquals(0, programmedAgentDAO.readAllProgrammedAgents().size());
        programmedAgentDAO.createProgrammedAgent(PROGRAMMED_AGENT);
        Assert.assertEquals(1, programmedAgentDAO.readAllProgrammedAgents().size());

        ProgrammedAgent programmedAgentDb = programmedAgentDAO.readAllProgrammedAgents().get(0);

        Assert.assertEquals(PROGRAMMED_AGENT.getProgrammedAgentName(), programmedAgentDb.getProgrammedAgentName());
    }

    @Test
    void updateProgrammedAgent() {
        Assert.assertEquals(0, programmedAgentDAO.readAllProgrammedAgents().size());
        programmedAgentDAO.createProgrammedAgent(PROGRAMMED_AGENT);
        Assert.assertEquals(1, programmedAgentDAO.readAllProgrammedAgents().size());
        programmedAgentDAO.updateProgrammedAgent(PROGRAMMED_AGENT, PROGRAMMED_AGENT_UPDATE);
        Assert.assertEquals(1, programmedAgentDAO.readAllProgrammedAgents().size());

        ProgrammedAgent programmedAgentDb = programmedAgentDAO.readAllProgrammedAgents().get(0);

        Assert.assertEquals(PROGRAMMED_AGENT_UPDATE.getProgrammedAgentName(), programmedAgentDb.getProgrammedAgentName());
    }

    @Test
    void deleteProgrammedAgent() {
        Assert.assertEquals(0, programmedAgentDAO.readAllProgrammedAgents().size());
        programmedAgentDAO.createProgrammedAgent(PROGRAMMED_AGENT);
        programmedAgentDAO.createProgrammedAgent(PROGRAMMED_AGENT2);
        Assert.assertEquals(2, programmedAgentDAO.readAllProgrammedAgents().size());
        programmedAgentDAO.deleteProgrammedAgent(PROGRAMMED_AGENT2);
        Assert.assertEquals(1, programmedAgentDAO.readAllProgrammedAgents().size());
    }
}