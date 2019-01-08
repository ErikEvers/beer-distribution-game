package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.domain_objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.when;


class BusinessRuleTest {
    private BusinessRule businessRule = new BusinessRule();
    private Round round;
    private Facility facility;
    private FacilityTurn facilityTurn;
    private FacilityTurnDeliver facilityTurnDeliver;
    private FacilityTurnOrder facilityTurnOrder;
    private int facilityId = 11;

    @Test
    void testBusinessRule_getChilderen_True() {
        List<ASTNode> exp = new ArrayList<>();
        exp.add(null);
        exp.add(null);

        assertEquals(exp, businessRule.getChildren());
    }

    @Test
    void testBusinessRule_encode() {
        String res = businessRule.encode();
        String exp = "BR()";

        assertEquals(exp, res);
    }

    @Test
    void testBusinessRule_Equals_True() {
        boolean res = businessRule.equals(businessRule);
        assertTrue(res);
    }

    @Test
    void testBusinessRule_Equals_FalseNull() {
        boolean res = businessRule.equals(null);
        assertFalse(res);
    }

    @Test
    void testTransformChildWithNonOperation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ComparisonValue comparisonValue = mock(ComparisonValue.class);
        Value value = mock(Value.class);

        when(comparisonValue.getOperationValue()).thenReturn(value);

        BusinessRule businessRule = new BusinessRule();

        Method method = businessRule.getClass().getDeclaredMethod("transformChild", ASTNode.class);
        method.setAccessible(true);
        method.invoke(businessRule, comparisonValue);

        verifyZeroInteractions(value);

        InOrder inOrder = Mockito.inOrder(comparisonValue);

        inOrder.verify(comparisonValue).getOperationValue();
        inOrder.verify(comparisonValue).getChildren();
    }

    @Test
    void testTransformChildWithOperation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ComparisonValue comparisonValue = mock(ComparisonValue.class);
        Operation operation = mock(Operation.class);

        when(comparisonValue.getOperationValue()).thenReturn(operation);
        when(operation.resolveOperation()).thenReturn(new Value().addValue("20"));
        BusinessRule businessRule = new BusinessRule();

        Method method = businessRule.getClass().getDeclaredMethod("transformChild", ASTNode.class);
        method.setAccessible(true);
        method.invoke(businessRule, comparisonValue);

        InOrder inOrder = Mockito.inOrder(comparisonValue, operation);
        inOrder.verify(comparisonValue).getOperationValue();
        inOrder.verify(operation).resolveOperation();
        inOrder.verify(comparisonValue).getChildren();
    }

    @BeforeEach
    void setupTestReplaceBusinessRuleWithValue() {
        List<FacilityTurn> facilityTurns = new ArrayList<>();
        List<FacilityTurnOrder> facilityTurnOrders = new ArrayList<>();
        List<FacilityTurnDeliver> facilityTurnDelivers = new ArrayList<>();
        round = Mockito.mock(Round.class);
        facilityTurn = Mockito.mock(FacilityTurn.class);
        facilityTurnOrder = Mockito.mock(FacilityTurnOrder.class);
        facilityTurnDeliver = Mockito.mock(FacilityTurnDeliver.class);
        facilityTurns.add(facilityTurn);
        facilityTurnOrders.add(facilityTurnOrder);
        facilityTurnDelivers.add(facilityTurnDeliver);
        when(round.getFacilityTurnDelivers()).thenReturn(facilityTurnDelivers);
        when(round.getFacilityOrders()).thenReturn(facilityTurnOrders);
        when(round.getFacilityTurns()).thenReturn(facilityTurns);


        when(facilityTurn.getFacilityId()).thenReturn(facilityId);
        when(facilityTurn.getStock()).thenReturn(facilityId);
        when(facilityTurn.getRemainingBudget()).thenReturn(facilityId);
        when(facilityTurn.getBackorders()).thenReturn(facilityId);


        when(facilityTurnOrder.getFacilityId()).thenReturn(facilityId);
        when(facilityTurnOrder.getOrderAmount()).thenReturn(facilityId);


        when(facilityTurnDeliver.getFacilityId()).thenReturn(facilityId);
        when(facilityTurnDeliver.getDeliverAmount()).thenReturn(facilityId);

    }

    @Test
    void testBusinessrule_getReplacementValue_equals_10() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new ComparisonStatement()
                        .addChild(new ComparisonStatement()
                                .addChild(new Comparison()
                                        .addChild(new ComparisonValue().addChild(new Value().addValue("incoming order").addValue("factory")))
                                        .addChild(new ComparisonOperator("equal"))
                                        .addChild(new ComparisonValue().addChild(new Value().addValue("back orders").addValue("factory")))))
                        .addChild(new BooleanOperator())
                        .addChild(new ComparisonStatement()
                                .addChild(new Comparison()
                                        .addChild(new ComparisonValue().addChild(new Value().addValue("budget")))
                                        .addChild(new ComparisonOperator("not equal"))
                                        .addChild(new ComparisonValue().addChild(new Value().addValue("ordered"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new DivideOperation()
                                .addChild(new Value().addValue("40%").addValue("inventory"))
                                .addChild(new Value().addValue("20%").addValue("outgoing goods"))));

        String expected = "BR(CS(CS(C(CV(V(10))ComO(>=)CV(V(10))))BoolO(null)CS(C(CV(V(10))ComO(!=)CV(V(10)))))A(AR(order)Div(V(40% 10)CalO(/)V(20% 10))))";

        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityId);

        String result = businessRule.encode();
        assertEquals(expected, result);
    }

    @Test
    void testGameValue_ORDERED_contains_ordered() {
        assertTrue(GameValue.ORDERED.contains("ordered"));
    }


    @Test
    void testGame_ORDERED_getValue_Equals_ordered() {
        String[] actual = GameValue.ORDERED.getValue();
        String[] expected = {"ordered"};
        assertEquals(expected, actual);
    }

}
