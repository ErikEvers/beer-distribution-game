package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.BooleanLiteral;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EvaluateBusinessRuleTest {
    private Provider<Value> valueProvider;
    private Provider<Comparison> comparisonProvider;
    private Provider<ComparisonValue> comparisonValueProvider;
    private Provider<ComparisonOperator> comparisonOperatorProvider;
    private Provider<Action> actionProvider;
    private Provider<ActionReference> actionReferenceProvider;
    private Provider<BusinessRule> businessRuleProvider;
    private Provider<ComparisonStatement> comparisonStatementProvider;
    private Provider<BooleanLiteral> booleanLiteralProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        valueProvider = injector.getProvider(Value.class);
        comparisonOperatorProvider = injector.getProvider(ComparisonOperator.class);
        comparisonProvider = injector.getProvider(Comparison.class);
        comparisonValueProvider = injector.getProvider(ComparisonValue.class);
        actionProvider = injector.getProvider(Action.class);
        actionReferenceProvider = injector.getProvider(ActionReference.class);
        businessRuleProvider = injector.getProvider(BusinessRule.class);
        comparisonStatementProvider = injector.getProvider(ComparisonStatement.class);
        booleanLiteralProvider = injector.getProvider(BooleanLiteral.class);
    }

    @Test
    void testResolvingComparisonStatementWithOneComparisonCondition() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingComparisonStatementWithOperationOnRightSide() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("10")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(new AddOperation()
                                .addChild(valueProvider.get().addValue("20"))
                                .addChild(new CalculationOperator("+"))
                                .addChild(valueProvider.get().addValue("4"))))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(false))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingComparisonStatementWithOperationOnLeftSide() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(new AddOperation()
                                .addChild(valueProvider.get().addValue("20"))
                                .addChild(new CalculationOperator("+"))
                                .addChild(valueProvider.get().addValue("4"))))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("24")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingDefaultCondition() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(new Default())
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingAndComparisonStatement() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(new BooleanOperator("and"))
                .addChild(booleanLiteralProvider.get().setValue(false)))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(false))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingOrComparisonStatement() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(booleanLiteralProvider.get().setValue(false))
                .addChild(new BooleanOperator("or"))
                .addChild(booleanLiteralProvider.get().setValue(false)))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(false))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingEqualsComparison() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("19")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertNotEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingNotEqualsComparison() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("19")))
                        .addChild(comparisonOperatorProvider.get().addValue("not equal"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingHigherThanComparison() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("19")))
                        .addChild(comparisonOperatorProvider.get().addValue("higher"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(false))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingLowerThanComparison() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("19")))
                        .addChild(comparisonOperatorProvider.get().addValue("lower than"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingEqualsComparisonWithOperation() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();

        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(new SubtractOperation()
                                .addChild(valueProvider.get().addValue("30"))
                                .addChild(new CalculationOperator("-"))
                                .addChild(valueProvider.get().addValue("10"))))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingGreaterEqualsComparisonGreater() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("51")))
                        .addChild(comparisonOperatorProvider.get().addValue("higher than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("50")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingGreaterEqualsComparisonEqual() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("51")))
                        .addChild(comparisonOperatorProvider.get().addValue("higher than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("50")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingLessEqualsComparisonLess() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("19")))
                        .addChild(comparisonOperatorProvider.get().addValue("less than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingLessEqualsComparisonEqual() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))
                        .addChild(comparisonOperatorProvider.get().addValue("less than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }
}
