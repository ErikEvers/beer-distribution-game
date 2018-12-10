package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvaluateBusinessRuleTest {

    @Test
    public void testResolvingAddOperation() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new AddOperation()
                                .addChild(new Value().addValue("20"))
                                .addChild(new CalculationOperator("+"))
                                .addChild(new Value().addValue("4"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("24")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }

    @Test
    public void testResolvingMultipleAddOperations() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new AddOperation()
                                .addChild(new AddOperation()
                                        .addChild(new Value().addValue("20"))
                                        .addChild(new CalculationOperator("+"))
                                        .addChild(new Value().addValue("4")))
                                .addChild(new CalculationOperator("+"))
                                .addChild(new Value().addValue("3"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("27")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }

    @Test
    public void testResolvingDivideOperation() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new DivideOperation()
                                .addChild(new Value().addValue("20"))
                                .addChild(new CalculationOperator("/"))
                                .addChild(new Value().addValue("2"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("10")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }

    @Test
    public void testResolvingMultipleDivideOperations() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new DivideOperation()
                                .addChild(new DivideOperation()
                                        .addChild(new Value().addValue("20"))
                                        .addChild(new CalculationOperator("/"))
                                        .addChild(new Value().addValue("2")))
                                .addChild(new CalculationOperator("/"))
                                .addChild(new Value().addValue("2"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("5")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }

    @Test
    public void testResolvingDivideOperationWithRoundedResult() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new DivideOperation()
                                .addChild(new Value().addValue("17"))
                                .addChild(new CalculationOperator("/"))
                                .addChild(new Value().addValue("2"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("8")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }

    @Test
    public void testResolvingMultiplyOperation() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new MultiplyOperation()
                                .addChild(new Value().addValue("20"))
                                .addChild(new CalculationOperator("*"))
                                .addChild(new Value().addValue("2"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("40")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }

    @Test
    public void testResolvingMultipleMultiplyOperations() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new MultiplyOperation()
                                .addChild(new MultiplyOperation()
                                        .addChild(new Value().addValue("20"))
                                        .addChild(new CalculationOperator("*"))
                                        .addChild(new Value().addValue("2")))
                                .addChild(new CalculationOperator("*"))
                                .addChild(new Value().addValue("2"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("80")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }

    @Test
    public void testResolvingSubtractOperation() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new SubtractOperation()
                                .addChild(new Value().addValue("20"))
                                .addChild(new CalculationOperator("-"))
                                .addChild(new Value().addValue("2"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("18")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }

    @Test
    public void testResolvingMultipleSubtractOperations() {
        BusinessRule businessRuleBefore = new BusinessRule();
        businessRuleBefore.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new SubtractOperation()
                                .addChild(new SubtractOperation()
                                        .addChild(new Value().addValue("20"))
                                        .addChild(new CalculationOperator("-"))
                                        .addChild(new Value().addValue("2")))
                                .addChild(new CalculationOperator("-"))
                                .addChild(new Value().addValue("2"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        BusinessRule businessRuleAfter = new BusinessRule();
        businessRuleAfter.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("16")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        assertTrue(businessRuleBefore.equals(businessRuleAfter));
    }
}
