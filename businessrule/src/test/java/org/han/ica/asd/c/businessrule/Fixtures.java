package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.BooleanLiteral;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;

import javax.inject.Inject;
import javax.inject.Provider;

class Fixtures {
    private static Provider<Value> valueProvider;
    private static Provider<Comparison> comparisonProvider;
    private static Provider<ComparisonValue> comparisonValueProvider;
    private static Provider<ComparisonOperator> comparisonOperatorProvider;
    private static Provider<Action> actionProvider;
    private static Provider<ActionReference> actionReferenceProvider;
    private static Provider<BusinessRule> businessRuleProvider;
    private static Provider<ComparisonStatement> comparisonStatementProvider;
    private static Provider<DivideOperation> divideOperationProvider;
    private static Provider<CalculationOperator> calculationOperatorProvider;
    private static Provider<BooleanOperator> booleanOperatorProvider;
    private static Provider<Default> defaultProvider;
    private static Provider<AddOperation> addOperationProvider;
    private static Provider<SubtractOperation> subtractOperationProvider;

    @Inject
    public Fixtures(Provider<SubtractOperation> subtractOperationProvider, Provider<AddOperation> addOperationProvider, Provider<BooleanOperator> booleanOperatorProvider, Provider<Default> defaultProvider, Provider<Value> valueProvider, Provider<Comparison> comparisonProvider, Provider<ComparisonValue> comparisonValueProvider, Provider<ComparisonOperator> comparisonOperatorProvider, Provider<Action> actionProvider, Provider<ActionReference> actionReferenceProvider, Provider<BusinessRule> businessRuleProvider, Provider<BusinessRuleHandler> businessRuleHandlerProvider, Provider<ComparisonStatement> comparisonStatementProvider, Provider<DivideOperation> divideOperationProvider, Provider<CalculationOperator> calculationOperatorProvider, Provider<BooleanLiteral> booleanLiteralProvider, Provider<Evaluator> evaluatorProvider) {
        Fixtures.valueProvider = valueProvider;
        Fixtures.comparisonProvider = comparisonProvider;
        Fixtures.comparisonValueProvider = comparisonValueProvider;
        Fixtures.comparisonOperatorProvider = comparisonOperatorProvider;
        Fixtures.actionProvider = actionProvider;
        Fixtures.actionReferenceProvider = actionReferenceProvider;
        Fixtures.businessRuleProvider = businessRuleProvider;
        Fixtures.comparisonStatementProvider = comparisonStatementProvider;
        Fixtures.divideOperationProvider = divideOperationProvider;
        Fixtures.calculationOperatorProvider = calculationOperatorProvider;
        Fixtures.defaultProvider = defaultProvider;
        Fixtures.booleanOperatorProvider = booleanOperatorProvider;
        Fixtures.addOperationProvider = addOperationProvider;
        Fixtures.subtractOperationProvider = subtractOperationProvider;
    }

    //Setup a businessRule to test a basic rule
    static BusinessRule BasicRule() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));

        return businessRule;
    }

    //Setup a businessRule to test a and rule.
    static BusinessRule BasicRuleAND() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20"))))
                .addChild(booleanOperatorProvider.get().addValue("and"))
                .addChild(comparisonStatementProvider.get()
                        .addChild(comparisonProvider.get()
                                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("backlog")))
                                .addChild(comparisonOperatorProvider.get().addValue("higher"))
                                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20"))))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));
        return businessRule;
    }

    //Setup a businessRule to test a or rule.
    static BusinessRule BasicRuleOR() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20"))))
                .addChild(booleanOperatorProvider.get().addValue("or"))
                .addChild(comparisonStatementProvider.get()
                        .addChild(comparisonProvider.get()
                                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("backlog")))
                                .addChild(comparisonOperatorProvider.get().addValue("higher"))
                                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20"))))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));
        return businessRule;
    }

    //Setup a businessRule to test a calculation.
    static BusinessRule RuleCalculate() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(addOperationProvider.get()
                                .addChild(valueProvider.get().addValue("20"))
                                .addChild(calculationOperatorProvider.get().addValue("+"))
                                .addChild(valueProvider.get().addValue("4"))))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));
        return businessRule;
    }

    //Setup a businessrule to test multiple calculations
    static BusinessRule RuleMultipleCalculate() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(subtractOperationProvider.get()
                                .addChild(divideOperationProvider.get()
                                        .addChild(valueProvider.get().addValue("20"))
                                        .addChild(calculationOperatorProvider.get().addValue("/"))
                                        .addChild(valueProvider.get().addValue("4")))
                                .addChild(calculationOperatorProvider.get().addValue("-"))
                                .addChild(valueProvider.get().addValue("3"))))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));
        return businessRule;
    }

    //Setup a businessrule to test Default rule
    static BusinessRule RuleDefault() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(defaultProvider.get())
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("40")));
        return businessRule;
    }

    //Setup a businessrule to test Round rule
    static BusinessRule RuleWithRound() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")))
                        .addChild(comparisonOperatorProvider.get().addValue("is"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("11")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));
        return businessRule;
    }

    //Setup a businessrule to test Percentage rule
    static BusinessRule RulePercentage() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(comparisonStatementProvider.get()
                .addChild(comparisonProvider.get()
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")))
                        .addChild(comparisonOperatorProvider.get().addValue("is higher"))
                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20%").addValue("backlog")))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("30")));
        return businessRule;
    }
}