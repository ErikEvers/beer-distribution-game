package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.ProgrammedBusinessRulesDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessRuleStoreTest {

    private BusinessRuleStore businessRuleStore;

    @BeforeEach
    void setup(){
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ProgrammedBusinessRulesDAO.class);
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
}