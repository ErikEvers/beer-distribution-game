package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.ProgrammedAgent;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgrammedAgentDAOIntegrationTest {
    private static final ProgrammedAgent PROGRAMMED_AGENT = new ProgrammedAgent("Name1");
    private static final ProgrammedAgent PROGRAMMED_AGENT_UPDATE = new ProgrammedAgent("Name1_Updated");
    private static final ProgrammedAgent PROGRAMMED_AGENT2 = new ProgrammedAgent("Name2");

    private ProgrammedAgentDAO programmedAgentDAO;

    @BeforeEach
    void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(DatabaseConnection.class).to(DBConnectionTest.class);
            }
        });

        DBConnectionTest.getInstance().createNewDatabase();
        programmedAgentDAO = injector.getInstance(ProgrammedAgentDAO.class);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
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