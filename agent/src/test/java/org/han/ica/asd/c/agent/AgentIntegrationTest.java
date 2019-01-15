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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AgentIntegrationTest {
    // Linear tests
    @Test
    void testLinearBusinessRulesOfRegionalWarehouse1UsesDefaultBusinessRuleToOrder10GoodsFromFactory() {
        Integer sourceFacilityId = 1;
        Integer targetFacilityId = 0;
        Facility sourceFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(sourceFacilityId);
        Facility targetFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(targetFacilityId);
        Integer expectedOrder = 10;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(>=)CV(V(40)))BoolO(&&)CS(C(CV(V(0))ComO(<)CV(V(3)))))A(AR(order)V(20)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(" + expectedOrder + ")))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityId, businessRulesList);

        Round round = new Round(
                2,
                Arrays.asList(
                        new FacilityTurn(targetFacility.getFacilityId(), 1, 10, 0, 1000, false),
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 10, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacilityId, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(targetFacility.getFacilityId(), sourceFacilityId, 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testLinearBusinessRulesOfAgentUsesBusinessRuleWithOrBooleanOperatorInventoryIsGreaterThan20AndLowerThan40Order15Goods() {
        Integer sourceFacilityIndex = 3;
        Integer targetFacilityIndex = 2;
        Facility sourceFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer inventory = 40;
        Integer expectedOrder = 15;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(==)CV(V(24))))A(AR(order)V(20)))"),
                new GameBusinessRules("business rule 2", "BR(CS(C(CV(V(inventory))ComO(>)CV(V(20)))BoolO(||)CS(C(CV(V(0))ComO(>)CV(V(3)))))A(AR(order)V(" + expectedOrder + ")CS(C(CV(V(inventory))ComO(<)CV(V(40))))))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(order)V(1)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, inventory, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacilityIndex, 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testLinearBusinessRulesOfWholesalerUsesBusinessRuleWithOrComparisonOperatorInventoryIsNotBetween20And40ToOrder2GoodsFromRetail() {
        Integer sourceFacilityIndex = 2;
        Integer targetFacilityIndex = 3;
        Facility sourceFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedDeliver = 2;
        Integer inventory = 60;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(>)CV(V(20)))BoolO(&&)CS(C(CV(V(inventory))ComO(<)CV(V(40)))))A(AR(deliver)V(20)))"),
                new GameBusinessRules("business rule 2", "BR(CS(C(CV(V(inventory))ComO(<=)CV(V(20)))BoolO(||)CS(C(CV(V(inventory))ComO(>=)CV(V(40)))))A(AR(deliver)V(" + expectedDeliver + ")))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(deliver)V(10)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, inventory, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), 0, 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualDeliver = gameRoundAction.targetDeliverMap.get(targetFacility);
        assertEquals(expectedDeliver, actualDeliver);
    }

    @Test
    void testLinearBusinessRulesOfRegionalWarehouseUsesBusinessRuleBacklogIsEqualsTo10ToOrder15GoodsFromFactory() {
        Integer sourceFacilityIndex = 1;
        Integer targetFacilityIndex = 0;
        Facility sourceFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 15;
        Integer backlog = 10;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(backlog))ComO(==)CV(V(" + backlog + "))))A(AR(order)V(" + expectedOrder + ")))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, backlog, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacility.getFacilityId(), 2, 0, 20)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testLinearBusinessRulesOfRegionalWarehouseUsesBusinessRuleOutgoingGoodsIsEqualsTo35ToOrder25GoodsFromFactory() {
        Integer sourceFacilityIndex = 1;
        Integer targetFacilityIndex = 0;
        Facility sourceFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 25;
        Integer outgoingGoods = 35;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(outgoing goods))ComO(==)CV(V(" + outgoingGoods + "))))A(AR(order)V(" + expectedOrder + ")))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacility.getFacilityId(), 2, 0, outgoingGoods)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testLinearBusinessRulesOfRegionalWarehouseUsesBusinessRuleBudgetNotEqualsTo999ToOrder25GoodsFromFactory() {
        Integer sourceFacilityIndex = 1;
        Integer targetFacilityIndex = 0;
        Facility sourceFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.LinearConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 25;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 1000;
        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(order)V(" + expectedOrder + ")))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.LinearConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    // Tree tests
    @Test
    void testTreeBusinessRulesOfRegionalWarehouse1UsesBusinessRuleOrderedEquals20ToOrder25GoodsFromFactory() {
        Integer sourceFacilityIndex = 1;
        Integer targetFacilityIndex = 0;
        Facility sourceFacility = Fixtures.TreeConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.TreeConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 25;
        Integer orderedAmount = 20;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(ordered))ComO(==)CV(V(" + orderedAmount + "))))A(AR(order)V(" + expectedOrder + ")P(factory 1)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.TreeConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, 0, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), orderedAmount)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testTreeBusinessRulesOfWholesaler3UsesBusinessRuleBackOrdersLowerThan20ToOrder25GoodsFromRetail5() {
        Integer sourceFacilityIndex = 5;
        Integer targetFacilityIndex = 11;
        Facility sourceFacility = Fixtures.TreeConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.TreeConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 25;
        Integer backOrderAmount = 20;
        Integer currentBackOrderAmount = 19;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(back orders))ComO(<)CV(V(" + backOrderAmount + "))))A(AR(deliver)V(" + expectedOrder + ")P(retail 5)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(deliver)V(5)P(retail 5)))")));
        IParticipant participant = Fixtures.TreeConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, currentBackOrderAmount, 0, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 0)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetDeliverMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    // Graph tests
    @Test
    void testGraphBusinessRulesOfRegionalWarehouse1UsesBusinessRuleBudgetGreaterThan500ToOrder25GoodsFromFactory1() {
        Integer sourceFacilityIndex = 3;
        Integer targetFacilityIndex = 0;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 25;
        Integer businessRuleComparedBudget = 500;
        Integer actualBudget = 1000;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(budget))ComO(>)CV(V(" + businessRuleComparedBudget + "))))A(AR(order)V(" + expectedOrder + ")P(factory 1)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGraphBusinessRulesOfRetail4UsesBusinessRuleStockLowerThan20ToOrder25GoodsFromWholesaler3() {
        Integer sourceFacilityIndex = 12;
        Integer targetFacilityIndex = 7;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 25;
        Integer stockLimit = 20;
        Integer stockCurrent = 10;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(stock))ComO(<)CV(V(" + stockLimit + "))))A(AR(order)V(" + expectedOrder + ")P(wholesaler 3)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)P(wholesaler 3)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, stockCurrent, 0, 30, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGraphBusinessRulesOfWholesaler1UsesBusinessRuleIncomingOrderGreaterThan20ToOrder25GoodsFromRegionalWarehouse2() {
        Integer sourceFacilityIndex = 5;
        Integer targetFacilityIndex = 4;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 25;
        Integer incomingOrderLimit = 20;
        Integer incomingOrderCurrent = 1000;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(incoming order))ComO(>)CV(V(" + incomingOrderLimit + "))))A(AR(order)V(" + expectedOrder + ")P(regional warehouse 2)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)P(factory 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, 30, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(targetFacility.getFacilityId(), sourceFacility.getFacilityId(), incomingOrderCurrent)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGraphBusinessRulesOfWholesaler3UsesBusinessRuleRoundGreaterThan8ToDeliver25GoodsToRetail1() {
        Integer sourceFacilityIndex = 7;
        Integer targetFacilityIndex = 9;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedDeliver = 25;
        Integer roundLimit = 8;
        Integer roundCurrent = 9;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(round))ComO(>)CV(V(" + roundLimit + "))))A(AR(deliver)V(" + expectedDeliver + ")P(retail 1)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(deliver)V(5)P(retail 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                roundCurrent,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), roundCurrent - 1, 20, 0, 30, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualDeliver = gameRoundAction.targetDeliverMap.get(targetFacility);
        assertEquals(expectedDeliver, actualDeliver);
    }

    @Test
    void testGraphBusinessRulesOfFactory2BusinessRuleBudgetNotEqualTo999FailsUsesDefaultBusinessRuleToDeliver5GoodsToRegionalWarehouse2() {
        Integer sourceFacilityIndex = 1;
        Integer targetFacilityIndex = 4;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedDeliver = 5;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 999;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(deliver)V(20)P(regional warehouse 2)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(deliver)V(" + expectedDeliver + ")P(regional warehouse 2)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualDeliver = gameRoundAction.targetDeliverMap.get(targetFacility);
        assertEquals(expectedDeliver, actualDeliver);
    }

    @Test
    void testGraphBusinessRulesOfRegionalWarehouse2DefaultBusinessRuleDeliver5GoodsToWholesaler4() {
        Integer sourceFacilityIndex = 4;
        Integer targetFacilityIndex = 8;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedDeliver = 5;
        Integer businessRuleComparedBudget = 999;
        Integer actualBudget = 999;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(budget))ComO(!=)CV(V(" + businessRuleComparedBudget + "))))A(AR(deliver)V(20)P(wholesaler 4)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(deliver)V(" + expectedDeliver + ")P(wholesaler 4)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, actualBudget, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualDeliver = gameRoundAction.targetDeliverMap.get(targetFacility);
        assertEquals(expectedDeliver, actualDeliver);
    }

    @Test
    void testGraphBusinessRulesOfFactory2BusinessRuleOutgoingGoodsEqualsTo20Deliver25GoodsToRegionalWarehouse2() {
        Integer sourceFacilityIndex = 1;
        Integer targetFacilityIndex = 4;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer outgoingGoods = 20;
        Integer expectedOrder = 25;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(outgoing goods))ComO(==)CV(V(" + outgoingGoods + "))))A(AR(deliver)V(" + expectedOrder + ")P(regional warehouse 2)))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(deliver)V(5)P(regional warehouse 2)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, 1000, false)),
                Collections.emptyList(),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 10, outgoingGoods)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetDeliverMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGraphFactory2DeliverToRetail1WontGetOrdersOrDeliversLogsExceptionInAgent() {
        Integer sourceFacilityIndex = 1;
        Integer targetFacilityIndex = 9;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        String expectedMessage = "Facility with ID: 10 is not found.";
        Level expectedLevel = Level.SEVERE;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(outgoing goods))ComO(==)CV(V(10))))A(AR(deliver)V(20)P(retail 1)))"),
                new GameBusinessRules("business rule 2", "BR(CS(C(CV(V(outgoing goods))ComO(==)CV(V(20))))A(AR(deliver)V(20)P(retail 1)))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(order)V(5)P(retail 1)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, 1000, false)),
                Collections.singletonList(
                        new FacilityTurnOrder(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 20)),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 10, 20)));

        Logger logger = Logger.getLogger(Agent.class.getName());
        TestExceptionLogHandler testExceptionLogHandler = new TestExceptionLogHandler();

        logger.addHandler(testExceptionLogHandler);

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        assertTrue(gameRoundAction.targetOrderMap.isEmpty());
        assertTrue(gameRoundAction.targetDeliverMap.isEmpty());
        assertEquals(expectedLevel, testExceptionLogHandler.level);
        assertEquals(expectedMessage, testExceptionLogHandler.message);
    }

    @Test
    void testGraphBusinessRulesOfFactory3BusinessRuleOutgoingGoodsToRegionalWarehouse1Equals20Produce60Goods() {
        Integer sourceFacilityIndex = 2;
        Integer targetFacilityIndex = 3;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer outgoingGoods = 20;
        Integer expectedOrder = 60;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(outgoing goods))ComO(==)CV(V(" + outgoingGoods + "))))A(AR(order)V(" + expectedOrder + ")))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, 20, 0, 1000, false)),
                Collections.emptyList(),
                Collections.singletonList(
                        new FacilityTurnDeliver(sourceFacility.getFacilityId(), targetFacility.getFacilityId(), 10, outgoingGoods)));

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(sourceFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGraphBusinessRulesOfFactory1BusinessRuleInventoryLowerThan20Produce25Goods() {
        Integer sourceFacilityIndex = 0;
        Integer expectedOrder = 25;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Integer inventoryIsLowerThan = 20;
        Integer inventory = 10;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(<)CV(V(" + inventoryIsLowerThan + "))))A(AR(order)V(" + expectedOrder + ")))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, inventory, 0, 0, false)),
                Collections.emptyList(),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(sourceFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGraphBusinessRulesOfRetail2BusinessRuleInventoryGreaterThan20Order25GoodsFromWholesaler1() {
        Integer sourceFacilityIndex = 10;
        Integer targetFacilityIndex = 5;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetFacilityIndex);
        Integer expectedOrder = 25;
        Integer inventoryLowerThan = 20;
        Integer inventory = 30;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(inventory))ComO(>)CV(V(" + inventoryLowerThan + "))))A(AR(order)V(" + expectedOrder + ")))"),
                new GameBusinessRules("business rule 2", "BR(D()A(AR(order)V(5)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 1, inventory, 0, 0, false)),
                Collections.emptyList(),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetFacility);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGraphBusinessRulesOfRegionalWarehouse2DefaultBusinessRulesForOrderingAndDeliveringToFactory2AndWholesaler4() {
        Integer sourceFacilityIndex = 4;
        Integer targetOrderFacilityIndex = 1;
        Integer targetDeliverFacilityIndex = 8;
        Facility sourceFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(sourceFacilityIndex);
        Facility targetOrderFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetOrderFacilityIndex);
        Facility targetDeliverFacility = Fixtures.GraphConfiguration.getInstance().facilityList.get(targetDeliverFacilityIndex);
        Integer expectedOrder = 5;
        Integer expectedDeliver = 20;

        List<GameBusinessRules> businessRulesList = Collections.unmodifiableList(Lists.newArrayList(
                new GameBusinessRules("business rule 1", "BR(CS(C(CV(V(incoming order))ComO(>)CV(V(20))))A(AR(order)V(10)P(factory 2)))"),
                new GameBusinessRules("business rule 2", "BR(CS(C(CV(V(outgoing goods))ComO(>)CV(V(20))))A(AR(deliver)V(15)P(wholesaler 4))"),
                new GameBusinessRules("business rule 3", "BR(D()A(AR(order)V(" + expectedOrder + ")P(factory 2)))"),
                new GameBusinessRules("business rule 4", "BR(D()A(AR(deliver)V(" + expectedDeliver + ")P(wholesaler 4)))")));
        IParticipant participant = Fixtures.GraphConfiguration.getInstance().createAgent(sourceFacilityIndex, businessRulesList);

        Round round = new Round(
                2,
                Collections.singletonList(
                        new FacilityTurn(sourceFacility.getFacilityId(), 0, 30, 0, 500, false)),
                Collections.emptyList(),
                Collections.emptyList());

        GameRoundAction gameRoundAction = participant.executeTurn(round);
        Integer actualOrder = gameRoundAction.targetOrderMap.get(targetOrderFacility);
        Integer actualDeliver = gameRoundAction.targetDeliverMap.get(targetDeliverFacility);
        assertEquals(expectedOrder, actualOrder);
        assertEquals(expectedDeliver, actualDeliver);
    }

    @Test
    void testGraphPlayMakeshiftGameOf10TurnsWith15AgentsContainingDefaultBusinessRules() {
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
                new FacilityTurn(14, 0, 30, 0, 1000, false),
                new FacilityTurn(15, 0, 30, 0, 1000, false));

        List<FacilityTurnOrder>     listOfFacilityTurnOrders    = Arrays.asList(
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
                new FacilityTurnOrder(14, 7, 5),
                new FacilityTurnOrder(0, -1, 5));

        List<FacilityTurnDeliver>   listOfFacilityTurnDelivers  = Arrays.asList(
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
                new FacilityTurnDeliver(14, -1, 0, 5),
                new FacilityTurnDeliver(0, 3, 0, 5));

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
}
