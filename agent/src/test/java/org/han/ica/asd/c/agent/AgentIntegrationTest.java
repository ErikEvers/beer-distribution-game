package org.han.ica.asd.c.agent;

import com.google.common.collect.Lists;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.GameRoundAction;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.test.IntegrationTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Category(IntegrationTest.class)
class AgentIntegrationTest {
    // Linear tests
    @Test
    void testBusinessRulesOfAgentUsesDefaultBusinessRule() {
        Integer sourceFacilityId = 1;
        Integer targetFacilityId = 0;
        Integer actualOrder = 10;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(>=)CV(V(40)))BoolO(&&)CS(C(CV(V(0))ComO(<)CV(V(3)))))A(AR(order)V(20)P(factory 1)))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(order)V(" + actualOrder + ")P(factory 1)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Arrays.asList(
                        new FacilityTurn(targetFacilityId, 1, 10, 0, 1000, false),
                        new FacilityTurn(sourceFacilityId, 1, 10, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(targetFacilityId, sourceFacilityId, 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.LinearConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualOrder, result);
    }

    @Test
    void testBusinessRulesOfAgentUsesBusinessRuleWithOrBooleanOperatorInventoryIsGreaterOrEqualsTo40() {
        Integer sourceFacilityId = 1;
        int inventory = 40;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(>=)CV(V(40)))BoolO(||)CS(C(CV(V(0))ComO(<)CV(V(3)))))A(AR(order)V(20)P(factory 1)))"),
                new GameBusinessRules("business rule 2", "BR(CS(C(CV(V(inventory))ComO(<)CV(V(40)))BoolO(||)CS(C(CV(V(0))ComO(>)CV(V(3)))))A(AR(order)V(20)P(factory 1)CS(C(CV(V(inventory))ComO(<)CV(V(10))))))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(order)V(10)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, inventory, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, 0, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacilityId, 2, 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.LinearConfiguration.getInstance().facilityList.get(0));
        assertEquals(new Integer(20), result);
    }

    @Test
    void testBusinessRulesOfAgentUsesBusinessRuleWithOrComparisonOperatorInventoryIsNotBetween20And40() {
        Integer sourceFacilityId = 1;
        Integer actualOrder = 1;
        Integer inventory = 60;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(>)CV(V(20)))BoolO(&&)CS(C(CV(V(inventory))ComO(<)CV(V(40)))))A(AR(order)V(20)P(factory 1)))"),
                new GameBusinessRules("business rule 2", "BR(CS(C(CV(V(inventory))ComO(<=)CV(V(20)))BoolO(||)CS(C(CV(V(inventory))ComO(>=)CV(V(40)))))A(AR(order)V(" + actualOrder + ")P(factory 1)))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(order)V(10)P(factory 1)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, inventory, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, 0, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacilityId, 2, 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.LinearConfiguration.getInstance().facilityList.get(0));
        assertEquals(actualOrder, result);
    }

    @Test
    void testBusinessRulesOfAgentUsesBusinessRuleBacklogIsEqualsTo10() {
        Integer sourceFacilityId = 1;
        Integer actualOrder = 15;
        Integer backlog = 10;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(backlog))ComO(==)CV(V(" + backlog + "))))A(AR(order)V(" + actualOrder + ")P(factory 1)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, backlog, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, 0, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacilityId, 2, 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.LinearConfiguration.getInstance().facilityList.get(0));
        assertEquals(actualOrder, result);
    }

    @Test
    void testBusinessRulesOfAgentUsesBusinessRuleOutgoingGoodsIsEqualsTo35() {
        Integer sourceFacilityId = 1;
        Integer actualOrder = 25;
        Integer outgoingGoods = 35;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(outgoing goods))ComO(==)CV(V(" + outgoingGoods + "))))A(AR(order)V(" + actualOrder + ")P(factory 1)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(1, 0, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(1, 2, 0, outgoingGoods)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.LinearConfiguration.getInstance().facilityList.get(0));
        assertEquals(actualOrder, result);
    }

    @Test
    void testBusinessRulesOfAgentUsesBusinessRuleNotEqualsComparisonOperator() {
        Integer sourceFacilityId = 1;
        Integer targetFacilityId = 0;
        Integer actualOrder = 25;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 1000;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(order)V(" + actualOrder + ")P(factory 1)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.LinearConfiguration.getInstance().facilityList.get(0));
        assertEquals(actualOrder, result);
    }

    // Tree tests
    @Test
    void testTreeRegionalWarehouse1Order25GoodsFromFacilityFactory1WhenRegionalWarehouse1Ordered20PreviousRound() {
        Integer sourceFacilityId = 1;
        Integer targetFacilityId = 0;
        Integer actualOrder = 25;
        Integer orderedAmount = 20;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(ordered))ComO(==)CV(V(" + orderedAmount + "))))A(AR(order)V(" + actualOrder + ")P(factory 1)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.TreeConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, 0, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, orderedAmount)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.TreeConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualOrder, result);
    }

    @Test
    void testTreeWholesaler3Deliver20GoodsToRetail5WhenBackOrdersAreLowerThan20() {
        Integer sourceFacilityId = 5;
        Integer targetFacilityId = 11;
        Integer actualOrder = 25;
        Integer backOrderAmount = 20;
        Integer currentBackOrderAmount = 19;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(back orders))ComO(<)CV(V(" + backOrderAmount + "))))A(AR(order)V(" + actualOrder + ")P(retail 5)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(retail 5)))")));
        IParticipant participant = Fixtures.TreeConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, currentBackOrderAmount, 0, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 0)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.TreeConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualOrder, result);
    }

    // Graph tests
    @Test
    void testGraphRegionalWarehouse1OrderFromFacilityFactory1() {
        Integer sourceFacilityId = 3;
        Integer targetFacilityId = 0;
        Integer actualOrder = 25;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 1000;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(order)V(" + actualOrder + ")P(factory 1)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualOrder, result);
    }

    @Test
    void testGraphRetail14OrderFromWholesaler3() {
        Integer sourceFacilityId = 12;
        Integer targetFacilityId = 7;
        Integer actualOrder = 25;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 1000;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(order)V(" + actualOrder + ")P(wholesaler 3)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualOrder, result);
    }

    @Test
    void testGraphWholesaler1OrderFromRegionalWarehouse2() {
        Integer sourceFacilityId = 5;
        Integer targetFacilityId = 4;
        Integer actualOrder = 25;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 1000;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(order)V(" + actualOrder + ")P(regional warehouse 2)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualOrder, result);
    }

    @Test
    void testGraphRegionalWarehouse1DeliverToWholesaler3() {
        Integer sourceFacilityId = 7;
        Integer targetFacilityId = 9;
        Integer actualDeliver = 25;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 1000;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(deliver)V(" + actualDeliver + ")P(retail 1)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(deliver)V(5)P(retail 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetDeliverMap.get(Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualDeliver, result);
    }

    @Test
    void testGraphFactory2DeliverToRegionalWarehouse2() {
        Integer sourceFacilityId = 4;
        Integer targetFacilityId = 1;
        Integer actualDeliver = 5;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 999;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(deliver)V(20)P(factory 2)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(deliver)V(" + actualDeliver + ")P(factory 2)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetDeliverMap.get(Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualDeliver, result);
    }

    @Test
    void testGraphRegionalWarehouse2DeliverToWholesaler4() {
        Integer sourceFacilityId = 4;
        Integer targetFacilityId = 8;
        Integer actualDeliver = 5;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 999;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(deliver)V(20)P(wholesaler 4)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(deliver)V(" + actualDeliver + ")P(wholesaler 4)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, targetFacilityId, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetDeliverMap.get(Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualDeliver, result);
    }

    @Test
    void testGraphFactory2DeliverToRegionalWarehouse1() {
        Integer sourceFacilityId = 1;
        Integer targetFacilityId = 3;
        Integer outgoingGoods = 20;
        Integer actualDeliver = 25;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(inventory))ComO(==)CV(V(" + outgoingGoods + "))))A(AR(order)V(" + actualDeliver + ")P(regional warehouse 1)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(regional warehouse 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, 1000, false)),
                Collections.emptyList(),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacilityId, targetFacilityId, 10, outgoingGoods)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer resultDeliver = gameRoundAction.targetOrderMap.get(Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityId));
        assertEquals(actualDeliver, resultDeliver);
    }

    @Test
    void testGraphFactory2DeliverToRetail1WontGetOrdersOrDelivers() {
        Integer sourceFacilityId = 1;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(outgoing goods))ComO(==)CV(V(20))))A(AR(deliver)V(20)P(retail 1)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)P(retail 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, 0, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacilityId, 0, 10, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        assertTrue(gameRoundAction.targetOrderMap.isEmpty());
        assertTrue(gameRoundAction.targetDeliverMap.isEmpty());
    }

    @Test
    void testGraphFactory2Create60Goods() {
        Integer sourceFacilityId = 2;
        Integer outgoingGoods = 20;
        Integer actualOrder = 60;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(outgoing goods))ComO(==)CV(V(" + outgoingGoods + "))))A(AR(order)V(" + actualOrder + ")))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, 20, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, 0, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacilityId, 0, 10, outgoingGoods)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(null);
        assertEquals(actualOrder, result);
    }

    @Test
    void testGraphFactoryOrder25Goods() {
        Integer sourceFacilityId = 0;
        Integer actualOrder = 25;
        Integer inventoryIsLowerThan = 20;
        Integer inventory = 10;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(inventory))ComO(<)CV(V(" + inventoryIsLowerThan + "))))A(AR(order)V(" + actualOrder + ")))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, inventory, 0, 0, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, 0, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer result = gameRoundAction.targetOrderMap.get(null);
        assertEquals(actualOrder, result);
    }

    @Test
    void testGraphRetail2Order25Goods() {
        Integer sourceFacilityId = 9;
        Integer actualOrder = 25;
        Integer inventoryLowerThan = 20;
        Integer inventory = 10;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 3", "BR(CS(C(CV(V(inventory))ComO(<)CV(V(" + inventoryLowerThan + "))))A(AR(order)V(" + actualOrder + ")))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacilityId, 1, inventory, 0, 0, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacilityId, 0, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        assertTrue(gameRoundAction.targetOrderMap.isEmpty());
        assertTrue(gameRoundAction.targetDeliverMap.isEmpty());
    }

    @Test
    void testGraphPlayMakeshiftGameOf10Turns() {
        //Order business rules
        GameBusinessRules gameBusinessRulesOrder5GoodsFromFactory1              = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(factory 1)))");
        GameBusinessRules gameBusinessRulesOrder5GoodsFromFactory2              = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(factory 2)))");
        GameBusinessRules gameBusinessRulesOrder5GoodsFromFactory3              = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(factory 3)))");

        GameBusinessRules gameBusinessRulesOrder5GoodsFromRegionalWarehouse1    = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(regional warehouse 1)))");
        GameBusinessRules gameBusinessRulesOrder5GoodsFromRegionalWarehouse2    = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(regional warehouse 2)))");

        GameBusinessRules gameBusinessRulesOrder5GoodsFromWholesaler1           = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(wholesaler 1)))");
        GameBusinessRules gameBusinessRulesOrder5GoodsFromWholesaler2           = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(wholesaler 2)))");
        GameBusinessRules gameBusinessRulesOrder5GoodsFromWholesaler3           = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(wholesaler 3)))");
        GameBusinessRules gameBusinessRulesOrder5GoodsFromWholesaler4           = new GameBusinessRules("default rule 1", "BR(D()A(AR(order)V(5)P(wholesaler 4)))");

        //Deliver business rules
        GameBusinessRules gameBusinessRulesDeliver5GoodsToRegionalWarehouse1    = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(regional warehouse 1)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToRegionalWarehouse2    = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(regional warehouse 2)))");

        GameBusinessRules gameBusinessRulesDeliver5GoodsToWholesaler1           = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(wholesaler 1)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToWholesaler2           = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(wholesaler 2)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToWholesaler3           = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(wholesaler 3)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToWholesaler4           = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(wholesaler 4)))");

        GameBusinessRules gameBusinessRulesDeliver5GoodsToRetail1               = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(retail 1)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToRetail2               = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(retail 2)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToRetail3               = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(retail 3)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToRetail4               = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(retail 4)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToRetail5               = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(retail 5)))");
        GameBusinessRules gameBusinessRulesDeliver5GoodsToRetail6               = new GameBusinessRules("default rule 1", "BR(D()A(AR(deliver)V(5)P(retail 6)))");

        List<IParticipant> listOfParticipants = new ArrayList<>();

        // Factory participants
        listOfParticipants.add(0, Fixtures.GraphConfiguration.getInstance().createAgent(0, Collections.singletonList(
                gameBusinessRulesDeliver5GoodsToRegionalWarehouse1
        )));
        listOfParticipants.add(1, Fixtures.GraphConfiguration.getInstance().createAgent(1, Arrays.asList(
                gameBusinessRulesDeliver5GoodsToRegionalWarehouse1,
                gameBusinessRulesDeliver5GoodsToRegionalWarehouse2
        )));
        listOfParticipants.add(2, Fixtures.GraphConfiguration.getInstance().createAgent(2, Collections.singletonList(
                gameBusinessRulesDeliver5GoodsToRegionalWarehouse1
        )));

        // Regional warehouse participants
        listOfParticipants.add(3, Fixtures.GraphConfiguration.getInstance().createAgent(3, Arrays.asList(
                gameBusinessRulesOrder5GoodsFromFactory1,
                gameBusinessRulesOrder5GoodsFromFactory2,
                gameBusinessRulesOrder5GoodsFromFactory3,
                gameBusinessRulesDeliver5GoodsToWholesaler1,
                gameBusinessRulesDeliver5GoodsToWholesaler2,
                gameBusinessRulesDeliver5GoodsToWholesaler3
        )));
        listOfParticipants.add(4, Fixtures.GraphConfiguration.getInstance().createAgent(4, Arrays.asList(
                gameBusinessRulesOrder5GoodsFromFactory2,
                gameBusinessRulesDeliver5GoodsToWholesaler1,
                gameBusinessRulesDeliver5GoodsToWholesaler4
        )));

        // Wholesaler participants
        listOfParticipants.add(5, Fixtures.GraphConfiguration.getInstance().createAgent(5, Arrays.asList(
                gameBusinessRulesOrder5GoodsFromRegionalWarehouse1,
                gameBusinessRulesOrder5GoodsFromRegionalWarehouse2,
                gameBusinessRulesDeliver5GoodsToRetail1,
                gameBusinessRulesDeliver5GoodsToRetail2
        )));
        listOfParticipants.add(6, Fixtures.GraphConfiguration.getInstance().createAgent(6, Arrays.asList(
                gameBusinessRulesDeliver5GoodsToRegionalWarehouse1,
                gameBusinessRulesDeliver5GoodsToRetail1,
                gameBusinessRulesDeliver5GoodsToRetail3,
                gameBusinessRulesDeliver5GoodsToRetail4
        )));
        listOfParticipants.add(7, Fixtures.GraphConfiguration.getInstance().createAgent(7, Arrays.asList(
                gameBusinessRulesDeliver5GoodsToRegionalWarehouse1,
                gameBusinessRulesDeliver5GoodsToRetail1,
                gameBusinessRulesDeliver5GoodsToRetail3,
                gameBusinessRulesDeliver5GoodsToRetail4,
                gameBusinessRulesDeliver5GoodsToRetail6
        )));
        listOfParticipants.add(8, Fixtures.GraphConfiguration.getInstance().createAgent(8, Arrays.asList(
                gameBusinessRulesDeliver5GoodsToRegionalWarehouse2,
                gameBusinessRulesDeliver5GoodsToRetail5
        )));

        // Retail participants
        listOfParticipants.add(9, Fixtures.GraphConfiguration.getInstance().createAgent(9, Arrays.asList(
                gameBusinessRulesOrder5GoodsFromWholesaler1,
                gameBusinessRulesOrder5GoodsFromWholesaler2,
                gameBusinessRulesOrder5GoodsFromWholesaler3
        )));
        listOfParticipants.add(10, Fixtures.GraphConfiguration.getInstance().createAgent(10, Collections.singletonList(
                gameBusinessRulesOrder5GoodsFromWholesaler1
        )));
        listOfParticipants.add(11, Fixtures.GraphConfiguration.getInstance().createAgent(11, Arrays.asList(
                gameBusinessRulesOrder5GoodsFromWholesaler2,
                gameBusinessRulesOrder5GoodsFromWholesaler3
        )));
        listOfParticipants.add(12, Fixtures.GraphConfiguration.getInstance().createAgent(12, Arrays.asList(
                gameBusinessRulesOrder5GoodsFromWholesaler2,
                gameBusinessRulesOrder5GoodsFromWholesaler3
        )));
        listOfParticipants.add(13, Fixtures.GraphConfiguration.getInstance().createAgent(13, Collections.singletonList(
                gameBusinessRulesOrder5GoodsFromWholesaler4
        )));
        listOfParticipants.add(14, Fixtures.GraphConfiguration.getInstance().createAgent(14, Collections.singletonList(
                gameBusinessRulesOrder5GoodsFromWholesaler3
        )));

        // Setup turns/ orders and delivers
        List<FacilityTurn>          listOfFacilityTurns         = Arrays.asList(
                new FacilityTurn(0, 0, 30, 0, 1000, false),
                new FacilityTurn(1, 0, 30, 0, 1000, false),
                new FacilityTurn(2, 0, 30, 0, 1000, false),
                new FacilityTurn(3, 0, 30, 0, 1000, false),
                new FacilityTurn(4, 0, 30, 0, 1000, false),
                new FacilityTurn(5, 0, 30, 0, 1000, false),
                new FacilityTurn(6, 0, 30, 0, 1000, false),
                new FacilityTurn(7, 0, 30, 0, 1000, false),
                new FacilityTurn(8, 0, 30, 0, 1000, false),
                new FacilityTurn(9, 0, 30, 0, 1000, false),
                new FacilityTurn(10, 0, 30, 0, 1000, false),
                new FacilityTurn(11, 0, 30, 0, 1000, false),
                new FacilityTurn(12, 0, 30, 0, 1000, false),
                new FacilityTurn(13, 0, 30, 0, 1000, false),
                new FacilityTurn(14, 0, 30, 0, 1000, false)
        );
        List<FacilityTurnOrder>     listOfFacilityTurnOrders    = Arrays.asList(
                new FacilityTurnOrder(0, -1, 5),
                new FacilityTurnOrder(1, -1, 5),
                new FacilityTurnOrder(2, -1, 5),
                new FacilityTurnOrder(3, 0, 5),
                new FacilityTurnOrder(4, 2, 5),
                new FacilityTurnOrder(5, 3, 5),
                new FacilityTurnOrder(6, 3, 5),
                new FacilityTurnOrder(7, 3, 5),
                new FacilityTurnOrder(8, 4, 5),
                new FacilityTurnOrder(9, 5, 5),
                new FacilityTurnOrder(10, 5, 5),
                new FacilityTurnOrder(11, 6, 5),
                new FacilityTurnOrder(12, 6, 5),
                new FacilityTurnOrder(13, 8, 5),
                new FacilityTurnOrder(14, 7, 5)
        );
        List<FacilityTurnDeliver>   listOfFacilityTurnDelivers  = Arrays.asList(
                new FacilityTurnDeliver(0, 3, 0, 5),
                new FacilityTurnDeliver(1, 4, 0, 5),
                new FacilityTurnDeliver(2, 3, 0, 5),
                new FacilityTurnDeliver(3, 6, 0, 5),
                new FacilityTurnDeliver(4, 8, 0, 5),
                new FacilityTurnDeliver(5, 10, 0, 5),
                new FacilityTurnDeliver(6, 9, 0, 5),
                new FacilityTurnDeliver(7, 14, 0, 5),
                new FacilityTurnDeliver(8, 13, 0, 5),
                new FacilityTurnDeliver(9, -1, 0, 5),
                new FacilityTurnDeliver(10, -1, 0, 5),
                new FacilityTurnDeliver(11, -1, 0, 5),
                new FacilityTurnDeliver(12, -1, 0, 5),
                new FacilityTurnDeliver(13, -1, 0, 5),
                new FacilityTurnDeliver(14, -1, 0, 5)
        );

        // Run makeshift engine to test get results from business rules for 10 rounds
        int roundCount = 10;
        Round round;

        for(int roundId = 0; roundId < roundCount; ++roundId) {
            round = new Round(roundId,
                    listOfFacilityTurns,
                    listOfFacilityTurnOrders,
                    listOfFacilityTurnDelivers);

            List<FacilityTurn>          listOfTemporaryFacilityTurns            = new ArrayList<>(listOfFacilityTurns);

            listOfFacilityTurns         = new ArrayList<>();
            listOfFacilityTurnOrders    = new ArrayList<>();
            listOfFacilityTurnDelivers  = new ArrayList<>();

            for (int participantId = 0, participantLength = listOfParticipants.size(); participantId < participantLength; ++participantId) {
                IParticipant participant = listOfParticipants.get(participantId);
                int facilityId = participant.getParticipant().getFacilityId();

                GameRoundAction participantGameRoundAction = participant.executeTurn(round);

                // New turn values
                FacilityTurn temporaryFacilityTurn =            listOfTemporaryFacilityTurns.get(participantId);
                int stock = temporaryFacilityTurn.getStock();
                int budget = temporaryFacilityTurn.getRemainingBudget();

                if (!participantGameRoundAction.targetOrderMap.isEmpty()) {
                    final int orderAmount = participantGameRoundAction.targetOrderMap.values().iterator().next();

                    listOfFacilityTurnOrders.add(
                            new FacilityTurnOrder(
                                    facilityId,
                                    participantGameRoundAction.targetOrderMap.keySet().iterator().next().getFacilityId(),
                                    orderAmount));

                    stock += orderAmount;
                    budget -= participant.getParticipant().getFacilityType().getValueIncomingGoods() * orderAmount;
                }

                if (!participantGameRoundAction.targetDeliverMap.isEmpty()) {
                    final int deliverAmount = participantGameRoundAction.targetDeliverMap.values().iterator().next();

                    listOfFacilityTurnDelivers.add(new FacilityTurnDeliver(
                            facilityId,
                            participantGameRoundAction.targetDeliverMap.keySet().iterator().next().getFacilityId(),
                            0,
                            deliverAmount));

                    stock -= deliverAmount;
                    budget += participant.getParticipant().getFacilityType().getValueOutgoingGoods() * deliverAmount;
                }

                listOfFacilityTurns.add(
                        new FacilityTurn(
                                facilityId,
                                roundId,
                                stock,
                                temporaryFacilityTurn.getBackorders(),
                                budget,
                                temporaryFacilityTurn.isBankrupt()));
            }
        }

        assertEquals(-20, listOfFacilityTurns.get(0).getStock());
        assertEquals(-20, listOfFacilityTurns.get(1).getStock());
        assertEquals(-20, listOfFacilityTurns.get(2).getStock());
        assertEquals(30, listOfFacilityTurns.get(3).getStock());
        assertEquals(30, listOfFacilityTurns.get(4).getStock());
        assertEquals(30, listOfFacilityTurns.get(5).getStock());
        assertEquals(-20, listOfFacilityTurns.get(6).getStock());
        assertEquals(-20, listOfFacilityTurns.get(7).getStock());
        assertEquals(-20, listOfFacilityTurns.get(8).getStock());
        assertEquals(80, listOfFacilityTurns.get(9).getStock());
        assertEquals(80, listOfFacilityTurns.get(10).getStock());
        assertEquals(80, listOfFacilityTurns.get(11).getStock());
        assertEquals(80, listOfFacilityTurns.get(12).getStock());
        assertEquals(80, listOfFacilityTurns.get(13).getStock());
        assertEquals(80, listOfFacilityTurns.get(14).getStock());
    }

    @Test
    void test() {
        Map<Integer, Integer> testMap = new HashMap<>();
        testMap.put(0, 4);
        testMap.put(1, 5);
        testMap.put(2, 6);
        testMap.put(3, 7);

        List<Integer> list = testMap.entrySet().stream()
                .filter(entry -> entry.getValue() == 5)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(
                        Collectors.toList());
        System.out.println(list);
    }
}
