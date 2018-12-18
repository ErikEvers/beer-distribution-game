package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.ProgrammedAgent;
import org.han.ica.asd.c.model.dao_model.ProgrammedBusinessRules;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

class ProgrammedBusinessRulesDAOTest {
    private static final Logger LOGGER = Logger.getLogger(FacilityTypeDAOIntegrationTest.class.getName());
    private static final ProgrammedAgent PROGRAMMED_AGENT = new ProgrammedAgent("name");
    private static final ProgrammedBusinessRules PROGRAMMED_BUSINESS_RULES = new ProgrammedBusinessRules("name", "businessrule1", "ast1");
    private static final ProgrammedBusinessRules PROGRAMMED_BUSINESS_RULES2 = new ProgrammedBusinessRules("name", "businessrule2", "ast2");
    private static final ProgrammedBusinessRules PROGRAMMED_BUSINESS_RULES2_UPDATE = new ProgrammedBusinessRules("name", "businessrule2", "ast2");

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

        ProgrammedBusinessRules programmedBusinessRulesDb = programmedBusinessRulesDAO.readAllProgrammedBusinessRulesFromAProgrammedAgent(PROGRAMMED_AGENT).get(0);

        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES.getProgrammedAgentName(),programmedBusinessRulesDb.getProgrammedAgentName());
        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES.getProgrammedBusinessRule(),programmedBusinessRulesDb.getProgrammedBusinessRule());
        Assert.assertEquals(PROGRAMMED_BUSINESS_RULES.getProgrammedAST(),programmedBusinessRulesDb.getProgrammedAST());
    }

    @Test
    void deleteSpecificProgrammedBusinessRule() {
    }

    @Test
    void deleteAllProgrammedBusinessRulesForAProgrammedAgent() {
    }
}