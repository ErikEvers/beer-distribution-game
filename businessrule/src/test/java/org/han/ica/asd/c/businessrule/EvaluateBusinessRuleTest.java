package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
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
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
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
    private Provider<AddOperation> addOperationProvider;
    private Provider<CalculationOperator> calculationOperatorProvider;
    private Provider<Default> defaultProvider;
    private Provider<BooleanOperator> booleanOperatorProvider;
    private Provider<SubtractOperation> subtractOperationProvider;
    

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStoreStub.class);
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
        addOperationProvider = injector.getProvider(AddOperation.class);
        calculationOperatorProvider = injector.getProvider(CalculationOperator.class);
        defaultProvider = injector.getProvider(Default.class);
        booleanOperatorProvider = injector.getProvider(BooleanOperator.class);
        subtractOperationProvider = injector.getProvider(SubtractOperation.class);
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
                        .addChild(comparisonValueProvider.get().addChild(addOperationProvider.get()
                                .addChild(valueProvider.get().addValue("20"))
                                .addChild(calculationOperatorProvider.get().addValue("+"))
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
                        .addChild(comparisonValueProvider.get().addChild(addOperationProvider.get()
                                .addChild(valueProvider.get().addValue("20"))
                                .addChild(calculationOperatorProvider.get().addValue("+"))
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
        businessRuleBefore.addChild(defaultProvider.get())
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
                .addChild(booleanOperatorProvider.get().addValue("and"))
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
                .addChild(booleanOperatorProvider.get().addValue("or"))
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
                        .addChild(comparisonValueProvider.get().addChild(subtractOperationProvider.get()
                                .addChild(valueProvider.get().addValue("30"))
                                .addChild(calculationOperatorProvider.get().addValue("-"))
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
    void testResolvingOperationInAction() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("19")))
                        .addChild(comparisonOperatorProvider.get().addValue("less than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(subtractOperationProvider.get()
                                .addChild(valueProvider.get().addValue("30"))
                                .addChild(calculationOperatorProvider.get().addValue("-"))
                                .addChild(valueProvider.get().addValue("10"))));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("20")));

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

    @Test
    void testResolvingBusinessRuleWithPercentageValues() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("25%").addValue("20")))
                        .addChild(comparisonOperatorProvider.get().addValue("less than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("50%").addValue("15")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("50%").addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("15")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingBusinessRuleWithPercentageValuesOfComparisonIsNotLessThanOrEqualTo() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("220%").addValue("100")))
                        .addChild(comparisonOperatorProvider.get().addValue("less than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("195%").addValue("100")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("50%").addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("15")));

        businessRuleBefore.evaluateBusinessRule();

        assertNotEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingBusinessRuleWithPercentageValuesGreaterThan100() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("220%").addValue("100")))
                        .addChild(comparisonOperatorProvider.get().addValue("less than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("195%").addValue("100")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("50%").addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("15")));

        businessRuleBefore.evaluateBusinessRule();

        assertNotEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingBusinessRuleWithZeroPercentageEquals() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("0%").addValue("0")))
                        .addChild(comparisonOperatorProvider.get().addValue("greater than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("0%").addValue("1000000")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("0%").addValue("30")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("0")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }

    @Test
    void testResolvingBusinessRuleWithPercentagesAndZeroValuesEquals() {
        BusinessRule businessRuleBefore = businessRuleProvider.get();
        businessRuleBefore.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("40918341%").addValue("0")))
                        .addChild(comparisonOperatorProvider.get().addValue("greater than or equal to"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("34128341%").addValue("0")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("1234567890%").addValue("0")));

        BusinessRule businessRuleAfter = businessRuleProvider.get();
        businessRuleAfter.addChild(booleanLiteralProvider.get().setValue(true))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("0")));

        businessRuleBefore.evaluateBusinessRule();

        assertEquals(businessRuleAfter, businessRuleBefore);
    }
}
