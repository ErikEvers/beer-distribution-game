package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.*;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.evaluator.Counter;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

class EvaluatorTest {
    private Evaluator evaluator = new Evaluator();

    @Test
    void testcheckRoundIsComparedToInt_ExceptionThrownRoundWithGameObject_SideRight() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("round")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);

        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, inputBusinessRule);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Round can only be compared to a number", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testcheckRoundIsComparedToInt_ExceptionThrownRoundWithGameObject_SideLeft() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("round")));
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);


        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, inputBusinessRule);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Round can only be compared to a number", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testCheckSubTreeNotInt_NoExceptionThrown_RoundWithValue() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("20")));
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);


        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, inputBusinessRule, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(inputBusinessRule.getErrorMessage());
    }

    @Test
    void testCheckOnlyOneDefault_ExceptionThrown_TwoDefaults() {
        Default aDefault = new Default();
        Counter counter = new Counter();
        counter.addOne();
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);


        try {
            Whitebox.invokeMethod(evaluator, "checkOnlyOneDefault", aDefault,inputBusinessRule,counter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("There can only be one default statement", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testCheckOnlyOneDefault_noExceptionThrown_oneDefault() {
        Default aDefault = new Default();
        Counter counter = new Counter();
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);

        try {
            Whitebox.invokeMethod(evaluator, "checkOnlyOneDefault", aDefault,inputBusinessRule,counter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(inputBusinessRule.getErrorMessage());
    }

    @Test
    void testcheckLowHighOnlyUsedWithGameValueAndAboveBelow_ExceptionThrown_noGameValueWithBelowAboveRight(){
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("lowest")));
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);


        try {
            Whitebox.invokeMethod(evaluator, "checkLowHighOnlyComparedToGameValueAndAboveBelow", comparison,inputBusinessRule);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Lowest and highest can only be used with a game value combined with an above/below", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testcheckLowHighOnlyUsedWithGameValueAndAboveBelow_ExceptionThrown_noGameValueWithBelowAboveLeft(){
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("lowest")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);

        try {
            Whitebox.invokeMethod(evaluator, "checkLowHighOnlyComparedToGameValueAndAboveBelow", comparison,inputBusinessRule);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Lowest and highest can only be used with a game value combined with an above/below", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testcheckLowHighOnlyUsedInComparison_ExceptionThrown_notInComparison(){
        Value value = new Value().addValue("lowest");
        Action action = new Action();
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);


        try {
            Whitebox.invokeMethod(evaluator, "checkLowHighOnlyUsedInComparison", value,inputBusinessRule,action);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Lowest and highest can only be used in a comparison", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testcheckDeliverOnlyUsedWithBelowAbove_ExceptionThrown_notBelowAbove(){
        Counter counter = new Counter();
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("20")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        ActionReference actionReference = new ActionReference("deliver");
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);


        try {
            Whitebox.invokeMethod(evaluator, "checkDeliverOnlyUsedWithBelow", comparison,inputBusinessRule,counter);
            Whitebox.invokeMethod(evaluator, "checkDeliverOnlyUsedWithBelow", actionReference,inputBusinessRule,counter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Deliver can only be used with a businessrule that uses below", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testEvaluate_Called_evaluate() throws Exception {
        Map<UserInputBusinessRule,BusinessRule> list = new HashMap<>();

        Evaluator spy = PowerMockito.spy(new Evaluator());

        spy.evaluate(list);
        PowerMockito.verifyPrivate(spy).invoke("evaluate",any());
    }
}