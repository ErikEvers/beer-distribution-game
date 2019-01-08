package org.han.ica.asd.c.agent;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.GameRoundAction;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class AgentTest {
    private static final String BUSINESS_RULES = "businessRules";
    private static final String RESOLVE_LOWER_FACILITY_ID = "resolveLowerFacilityId";
    private static final String RESOLVE_HIGHER_FACILITY_ID = "resolveHigherFacilityId";
    private static final String ORDER = "order";
    private static final String DELIVER = "deliver";
    private static final String IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10 = "if inventory higher than 10 then order 10";
    private static final String IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20 = "if inventory higher than 10 then order 20";
    private static final String IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30 = "if inventory higher than 10 then order 30";
    private static final String BUSINESS_RULE_1 = "BR(V(1))";
    private static final String BUSINESS_RULE_2 = "BR(V(2))";
    private static final String BUSINESS_RULE_3 = "BR(V(3))";
    private static final String PERSISTENCE = "persistence";

    private IPersistence persistence = new IPersistence() {
        @Override
        public void savePlayerTurn(Round data) {
        }

        @Override
        public Round fetchPlayerTurn(int roundId, int facilityId) {
            return null;
        }

        @Override
        public void saveRoundData(Round data) {
        }

        @Override
        public Round fetchRoundData(int roundId) {
            return null;
        }

        @Override
        public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
        }
    };

    private Facility mainFacility;
    private Facility upperFacility;
    private Facility lowerFacility;
    private Configuration configuration;
    private Round round;

    @BeforeEach
    private void init() {
        this.mainFacility = new Facility(
                new FacilityType("", 0, 0, 0, 0, 0, 0, 0),
                1);
        this.upperFacility = new Facility(new FacilityType(), 0);
        this.lowerFacility = new Facility(new FacilityType(), 2);
        this.configuration = new Configuration();

        List<Facility> list = new ArrayList<>();
        list.add(mainFacility);
        configuration.getFacilitiesLinkedTo().put(upperFacility, list);

        list = new ArrayList<>();
        list.add(lowerFacility);
        configuration.getFacilitiesLinkedTo().put(mainFacility, list);

        round = new Round();
        round.setRoundId(0);
    }

    private List<GameBusinessRules> gameBusinessRuleList = Lists.newArrayList(
            new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10, BUSINESS_RULE_1),
            new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20, BUSINESS_RULE_2),
            new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30, BUSINESS_RULE_3));


    @Test
    void testGenerateOrder() {
        Agent agent = new Agent(configuration, "", mainFacility,
                Lists.newArrayList(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 and 0 < 1 then order 30", "awesomerepresentationofthetreeasastring")));
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return Collections.emptyList();
                    }

                    @Override
                    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
                        return new ActionModel(ORDER, 30, lowerFacility.getFacilityId());
                    }
                });
                bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
            }
        });
        injector.injectMembers(agent);

        GameRoundAction result = agent.executeTurn(round);
        Map.Entry<Facility, Integer> entry = result.targetOrderMap.entrySet().iterator().next();

        assertEquals(30, (int) entry.getValue());
    }

    @Test
    void testGenerateDeliver() {
        Agent agent = new Agent(configuration, "", mainFacility,
                Lists.newArrayList(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 and 0 < 1 then order 30", "awesomerepresentationofthetreeasastring")));
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return Collections.emptyList();
                    }

                    @Override
                    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
                        return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
                    }
                });
                bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
            }
        });
        injector.injectMembers(agent);
        GameRoundAction result = agent.executeTurn(round);
        Map.Entry<Facility, Integer> entry = result.targetDeliverMap.entrySet().iterator().next();
        ;

        assertEquals(5, (int) entry.getValue());
    }

    @Test
    void testGenerateDeliverAndOrder() {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return Collections.emptyList();
                    }

                    @Override
                    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
                        switch (businessRule) {
                            case BUSINESS_RULE_1:
                                return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
                            case BUSINESS_RULE_2:
                                return new ActionModel(ORDER, 5, lowerFacility.getFacilityId());
                        }
                        return null;
                    }
                });
                bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
            }
        });
        injector.injectMembers(agent);

        GameRoundAction result = agent.executeTurn(round);

        assertEquals(1, result.targetOrderMap.size());
        assertEquals(1, result.targetDeliverMap.size());
    }

    @Test
    void testGenerateMultipleOrders() {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return Collections.emptyList();
                    }

                    @Override
                    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
                        if (!BUSINESS_RULE_3.equals(businessRule)) {
                            return new ActionModel(ORDER, 5, lowerFacility.getFacilityId());
                        }
                        return null;
                    }
                });
                bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
            }
        });
        injector.injectMembers(agent);

        GameRoundAction result = agent.executeTurn(round);

        assertEquals(1, result.targetOrderMap.size());
        assertEquals(0, result.targetDeliverMap.size());
    }

    @Test
    void testGenerateMultipleDelivers() {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return Collections.emptyList();
                    }

                    @Override
                    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
                        if (!BUSINESS_RULE_3.equals(businessRule)) {
                            return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
                        }
                        return null;
                    }
                });
                bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
            }
        });
        injector.injectMembers(agent);

        GameRoundAction result = agent.executeTurn(round);

        assertEquals(0, result.targetOrderMap.size());
        assertEquals(1, result.targetDeliverMap.size());
    }

    @Test
    void testGenerateMultipleDeliversAndOneOrder() {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return Collections.emptyList();
                    }

                    @Override
                    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
                        if (BUSINESS_RULE_3.equals(businessRule)) {
                            return new ActionModel(ORDER, 5, lowerFacility.getFacilityId());
                        }
                        return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
                    }
                });
                bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
            }
        });
        injector.injectMembers(agent);

        GameRoundAction result = agent.executeTurn(round);

        assertEquals(1, result.targetOrderMap.size());
        assertEquals(1, result.targetDeliverMap.size());
    }

    @Test
    void testGenerateOneDeliverAndMultipleOrders() {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return Collections.emptyList();
                    }

                    @Override
                    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
                        if (BUSINESS_RULE_1.equals(businessRule)) {
                            return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
                        }
                        return new ActionModel(ORDER, 5, lowerFacility.getFacilityId());
                    }
                });
                bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
            }
        });
        injector.injectMembers(agent);

        GameRoundAction result = agent.executeTurn(round);

        assertEquals(1, result.targetOrderMap.size());
        assertEquals(1, result.targetDeliverMap.size());
    }

    @Test
    void testResolveLowerFacilityIdExpectsMockedFacility() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Method method = agent.getClass().getDeclaredMethod(RESOLVE_LOWER_FACILITY_ID, int.class);
        method.setAccessible(true);
        Facility resultFacility = (Facility) method.invoke(agent, lowerFacility.getFacilityId());
        assertTrue(resultFacility != null);
    }

    @Test
    void testResolveLowerFacilityIdExpectsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Method method = agent.getClass().getDeclaredMethod(RESOLVE_LOWER_FACILITY_ID, int.class);
        method.setAccessible(true);
        Facility resultFacility = (Facility) method.invoke(agent, upperFacility.getFacilityId());
        assertNull(resultFacility);
    }

    @Test
    void testResolveHigherFacilityIdExpectsMockedFacility() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Method method = agent.getClass().getDeclaredMethod(RESOLVE_HIGHER_FACILITY_ID, int.class);
        method.setAccessible(true);
        Facility resultFacility = (Facility) method.invoke(agent, upperFacility.getFacilityId());
        assertTrue(resultFacility != null);
    }

    @Test
    void testResolveHigherFacilityIdExpectsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        Method method = agent.getClass().getDeclaredMethod(RESOLVE_HIGHER_FACILITY_ID, int.class);
        method.setAccessible(true);
        Facility resultFacility = (Facility) method.invoke(agent, lowerFacility.getFacilityId());
        assertNull(resultFacility);
    }

    @Test
    void testCallingLogWhenTriggeringBusinessRulesExpectsMethodCall() {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
        IPersistence persistenceMock = mock(IPersistence.class);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return Collections.emptyList();
                    }

                    @Override
                    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
                        if (BUSINESS_RULE_1.equals(businessRule)) {
                            return new ActionModel(DELIVER, 5, mainFacility.getFacilityId());
                        }
                        return new ActionModel(ORDER, 5, mainFacility.getFacilityId());
                    }
                });
                bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistenceMock);
            }
        });
        injector.injectMembers(agent);

        agent.executeTurn(round);

        verify(persistenceMock, times(1)).logUsedBusinessRuleToCreateOrder(any(GameBusinessRulesInFacilityTurn.class));
        verify(persistenceMock, times(1)).logUsedBusinessRuleToCreateOrder(any(GameBusinessRulesInFacilityTurn.class));
    }

    @Test
    void testGetParticipant() {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);

        assertEquals(mainFacility, agent.getParticipant());
    }
}
