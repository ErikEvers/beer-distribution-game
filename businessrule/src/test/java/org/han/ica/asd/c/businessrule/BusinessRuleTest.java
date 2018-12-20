package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.engine.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.mocks.GameData;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

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


public class BusinessRuleTest {
    private BusinessRule businessRule = new BusinessRule();

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

    @Test
    void testBusinessrule_getReplacementValue_equals_10() {
        String businessRuleString = "BR(CS(C(CV(V(40% back orders))ComO(<)CV(Add(V(inventory)CalO(+)Mul(V(inventory)CalO(*)V(back orders)))))))";
        String expected = "BR(CS(C(CV(V(40% 10))ComO(<)CV(Add(V(10)CalO(+)Mul(V(10)CalO(*)V(10)))))))";

	    Round round = Mockito.mock(Round.class);
        Facility facility = Mockito.mock(Facility.class);
        FacilityType facilityType = Mockito.mock(FacilityType.class);
        facility.setFacilityType(facilityType);
        Map<Facility,Integer> map = new HashMap<>();
        map.put(facility,10);
        Map<Facility, Map<Facility,Integer> > mapInMap = new HashMap<>();
        mapInMap.put(facility,map);

        int facilityId = 11;
        facility.setFacilityId(facilityId);
        when(facility.getFacilityType()).thenReturn(facilityType);
	    when(facility.getFacilityId()).thenReturn(facilityId);

	    when(round.getTurnStock()).thenReturn(map);
	    when(round.getTurnBackOrder()).thenReturn(mapInMap);

        businessRule = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);


        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityId);
        String result = businessRule.encode();

        assertEquals(expected, result);
    }

    @Test
    void testBusinessRule_hasMultipleChildren_equals_true() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String businessRuleString = "BR(CS(C(CV(V(stock))ComO(<)CV(V(10))))A(AR(order)V(0)))";

        businessRule = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);

        Method hasMultipleChildren = businessRule.getClass().getDeclaredMethod("hasMultipleChildren",ASTNode.class);
        hasMultipleChildren.setAccessible(true);
        boolean statement = (Boolean) hasMultipleChildren.invoke(businessRule, (ASTNode) businessRule.getChildren().get(1));
        assertTrue(statement);
    }
}
