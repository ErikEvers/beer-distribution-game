package org.han.ica.asd.c.agent;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
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
import org.powermock.reflect.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
		public void saveGameLog(BeerGame beerGame, boolean isStarted) {

		}

		@Override
		public void saveFacilityTurn(Round data) {
		}

		@Override
		public Round fetchFacilityTurn(int roundId) {
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
	private BeerGame beerGame;
	private Round round;
	private IPlayerGameLogic gameLogic;

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

		beerGame = new BeerGame();
		round = new Round();
		round.setRoundId(0);
		beerGame.getRounds().add(round);

		gameLogic = mock(IPlayerGameLogic.class);
		when(gameLogic.getBeerGame()).thenReturn(beerGame);
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
					public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
						return new ActionModel(ORDER, 30, lowerFacility.getFacilityId());
					}
				});
				bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
				bind(IPlayerGameLogic.class).toInstance(gameLogic);
			}
		});
		injector.injectMembers(agent);

		GameRoundAction result = agent.executeTurn();
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
					public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
						return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
					}
				});
				bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
				bind(IPlayerGameLogic.class).toInstance(gameLogic);
			}
		});
		injector.injectMembers(agent);
		GameRoundAction result = agent.executeTurn();
		Map.Entry<Facility, Integer> entry = result.targetDeliverMap.entrySet().iterator().next();

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
					public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
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
				bind(IPlayerGameLogic.class).toInstance(gameLogic);
			}
		});
		injector.injectMembers(agent);

		GameRoundAction result = agent.executeTurn();

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
					public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
						if (!BUSINESS_RULE_3.equals(businessRule)) {
							return new ActionModel(ORDER, 5, lowerFacility.getFacilityId());
						}
						return null;
					}
				});
				bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
				bind(IPlayerGameLogic.class).toInstance(gameLogic);
			}
		});
		injector.injectMembers(agent);

		GameRoundAction result = agent.executeTurn();

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
					public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
						if (!BUSINESS_RULE_3.equals(businessRule)) {
							return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
						}
						return null;
					}
				});
				bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
				bind(IPlayerGameLogic.class).toInstance(gameLogic);
			}
		});
		injector.injectMembers(agent);

		GameRoundAction result = agent.executeTurn();

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
					public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
						if (BUSINESS_RULE_3.equals(businessRule)) {
							return new ActionModel(ORDER, 5, lowerFacility.getFacilityId());
						}
						return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
					}
				});
				bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
				bind(IPlayerGameLogic.class).toInstance(gameLogic);
			}
		});
		injector.injectMembers(agent);

		GameRoundAction result = agent.executeTurn();

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
					public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
						if (BUSINESS_RULE_1.equals(businessRule)) {
							return new ActionModel(DELIVER, 5, upperFacility.getFacilityId());
						}
						return new ActionModel(ORDER, 5, lowerFacility.getFacilityId());
					}
				});
				bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistence);
				bind(IPlayerGameLogic.class).toInstance(gameLogic);
			}
		});
		injector.injectMembers(agent);

		GameRoundAction result = agent.executeTurn();

		assertEquals(1, result.targetOrderMap.size());
		assertEquals(1, result.targetDeliverMap.size());
	}

	@Test
	void testResolveLowerFacilityIdExpectsMockedFacility() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);
		Method method = agent.getClass().getDeclaredMethod(RESOLVE_LOWER_FACILITY_ID, int.class);
		method.setAccessible(true);
		Facility resultFacility = (Facility) method.invoke(agent, lowerFacility.getFacilityId());
		assertNotNull(resultFacility);
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
		assertNotNull(resultFacility);
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
					public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
						if (BUSINESS_RULE_1.equals(businessRule)) {
							return new ActionModel(DELIVER, 5, mainFacility.getFacilityId());
						}
						return new ActionModel(ORDER, 5, mainFacility.getFacilityId());
					}
				});
				bind(IPersistence.class).annotatedWith(Names.named(PERSISTENCE)).toInstance(persistenceMock);
				bind(IPlayerGameLogic.class).toInstance(gameLogic);
			}
		});
		injector.injectMembers(agent);

		agent.executeTurn();

		verify(persistenceMock, times(1)).logUsedBusinessRuleToCreateOrder(any(GameBusinessRulesInFacilityTurn.class));
		verify(persistenceMock, times(1)).logUsedBusinessRuleToCreateOrder(any(GameBusinessRulesInFacilityTurn.class));
	}

	@Test
	void testGetParticipant() {
        Agent agent = new Agent(configuration, "", mainFacility, gameBusinessRuleList);

        assertEquals(mainFacility, agent.getParticipant());
    }

	@Test
	void testFacilitySorting() {
		List<Facility> links = new ArrayList<>();
		links.add(new Facility());
		links.get(0).setFacilityId(4);
		links.add(new Facility());
		links.get(1).setFacilityId(5);
		links.add(new Facility());
		links.get(2).setFacilityId(1);

		Collections.sort(links);

		String exp = "145";
		StringBuilder res = new StringBuilder();

		for (Facility link : links) {
			res.append(link.getFacilityId());
		}

		assertEquals(exp,res.toString());
	}

	@Test
	void testresolveLowerFacilityId() {
		Configuration config = new Configuration();
		Map<Facility, List<Facility>> facilitiesLinkedTo = new HashMap<>();

		Facility myFacility = new Facility();
		myFacility.setFacilityId(1);
		Facility facilityWithId2 = new Facility();
		facilityWithId2.setFacilityId(2);
		Facility facilityWithId3 = new Facility();
		facilityWithId3.setFacilityId(3);
		Facility facilityWithId4 = new Facility();
		facilityWithId4.setFacilityId(4);
		Facility facilityWithId5 = new Facility();
		facilityWithId5.setFacilityId(5);

		List<Facility> facilitiesBelow = new ArrayList<>();
		facilitiesBelow.add(facilityWithId2);
		facilitiesBelow.add(facilityWithId3);

		List<Facility> listWithMyFacility = new ArrayList<>();
		listWithMyFacility.add(myFacility);

		facilitiesLinkedTo.put(myFacility, facilitiesBelow);
		facilitiesLinkedTo.put(facilityWithId4, listWithMyFacility);
		facilitiesLinkedTo.put(facilityWithId5, listWithMyFacility);
		config.setFacilitiesLinkedTo(facilitiesLinkedTo);

		Agent agent = new Agent(config, "", myFacility, gameBusinessRuleList);

		int exp = 2;
		Facility res = new Facility();

		try {
			res = Whitebox.invokeMethod(agent, "resolveLowerFacilityId", -1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertNotNull(res);
		assertEquals(exp,res.getFacilityId());
	}

	@Test
	void testresolveHigherFacilityId() {
		Configuration config = new Configuration();
		Map<Facility, List<Facility>> facilitiesLinkedTo = new HashMap<>();

		Facility myFacility = new Facility();
		myFacility.setFacilityId(1);
		Facility facilityWithId2 = new Facility();
		facilityWithId2.setFacilityId(2);
		Facility facilityWithId3 = new Facility();
		facilityWithId3.setFacilityId(3);
		Facility facilityWithId4 = new Facility();
		facilityWithId4.setFacilityId(4);
		Facility facilityWithId5 = new Facility();
		facilityWithId5.setFacilityId(5);

		List<Facility> facilitiesBelow = new ArrayList<>();
		facilitiesBelow.add(facilityWithId2);
		facilitiesBelow.add(facilityWithId3);

		List<Facility> listWithMyFacility = new ArrayList<>();
		listWithMyFacility.add(myFacility);

		facilitiesLinkedTo.put(myFacility, facilitiesBelow);
		facilitiesLinkedTo.put(facilityWithId4, listWithMyFacility);
		facilitiesLinkedTo.put(facilityWithId5, listWithMyFacility);
		config.setFacilitiesLinkedTo(facilitiesLinkedTo);

		Agent agent = new Agent(config, "", myFacility, gameBusinessRuleList);

		int exp = 4;
		Facility res = new Facility();

		try {
			res = Whitebox.invokeMethod(agent, "resolveHigherFacilityId", -1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertNotNull(res);
		assertEquals(exp,res.getFacilityId());
	}
}
