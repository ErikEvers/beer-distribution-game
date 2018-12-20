package org.han.ica.asd.c.agent;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.public_interfaces.IBusinessRules;
import org.han.ica.asd.c.model.domain_objects.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {
	private static final String RETRIEVE_ACTION_FROM_BUSINESS_RULE = "retrieveActionFromBusinessRule";
	private static final String BUSINESS_RULES = "businessRules";
	private static final String RESOLVE_FACILITY_ID = "resolveFacilityId";
	private static final String ORDER = "order";
	private static final String DELIVER = "deliver";
	private static final String IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10 = "if inventory higher than 10 then order 10";
	private static final String IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20 = "if inventory higher than 10 then order 20";
	private static final String IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30 = "if inventory higher than 10 then order 30";
	private static final String EMPTY_BUSINESS_RULE = "BR()";

	private Facility facility = new Facility(
			new FacilityType(null, 0, 0, 0, 0, 0, 0),
			Lists.newArrayList(new FacilityLinkedTo("1", new Facility(
					new FacilityType(null, 0, 0, 0, 0, 0, 0),
					new ArrayList<>(),
					0), true)),
			1);

	@Test
	void testEmptyActionReturnsGameAgentEmptyAction() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Agent agent;
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						return null;
					}
				});
			}
		});

		agent = injector.getInstance(Agent.class);
		Method method = agent.getClass().getDeclaredMethod(RETRIEVE_ACTION_FROM_BUSINESS_RULE, String.class, Round.class);
		method.setAccessible(true);
		GameAgentAction action = (GameAgentAction) method.invoke(agent, null, null);
		action.setTargetFacilityId(0);
		assertTrue(action instanceof GameAgentEmptyAction);
	}

	@Test
	void testOrderActionReturnsGameAgentOrderAction() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Agent agent;
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();
						action.addChild(new ActionReference(ORDER));

						Value value = new Value().addValue("11");
						action.addChild(value);

						return action;
					}
				});
			}
		});

		agent = injector.getInstance(Agent.class);
		Method method = agent.getClass().getDeclaredMethod(RETRIEVE_ACTION_FROM_BUSINESS_RULE, String.class, Round.class);
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
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();
						action.addChild(new ActionReference(DELIVER));

						Value value = new Value().addValue("11");
						action.addChild(value);

						return action;
					}
				});
			}
		});

		agent = injector.getInstance(Agent.class);
		Method method = agent.getClass().getDeclaredMethod(RETRIEVE_ACTION_FROM_BUSINESS_RULE, String.class, Round.class);
		method.setAccessible(true);
		GameAgentAction action = (GameAgentAction) method.invoke(agent, null, null);
		assertTrue(action instanceof GameAgentDeliver);
	}

	@Test
	void testGenerateOrder() {
		Agent agent = new Agent("", facility);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();
						action.addChild(new ActionReference(ORDER));

						Value value = new Value().addValue("30");
						action.addChild(value);

						return action;
					}
				});
			}
		});
		injector.injectMembers(agent);
		agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 and 0 < 1 then order 30", "awesomerepresentationofthetreeasastring"));

		GameRoundAction result = agent.generateRoundActions();
		Map.Entry<Facility, Integer> entry = result.targetOrderMap.entrySet().iterator().next();

		assertEquals(30, (int) entry.getValue());
	}

	@Test
	void testGenerateDeliver() {
		Agent agent = new Agent("", facility);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();
						action.addChild(new ActionReference(DELIVER));

						Value value = new Value().addValue("5");
						action.addChild(value);

						return action;
					}
				});
			}
		});
		injector.injectMembers(agent);
		agent.gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 and 0 < 1 then order 30", "awesomerepresentationofthetreeasastring"));

		GameRoundAction result = agent.generateRoundActions();
		Map.Entry<Facility, Integer> entry = result.targetDeliverMap.entrySet().iterator().next();

		assertEquals(5, (int) entry.getValue());
	}

	@Test
	void testGenerateDeliverAndOrder() {
		Agent agent = new Agent("", facility);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();

						if (IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10.equals(businessRule)) {
							action.addChild(new ActionReference(DELIVER));
							Value value = new Value().addValue("5");
							action.addChild(value);
						} else if (IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20.equals(businessRule)) {
							action.addChild(new ActionReference(ORDER));
							Value value = new Value().addValue("5");
							action.addChild(value);
						} else if (IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30.equals(businessRule)) {
							return null;
						}

						return action;
					}
				});
			}
		});
		injector.injectMembers(agent);

		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30, EMPTY_BUSINESS_RULE));

		GameRoundAction result = agent.generateRoundActions();

		assertEquals(1, result.targetOrderMap.size());
		assertEquals(1, result.targetDeliverMap.size());
	}

	@Test
	void testGenerateMultipleOrders() {
		Agent agent = new Agent("", facility);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();

						if (IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30.equals(businessRule)) {
							return null;
						} else {
							action.addChild(new ActionReference(ORDER));
							Value value = new Value().addValue("5");
							action.addChild(value);
						}

						return action;
					}
				});
			}
		});
		injector.injectMembers(agent);

		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30, EMPTY_BUSINESS_RULE));

		GameRoundAction result = agent.generateRoundActions();

		assertEquals(1, result.targetOrderMap.size());
		assertEquals(0, result.targetDeliverMap.size());
	}

	@Test
	void testGenerateMultipleDelivers() {
		Agent agent = new Agent("", facility);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();

						if (IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30.equals(businessRule)) {
							return null;
						} else {
							action.addChild(new ActionReference(DELIVER));
							Value value = new Value().addValue("5");
							action.addChild(value);
						}

						return action;
					}
				});
			}
		});
		injector.injectMembers(agent);

		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30, EMPTY_BUSINESS_RULE));

		GameRoundAction result = agent.generateRoundActions();

		assertEquals(0, result.targetOrderMap.size());
		assertEquals(1, result.targetDeliverMap.size());
	}

	@Test
	void testGenerateMultipleDeliversAndOneOrder() {
		Agent agent = new Agent("", facility);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();

						if (IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30.equals(businessRule)) {
							action.addChild(new ActionReference(ORDER));
							Value value = new Value().addValue("5");
							action.addChild(value);
						} else {
							action.addChild(new ActionReference(DELIVER));
							Value value = new Value().addValue("5");
							action.addChild(value);
						}

						return action;
					}
				});
			}
		});
		injector.injectMembers(agent);

		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30, EMPTY_BUSINESS_RULE));

		GameRoundAction result = agent.generateRoundActions();

		assertEquals(1, result.targetOrderMap.size());
		assertEquals(1, result.targetDeliverMap.size());
	}

	@Test
	void testGenerateOneDeliverAndMultipleOrders() {
		Agent agent = new Agent("", facility);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRules.class).annotatedWith(Names.named(BUSINESS_RULES)).toInstance(new IBusinessRules() {
					@Override
					public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
						return Collections.emptyList();
					}

					@Override
					public Action evaluateBusinessRule(String businessRule, Round roundData) {
						Action action = new Action();

						if (IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10.equals(businessRule)) {
							action.addChild(new ActionReference(DELIVER));
							Value value = new Value().addValue("5");
							action.addChild(value);
						} else {
							action.addChild(new ActionReference(ORDER));
							Value value = new Value().addValue("5");
							action.addChild(value);
						}

						return action;
					}
				});
			}
		});
		injector.injectMembers(agent);

		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_10, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_20, EMPTY_BUSINESS_RULE));
		agent.gameBusinessRulesList.add(new GameBusinessRules(IF_INVENTORY_HIGHER_THAN_10_THEN_ORDER_30, EMPTY_BUSINESS_RULE));

		GameRoundAction result = agent.generateRoundActions();

		assertEquals(1, result.targetOrderMap.size());
		assertEquals(1, result.targetDeliverMap.size());
	}


	@Test
	void testResolveFacilityIdExpectsMockedFacility() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Agent agent = new Agent("", facility);
		Method method = agent.getClass().getDeclaredMethod(RESOLVE_FACILITY_ID, int.class);
		method.setAccessible(true);
		Facility resultFacility = (Facility) method.invoke(agent, 0);
		assertEquals(resultFacility, facility.getFacilitiesLinkedTo().get(0).getFacilityDeliver());
	}

	@Test
	void testResolveFacilityIdExpectsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Agent agent = new Agent("", facility);
		Method method = agent.getClass().getDeclaredMethod(RESOLVE_FACILITY_ID, int.class);
		method.setAccessible(true);
		Facility resultFacility = (Facility) method.invoke(agent, 1);
		assertNull(resultFacility);
	}
}
