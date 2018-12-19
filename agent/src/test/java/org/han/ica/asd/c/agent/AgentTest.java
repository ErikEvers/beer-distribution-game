package org.han.ica.asd.c.agent;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.public_interfaces.IBusinessRules;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class AgentTest {
    @Test
    void testEmptyActionReturnsGameAgentEmptyAction() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        return null;
                    }
                });
            }
        });

        agent = injector.getInstance(Agent.class);
        Method method = agent.getClass().getDeclaredMethod("retrieveActionFromBusinessRule", String.class, Round.class);
        method.setAccessible(true);
        GameAgentAction action = (GameAgentAction) method.invoke(agent, null, null);
        assertTrue(action instanceof GameAgentEmptyAction);
    }

    @Test
    void testOrderActionReturnsGameAgentOrderAction() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();
                        action.addChild(new ActionReference("order"));

                        Value value = new Value().addValue("11");
                        action.addChild(value);

                        return action;
                    }
                });
            }
        });

        agent = injector.getInstance(Agent.class);
        Method method = agent.getClass().getDeclaredMethod("retrieveActionFromBusinessRule", String.class, Round.class);
        method.setAccessible(true);
        GameAgentAction action = (GameAgentAction) method.invoke(agent, null, null);
        assertTrue(action instanceof GameAgentOrder);
    }

    @Test
    void testDeliverActionReturnsGameAgentDeliverAction() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();
                        action.addChild(new ActionReference("deliver"));

                        Value value = new Value().addValue("11");
                        action.addChild(value);

                        return action;
                    }
                });
            }
        });

        agent = injector.getInstance(Agent.class);
        Method method = agent.getClass().getDeclaredMethod("retrieveActionFromBusinessRule", String.class, Round.class);
        method.setAccessible(true);
        GameAgentAction action = (GameAgentAction) method.invoke(agent, null, null);
        assertTrue(action instanceof GameAgentDeliver);
    }

    @Test
    void testGenerateOrder() {
        Agent agent = new Agent();
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();
                        action.addChild(new ActionReference("order"));

                        Value value = new Value().addValue("30");
                        action.addChild(value);

                        return action;
                    }
                });
            }
        });
        injector.injectMembers(agent);
        Facility targetFacility = new Facility(new FacilityType(null, 0, 0, 0, 0, 0, 0), new ArrayList<>(), 1);


        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 and 0 < 1 then order 30", "awesomerepresentationofthetreeasastring"));

        List<Map<Facility, Integer>> result = agent.generateOrder();
        Map<Facility, Integer> map = result.get(0);
        Map.Entry<Facility, Integer> entry = map.entrySet().iterator().next();


        assertEquals(30, (int)entry.getValue());
    }

    @Test
    void testGenerateDeliver() {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();
                        action.addChild(new ActionReference("deliver"));

                        Value value = new Value().addValue("5");
                        action.addChild(value);

                        return action;
                    }
                });
            }
        });
        agent = injector.getInstance(Agent.class);

        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 and 0 < 1 then order 30", "awesomerepresentationofthetreeasastring"));

        List<Map<Facility, Integer>> result = agent.generateOrder();
        Map<Facility, Integer> map = result.get(1);
        Map.Entry<Facility, Integer> entry = map.entrySet().iterator().next();


        assertEquals(5, (int)entry.getValue());
    }

    @Test
    void testGenerateDeliverAndOrder() {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();

                        if ("if inventory higher than 10 then order 10".equals(businessRule)){
                            action.addChild(new ActionReference("deliver"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 20".equals(businessRule)){
                            action.addChild(new ActionReference("order"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 30".equals(businessRule)){
                            return null;
                        }

                        return action;
                    }
                });
            }
        });
        agent = injector.getInstance(Agent.class);

        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 10", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 20", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 30", "awesomerepresentationofthetreeasastring"));

        List<Map<Facility, Integer>> result = agent.generateOrder();

        assertEquals(1, result.get(0).size());
        assertEquals(1, result.get(1).size());
    }

    @Test
    void testGenerateMultipleOrder() {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();

                        if ("if inventory higher than 10 then order 10".equals(businessRule)){
                            action.addChild(new ActionReference("order"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 20".equals(businessRule)){
                            action.addChild(new ActionReference("order"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 30".equals(businessRule)){
                            return null;
                        }

                        return action;
                    }
                });
            }
        });
        agent = injector.getInstance(Agent.class);

        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 10", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 20", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 30", "awesomerepresentationofthetreeasastring"));

        List<Map<Facility, Integer>> result = agent.generateOrder();

        assertEquals(1, result.get(0).size());
        assertEquals(0, result.get(1).size());
    }

    @Test
    void testGenerateMultipleDeliver() {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();

                        if ("if inventory higher than 10 then order 10".equals(businessRule)){
                            action.addChild(new ActionReference("deliver"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 20".equals(businessRule)){
                            action.addChild(new ActionReference("deliver"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 30".equals(businessRule)){
                            return null;
                        }

                        return action;
                    }
                });
            }
        });
        agent = injector.getInstance(Agent.class);

        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 10", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 20", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 30", "awesomerepresentationofthetreeasastring"));

        List<Map<Facility, Integer>> result = agent.generateOrder();

        assertEquals(0, result.get(0).size());
        assertEquals(1, result.get(1).size());
    }

    @Test
    void testGenerateMultipleDeliverAndOneOrder() {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();

                        if ("if inventory higher than 10 then order 10".equals(businessRule)){
                            action.addChild(new ActionReference("deliver"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 20".equals(businessRule)){
                            action.addChild(new ActionReference("deliver"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 30".equals(businessRule)){
                            action.addChild(new ActionReference("order"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        }

                        return action;
                    }
                });
            }
        });
        agent = injector.getInstance(Agent.class);

        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 10", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 20", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 30", "awesomerepresentationofthetreeasastring"));

        List<Map<Facility, Integer>> result = agent.generateOrder();

        assertEquals(1, result.get(0).size());
        assertEquals(1, result.get(1).size());
    }

    @Test
    void testGenerateOneDeliverAndMultipleOrder() {
        Agent agent;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).toInstance(new IBusinessRules() {
                    @Override
                    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
                        return null;
                    }

                    @Override
                    public Action evaluateBusinessRule(String businessRule, Round roundData) {
                        Action action = new Action();

                        if ("if inventory higher than 10 then order 10".equals(businessRule)){
                            action.addChild(new ActionReference("deliver"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 20".equals(businessRule)){
                            action.addChild(new ActionReference("order"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        } else if ("if inventory higher than 10 then order 30".equals(businessRule)){
                            action.addChild(new ActionReference("order"));
                            Value value = new Value().addValue("5");
                            action.addChild(value);
                        }

                        return action;
                    }
                });
            }
        });
        agent = injector.getInstance(Agent.class);

        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 10", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 20", "awesomerepresentationofthetreeasastring"));
        agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 then order 30", "awesomerepresentationofthetreeasastring"));

        List<Map<Facility, Integer>> result = agent.generateOrder();

        assertEquals(1, result.get(0).size());
        assertEquals(1, result.get(1).size());
    }
}
