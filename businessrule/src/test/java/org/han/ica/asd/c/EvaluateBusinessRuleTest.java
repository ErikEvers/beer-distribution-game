package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.*;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvaluateBusinessRuleTest {

    @Test
    public void testResolvingAddOperation() {
        AddOperation addOperation = new AddOperation();
        addOperation.addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("+"))
                .addChild(new Value().addValue("4"));

        Value value = (Value) addOperation.resolveOperation();

        assertEquals("24", value.getValue());
    }

    @Test
    public void testResolvingMultipleAddOperations() {
        AddOperation addOperation = new AddOperation();
        addOperation.addChild(new AddOperation()
                .addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("+"))
                .addChild(new Value().addValue("4")))
                .addChild(new CalculationOperator("+"))
                .addChild(new Value().addValue("3"));

        Value value = (Value) addOperation.resolveOperation();

        assertEquals("27", value.getValue());
    }

    @Test
    public void testResolvingDivideOperation() {
        DivideOperation divideOperation = new DivideOperation();
        divideOperation.addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("/"))
                .addChild(new Value().addValue("4"));

        Value value = (Value) divideOperation.resolveOperation();

        assertEquals("5", value.getValue());
    }

    @Test
    public void testResolvingMultipleDivideOperations() {
        DivideOperation divideOperation = new DivideOperation();
        divideOperation.addChild(new DivideOperation()
                .addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("/"))
                .addChild(new Value().addValue("2")))
                .addChild(new CalculationOperator("/"))
                .addChild(new Value().addValue("2"));

        Value value = (Value) divideOperation.resolveOperation();

        assertEquals("5", value.getValue());
    }

    @Test
    public void testResolvingDivideOperationWithRoundedResult() {
        DivideOperation divideOperation = new DivideOperation();
        divideOperation.addChild(new Value().addValue("17"))
                .addChild(new CalculationOperator("/"))
                .addChild(new Value().addValue("2"));

        Value value = (Value) divideOperation.resolveOperation();

        assertEquals("8", value.getValue());
    }

    @Test
    public void testResolvingMultiplyOperation() {
        MultiplyOperation multiplyOperation = new MultiplyOperation();
        multiplyOperation.addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("*"))
                .addChild(new Value().addValue("4"));

        Value value = (Value) multiplyOperation.resolveOperation();

        assertEquals("80", value.getValue());
    }

    @Test
    public void testResolvingMultipleMultiplyOperations() {
        MultiplyOperation multiplyOperation = new MultiplyOperation();
        multiplyOperation.addChild(new MultiplyOperation()
                .addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("*"))
                .addChild(new Value().addValue("2")))
                .addChild(new CalculationOperator("*"))
                .addChild(new Value().addValue("2"));

        Value value = (Value) multiplyOperation.resolveOperation();

        assertEquals("80", value.getValue());
    }

    @Test
    public void testResolvingSubtractOperation() {
        SubtractOperation subtractOperation = new SubtractOperation();
        subtractOperation.addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("-"))
                .addChild(new Value().addValue("4"));

        Value value = (Value) subtractOperation.resolveOperation();

        assertEquals("16", value.getValue());
    }

    @Test
    public void testResolvingMultipleSubtractOperations() {
        SubtractOperation subtractOperation = new SubtractOperation();
        subtractOperation.addChild(new SubtractOperation()
                .addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("-"))
                .addChild(new Value().addValue("2")))
                .addChild(new CalculationOperator("-"))
                .addChild(new Value().addValue("2"));

        Value value = (Value) subtractOperation.resolveOperation();

        assertEquals("16", value.getValue());
    }

    @Test
    public void testResolvingComparisonStatementWithOneComparisonCondition() {
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
    public void testResolvingDefaultCondition() {
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
    public void testResolvingAndComparisonStatement() {
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
    public void testResolvingOrComparisonStatement() {
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
}
