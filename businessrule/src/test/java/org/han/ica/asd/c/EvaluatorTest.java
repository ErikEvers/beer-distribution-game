package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.evaluator.BusinessRuleException;
import org.han.ica.asd.c.businessrule.parser.evaluator.Counter;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

class EvaluatorTest {
    Evaluator evaluator = new Evaluator();

    @Test
    void testCheckSubTreeNotInt_ExceptionThrownRoundWithGameObject_SideRight() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("round")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        try {
            evaluator.checkSubTreeNotInt(comparison, 1, 2);
            fail("expected exception was not occured.");
        } catch (BusinessRuleException e) {
        }
    }

    @Test
    void testCheckSubTreeNotInt_ExceptionThrownRoundWithGameObject_SideLeft() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("round")));
        try {
            evaluator.checkSubTreeNotInt(comparison, 1, 2);
            fail("expected exception was not occured.");
        } catch (BusinessRuleException e) {
        }
    }

    @Test
    void testCheckSubTreeNotInt_NoExceptionThrown_RoundWithValue() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("20")));
        try {
            evaluator.checkSubTreeNotInt(comparison, 1, 2);
        } catch (BusinessRuleException e) {
            fail("Exception was thrown, no exception needed.");
        }
    }

    @Test
    void testCheckOnlyOneDefault_ExceptionThrown_TwoDefaults() {
        Default aDefault = new Default();
        Counter counter = new Counter();
        counter.addOne();
        try {
            evaluator.checkOnlyOneDefault(aDefault, 1, counter);
            fail("expected exception was not occured.");
        } catch (BusinessRuleException e) {

        }
    }

    @Test
    void testCheckOnlyOneDefault_ExceptionThrown_oneDefaults() {
        Default aDefault = new Default();
        Counter counter = new Counter();
        try {
            evaluator.checkOnlyOneDefault(aDefault, 1, counter);
        } catch (BusinessRuleException e) {
            fail("Exception was thrown, no exception needed.");
        }
    }
    /*@Test
    void testCheckRoundIsComparedToInt_Called_checkSubTreeNotInt() throws BusinessRuleException {
        Comparison comparison = mock(Comparison.class);

        verify(comparison).getChildren();
    }*/
}