package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.ProgrammedBusinessRulesDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.ProgrammedAgentDB;
import org.han.ica.asd.c.model.dao_model.ProgrammedBusinessRulesDB;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

class ProgrammedBusinessRulesDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(ProgrammedBusinessRulesDAOIntegrationTest.class.getName());
    private static final ProgrammedAgentDB PROGRAMMED_AGENT = new ProgrammedAgentDB("name");
    private static final ProgrammedBusinessRulesDB PROGRAMMED_BUSINESS_RULES = new ProgrammedBusinessRulesDB("name", "businessrule1", "ast1");
    private static final ProgrammedBusinessRulesDB PROGRAMMED_BUSINESS_RULES2 = new ProgrammedBusinessRulesDB("name", "BusinessRule2", "ast2");

    private ProgrammedBusinessRulesDAO programmedBusinessRulesDAO;

    @BeforeEach
    void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
            }
        });

        DBConnectionTest.getInstance().createNewDatabase();
        programmedBusinessRulesDAO = injector.getInstance(ProgrammedBusinessRulesDAO.class);
    }

    @AfterEach
    void tearDown() {
        DBConnectionTest.getInstance().cleanup();
    }

    @Test
    void createProgrammedbusinessRule() {
        Assert.assertEquals(0, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());
        programmedBusinessRulesDAO.createProgrammedbusinessRule(PROGRAMMED_BUSINESS_RULES);
        Assert.assertEquals(1, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());

        ProgrammedBusinessRulesDB programmedBusinessRulesDb = programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).get(0);

        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES.getProgrammedAgentName(), programmedBusinessRulesDb.getProgrammedAgentName());
        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES.getProgrammedBusinessRule(), programmedBusinessRulesDb.getProgrammedBusinessRule());
        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES.getProgrammedAST(), programmedBusinessRulesDb.getProgrammedAST());
    }

    @Test
    void deleteSpecificProgrammedBusinessRule() {
        Assert.assertEquals(0, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());
        programmedBusinessRulesDAO.createProgrammedbusinessRule(PROGRAMMED_BUSINESS_RULES);
        Assert.assertEquals(1, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());

        programmedBusinessRulesDAO.createProgrammedbusinessRule(PROGRAMMED_BUSINESS_RULES2);
        Assert.assertEquals(2, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());
        programmedBusinessRulesDAO.deleteSpecificProgrammedBusinessRule(PROGRAMMED_BUSINESS_RULES);
        Assert.assertEquals(1, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());

        ProgrammedBusinessRulesDB programmedBusinessRulesDb = programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).get(0);

        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES2.getProgrammedAgentName(), programmedBusinessRulesDb.getProgrammedAgentName());
        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES2.getProgrammedBusinessRule(), programmedBusinessRulesDb.getProgrammedBusinessRule());
        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES2.getProgrammedAST(), programmedBusinessRulesDb.getProgrammedAST());
    }

    @Test
    void deleteAllProgrammedBusinessRulesForAProgrammedAgent() {
        Assert.assertEquals(0, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());
        programmedBusinessRulesDAO.createProgrammedbusinessRule(PROGRAMMED_BUSINESS_RULES);
        Assert.assertEquals(1, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());

        programmedBusinessRulesDAO.createProgrammedbusinessRule(PROGRAMMED_BUSINESS_RULES2);
        Assert.assertEquals(2, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());
        programmedBusinessRulesDAO.deleteAllProgrammedBusinessRulesForAProgrammedAgent(PROGRAMMED_AGENT);
        Assert.assertEquals(0, programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).size());
    }
}