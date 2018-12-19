package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.*;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EvaluateBusinessRuleTest {

    @Test
    void testResolvingComparisonStatementWithOneComparisonCondition() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingComparisonStatementWithOperationOnRightSide() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("10")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new AddOperation()
                                .addChild(new Value().addValue("20"))
                                .addChild(new CalculationOperator("+"))
                                .addChild(new Value().addValue("4"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(false))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingComparisonStatementWithOperationOnLeftSide() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new AddOperation()
                                .addChild(new Value().addValue("20"))
                                .addChild(new CalculationOperator("+"))
                                .addChild(new Value().addValue("4"))))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("24")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingDefaultCondition() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new Default())
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingAndComparisonStatement() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new BooleanLiteral(true))
                .addChild(new BooleanOperator("and"))
                .addChild(new BooleanLiteral(false)))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(false))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingOrComparisonStatement() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new BooleanLiteral(false))
                .addChild(new BooleanOperator("or"))
                .addChild(new BooleanLiteral(false)))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(false))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingEqualsComparison() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("19")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertNotEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingNotEqualsComparison() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("19")))
                        .addChild(new ComparisonOperator("not equal"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingHigherThanComparison() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("19")))
                        .addChild(new ComparisonOperator("higher than"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(false))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingLowerThanComparison() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("19")))
                        .addChild(new ComparisonOperator("lower than"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingEqualsComparisonWithOperation() {
        BusinessRule businessRuleBefore = new BusinessRule();

        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new SubtractOperation()
                                .addChild(new Value().addValue("30"))
                                .addChild(new CalculationOperator("-"))
                                .addChild(new Value().addValue("10"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingGreaterEqualsComparisonGreater() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("51")))
                        .addChild(new ComparisonOperator("greater equal"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("50")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingGreaterEqualsComparisonEqual() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("51")))
                        .addChild(new ComparisonOperator("greater equal"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("50")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingLessEqualsComparisonLess() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("19")))
                        .addChild(new ComparisonOperator("less equal"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingLessEqualsComparisonEqual() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("less equal"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new BooleanLiteral(true))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }
}
