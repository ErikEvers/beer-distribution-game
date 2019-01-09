package org.han.ica.asd.c.agent;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.businessrule.engine.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.engine.BusinessRuleFactory;
import org.han.ica.asd.c.businessrule.engine.IBusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.engine.IBusinessRuleFactory;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.INodeConverter;
import org.han.ica.asd.c.businessrule.parser.ast.NodeConverter;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AgentIntegrationTest {
    private IParticipant participant;
    private List<Facility> facilityList = Collections.unmodifiableList(Lists.newArrayList(
            new Facility(
                    new FacilityType("factory 1", 3, 3, 10, 30, 1000, 3, 40), 0),
            new Facility(
                    new FacilityType("regional warehouse 1", 3, 3, 10, 30, 1000, 3, 40), 1),
            new Facility(
                    new FacilityType("wholesale 1", 3, 3, 10, 30, 1000, 3, 40), 2),
            new Facility(
                    new FacilityType("retail 1", 3, 3, 10, 30, 1000, 3, 40), 3)));
    private Map<Facility, List<Facility>> facilitiesLinkedTo;
    private Injector participantInjector;
    private Configuration configuration;

    @BeforeEach
    void beforeEach() {
        facilitiesLinkedTo = new HashMap<>();
        facilitiesLinkedTo.put(facilityList.get(0), Collections.singletonList(facilityList.get(1)));
        facilitiesLinkedTo.put(facilityList.get(1), Arrays.asList(facilityList.get(0), facilityList.get(2)));
        facilitiesLinkedTo.put(facilityList.get(2), Arrays.asList(facilityList.get(1), facilityList.get(3)));
        facilitiesLinkedTo.put(facilityList.get(3), Collections.singletonList(facilityList.get(2)));

        configuration = new Configuration(
                10,
                1,
                1,
                1,
                1,
                1,
                1,
                true,
                false,
                facilityList,
                facilitiesLinkedTo);

        participantInjector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(INodeConverter.class).to(NodeConverter.class);
                bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).to(BusinessRuleHandler.class);
                bind(IBusinessRuleFactory.class).to(BusinessRuleFactory.class);
                bind(IBusinessRuleDecoder.class).to(BusinessRuleDecoder.class);
                bind(IPersistence.class).annotatedWith(Names.named("persistence")).toInstance(new IPersistence() {
                    @Override
                    public void saveFacilityTurn(Round data) {
                    }

                    @Override
                    public Round fetchFacilityTurn(int roundId, int facilityId) {
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
                });
                bind(IBusinessRuleStore.class).toInstance(new IBusinessRuleStore() {
                    @Override
                    public List<String> readInputBusinessRules(String agentName) {
                        return null;
                    }

                    @Override
                    public void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap) {
                    }

                    @Override
                    public List<List<String>> getAllFacilities() {
                        return Lists.newArrayList(
                                Lists.newArrayList("0"),
                                Lists.newArrayList("1"),
                                Lists.newArrayList("2"),
                                Lists.newArrayList("3")
                        );
                    }

                    @Override
                    public List<String> getAllProgrammedAgents() {
                        return null;
                    }

                    @Override
                    public void deleteProgrammedAgent(String agentName) {
                    }
                });
            }
        });
    }

    @Test
    void test() {
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(>=)CV(V(40)))BoolO(||)CS(C(CV(V(0))ComO(<)CV(V(3)))))A(AR(order)V(20)P(factory 1)))"),
                new GameBusinessRules("business rule 2", "BR(CS(C(CV(V(inventory))ComO(<)CV(V(40)))BoolO(||)CS(C(CV(V(0))ComO(>)CV(V(3)))))A(AR(order)V(20)P(factory 1)CS(C(CV(V(inventory))ComO(<)CV(V(10))))))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(order)V(10)))")));
        participant = new Agent(configuration, "RegionalWarehouseTestDummy", facilityList.get(1), businessRulesList);
        participantInjector.injectMembers(participant);

        Round round = new Round(
                2,
                Arrays.asList(
                        new FacilityTurn(0, 1, 40, 0, 1000, false),
                        new FacilityTurn(1, 1, 40, 0, 1000, false),
                        new FacilityTurn(2, 1, 40, 0, 1000, false),
                        new FacilityTurn(3, 1, 40, 0, 1000, false)),
                // Hoe order je als je factory bent.
                Arrays.asList(
                        new FacilityTurnOrder(1, 0, 20),
                        new FacilityTurnOrder(2, 1, 20),
                        new FacilityTurnOrder(3, 2, 20)),
                Arrays.asList(
                        new FacilityTurnDeliver(0, 1, 0, 20),
                        new FacilityTurnDeliver(1, 2, 0, 20),
                        new FacilityTurnDeliver(2, 3, 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        assertEquals(new Integer(20), gameRoundAction.targetOrderMap.get(facilityList.get(0)));
    }

    @Test
    void test2() {
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(>=)CV(V(40)))BoolO(||)CS(C(CV(V(0))ComO(<)CV(V(3)))))A(AR(order)V(20)P(factory 1)))"),
                new GameBusinessRules("business rule 2", "BR(CS(C(CV(V(inventory))ComO(<)CV(V(40)))BoolO(||)CS(C(CV(V(0))ComO(>)CV(V(3)))))A(AR(order)V(20)P(factory 1)CS(C(CV(V(inventory))ComO(<)CV(V(10))))))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(order)V(10)))")));
        participant = new Agent(configuration, "RegionalWarehouseTestDummy", facilityList.get(1), businessRulesList);
        participantInjector.injectMembers(participant);

        Round round = new Round(
                2,
                Arrays.asList(
                        new FacilityTurn(0, 1, 40, 0, 1000, false),
                        new FacilityTurn(1, 1, 40, 0, 1000, false),
                        new FacilityTurn(2, 1, 40, 0, 1000, false),
                        new FacilityTurn(3, 1, 40, 0, 1000, false)),
                // Hoe order je als je factory bent.
                Arrays.asList(
                        new FacilityTurnOrder(1, 0, 20),
                        new FacilityTurnOrder(2, 1, 20),
                        new FacilityTurnOrder(3, 2, 20)),
                Arrays.asList(
                        new FacilityTurnDeliver(0, 1, 0, 20),
                        new FacilityTurnDeliver(1, 2, 0, 20),
                        new FacilityTurnDeliver(2, 3, 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        assertEquals(new Integer(20), gameRoundAction.targetOrderMap.get(facilityList.get(0)));
    }
}
