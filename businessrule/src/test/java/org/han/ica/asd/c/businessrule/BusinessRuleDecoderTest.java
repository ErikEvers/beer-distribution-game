package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.engine.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class BusinessRuleDecoderTest {

    @Test
    void parseBusinessRuleStringWithLesserThanComparisonToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(10))))A(AR(order)V(0)))";
        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithGreaterThanComparisonToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(>)CV(V(20))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithAndOperationComparisonToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(20)))BoolO(==)CS(C(CV(V(round))ComO(>)CV(V(3)))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithOrOperationComparisonToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(20)))BoolO(||)CS(C(CV(V(round))ComO(>)CV(V(3)))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithEqualsToAndIsNotEqualsToToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(==)CV(V(inventory)))BoolO(==)CS(C(CV(V(round))ComO(!=)CV(V(3)))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseDefaultOrderBusinessRuleString() {
        String businessRuleString = "BR(D()A(AR(order)V(10)))";
        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithAddOperationBusinessRuleIsEqual() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("5")))
                        .addChild(new ComparisonOperator("equal"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("5"))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new AddOperation()
                                .addChild(new Value().addValue("10"))
                                .addChild(new Value().addValue("1"))));

        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRule.encode());
        assertEquals(businessRule.encode(), businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithSubtractOperationBusinessRuleIsEqual() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("higher"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new SubtractOperation()
                                .addChild(new Value().addValue("inventory"))
                                .addChild(new Value().addValue("2"))));

        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRule.encode());
        assertEquals(businessRule.encode(), businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithMultiplyOperationBusinessRuleIsEqual() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("higher"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new MultiplyOperation()
                                .addChild(new Value().addValue("inventory"))
                                .addChild(new Value().addValue("2"))));

        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRule.encode());
        assertEquals(businessRule.encode(), businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithDivideOperationBusinessRuleIsEqual() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("higher"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new DivideOperation()
                                .addChild(new Value().addValue("inventory"))
                                .addChild(new Value().addValue("2"))));

        BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRule.encode());
        assertEquals(businessRule.encode(), businessRuleParsed.encode());
    }

    @Test
    void processOneIterationOfBusinessRuleScriptReturnsCorrectIdentifierAndNextIteration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("processIteration", Deque.class, String[].class);
        BusinessRuleDecoder businessRuleDecoder = new BusinessRuleDecoder();
        processIterationMethod.setAccessible(true);

        Deque<ASTNode> astNodeDeque = new LinkedList<>();
        astNodeDeque.push(new BusinessRule());
        String[] iteration = (String[]) processIterationMethod.invoke(businessRuleDecoder, astNodeDeque, new String[] {"A", "C(A)"});

        assertEquals("C", iteration[0]);
        assertEquals("A)", iteration[1]);
    }

    @Test
    void getNextIterationOfBusinessRuleScriptBySplittingOpeningParenthesis() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("nextIteration", String.class);
        BusinessRuleDecoder businessRuleDecoder = new BusinessRuleDecoder();
        processIterationMethod.setAccessible(true);

        String[] iteration = (String[]) processIterationMethod.invoke(businessRuleDecoder, "A(A)");

        assertEquals("A", iteration[0]);
        assertEquals("A)", iteration[1]);
    }

    @Test
    void getNextIterationOfBusinessRuleScriptBySplittingClosingParenthesis() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method nextIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("nextIteration", String.class);
        BusinessRuleDecoder businessRuleDecoder = new BusinessRuleDecoder();
        nextIterationMethod.setAccessible(true);

        String[] iteration = (String[]) nextIterationMethod.invoke(businessRuleDecoder, "A)A(A)");

        assertEquals("A", iteration[0]);
        assertEquals("A(A)", iteration[1]);
    }

    @Test
    void popOrPushFindsOpeningParenthesisAndReturnsTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("popOrPush", String.class);
        BusinessRuleDecoder businessRuleDecoder = new BusinessRuleDecoder();
        processIterationMethod.setAccessible(true);

        boolean iteration = (boolean) processIterationMethod.invoke(businessRuleDecoder, "A(A)");

        assertTrue(iteration);
    }

    @Test
    void popOrPushFindsClosingParenthesisAndReturnsFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("popOrPush", String.class);
        BusinessRuleDecoder businessRuleDecoder = new BusinessRuleDecoder();
        processIterationMethod.setAccessible(true);

        boolean iteration = (boolean) processIterationMethod.invoke(businessRuleDecoder, "A)A(A)");

        assertFalse(iteration);
    }

    @Test
    void popOrPushFindsNoParenthesisReturnsFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("popOrPush", String.class);
        BusinessRuleDecoder businessRuleDecoder = new BusinessRuleDecoder();
        processIterationMethod.setAccessible(true);

        boolean iteration = (boolean) processIterationMethod.invoke(businessRuleDecoder, "A");

        assertFalse(iteration);
    }
}
