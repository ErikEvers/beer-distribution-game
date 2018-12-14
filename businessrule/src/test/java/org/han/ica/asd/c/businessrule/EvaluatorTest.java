package org.han.ica.asd.c.businessrule;

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
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

class EvaluatorTest {
    private Evaluator evaluator = new Evaluator();

    @Test
    void testcheckRoundIsComparedToInt_ExceptionThrownRoundWithGameObject_SideRight() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("round")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));

        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, 1);
            fail("Expected exception has not occured.");
        } catch (Exception e) {
        }
    }

    @Test
    void testcheckRoundIsComparedToInt_ExceptionThrownRoundWithGameObject_SideLeft() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("round")));

        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, 1);
            fail("Expected exception has not occured.");
        } catch (Exception e) {
        }
    }

    @Test
    void testCheckSubTreeNotInt_NoExceptionThrown_RoundWithValue() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("20")));

        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, 1, 2);
        } catch (Exception e) {
            fail("Exception was thrown, no exception needed.");
        }
    }

    @Test
    void testCheckOnlyOneDefault_ExceptionThrown_TwoDefaults() {
        Default aDefault = new Default();
        Counter counter = new Counter();
        counter.addOne();

        try {
            Whitebox.invokeMethod(evaluator, "checkOnlyOneDefault", aDefault,1,counter);
            fail("Expected exception has not occured.");
        } catch (Exception e) {
        }
    }

    @Test
    void testCheckOnlyOneDefault_ExceptionThrown_oneDefault() {
        Default aDefault = new Default();
        Counter counter = new Counter();

        try {
            Whitebox.invokeMethod(evaluator, "checkOnlyOneDefault", aDefault,1,counter);
        } catch (Exception e) {
            fail("Exception was thrown, no exception needed.");
        }
    }

    @Test
    void testcheckLowHighOnlyUsedWithGameValueAndAboveBelow_ExceptionThrown_noGameValueWithBelowAboveRight(){
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("lowest")));

        try {
            Whitebox.invokeMethod(evaluator, "checkLowHighOnlyComparedToGameValueAndAboveBelow", comparison,1);
            fail("Expected exception has not occured.");
        } catch (Exception e) {
        }
    }

    @Test
    void testcheckLowHighOnlyUsedWithGameValueAndAboveBelow_ExceptionThrown_noGameValueWithBelowAboveLeft(){
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("lowest")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));

        try {
            Whitebox.invokeMethod(evaluator, "checkLowHighOnlyComparedToGameValueAndAboveBelow", comparison,1);
            fail("Expected exception has not occured.");
        } catch (Exception e) {
        }
    }

    @Test
    void testcheckLowHighOnlyUsedInComparison_ExceptionThrown_notInComparison(){
        Value value = new Value().addValue("lowest");
        Action action = new Action();

        try {
            Whitebox.invokeMethod(evaluator, "checkLowHighOnlyUsedInComparison", value,1,action);
            fail("Expected exception has not occured.");
        } catch (Exception e) {
        }
    }

    @Test
    void testcheckDeliverOnlyUsedWithBelowAbove_ExceptionThrown_notBelowAbove(){
        Counter counter = new Counter();
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("20")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        ActionReference actionReference = new ActionReference("deliver");

        try {
            Whitebox.invokeMethod(evaluator, "checkDeliverOnlyUsedWithBelowAbove", comparison,1,counter);
            Whitebox.invokeMethod(evaluator, "checkDeliverOnlyUsedWithBelowAbove", actionReference,1,counter);
            fail("Expected exception has not occured.");
        } catch (Exception e) {
        }
    }

    //checkDeliverOnlyUsedWithBelowAbove

    @Test
    void testEvaluate_Called_evaluate() throws Exception {
        List<BusinessRule> list = new ArrayList<>();
        list.add(Fixtures.RuleWithRound());

        Evaluator spy = PowerMockito.spy(new Evaluator());

        spy.evaluate(list);
        PowerMockito.verifyPrivate(spy).invoke("evaluate",any());
    }
}