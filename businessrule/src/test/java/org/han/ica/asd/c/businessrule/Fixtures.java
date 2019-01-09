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
    private  Provider<Value> valueProvider;
    private  Provider<Comparison> comparisonProvider;
    private  Provider<ComparisonValue> comparisonValueProvider;
    private  Provider<ComparisonOperator> comparisonOperatorProvider;
    private  Provider<Action> actionProvider;
    private  Provider<ActionReference> actionReferenceProvider;
    private  Provider<BusinessRule> businessRuleProvider;
    private  Provider<ComparisonStatement> comparisonStatementProvider;
    private  Provider<DivideOperation> divideOperationProvider;
    private  Provider<CalculationOperator> calculationOperatorProvider;
    private  Provider<BooleanOperator> booleanOperatorProvider;
    private  Provider<Default> defaultProvider;
    private  Provider<AddOperation> addOperationProvider;
    private  Provider<SubtractOperation> subtractOperationProvider;

    @Inject
    public Fixtures(Provider<SubtractOperation> subtractOperationProvider, Provider<AddOperation> addOperationProvider, Provider<BooleanOperator> booleanOperatorProvider, Provider<Default> defaultProvider, Provider<Value> valueProvider, Provider<Comparison> comparisonProvider, Provider<ComparisonValue> comparisonValueProvider, Provider<ComparisonOperator> comparisonOperatorProvider, Provider<Action> actionProvider, Provider<ActionReference> actionReferenceProvider, Provider<BusinessRule> businessRuleProvider, Provider<BusinessRuleHandler> businessRuleHandlerProvider, Provider<ComparisonStatement> comparisonStatementProvider, Provider<DivideOperation> divideOperationProvider, Provider<CalculationOperator> calculationOperatorProvider, Provider<BooleanLiteral> booleanLiteralProvider, Provider<Evaluator> evaluatorProvider) {
        this.valueProvider = valueProvider;
        this.comparisonProvider = comparisonProvider;
        this.comparisonValueProvider = comparisonValueProvider;
        this.comparisonOperatorProvider = comparisonOperatorProvider;
        this.actionProvider = actionProvider;
        this.actionReferenceProvider = actionReferenceProvider;
        this.businessRuleProvider = businessRuleProvider;
        this.comparisonStatementProvider = comparisonStatementProvider;
        this.divideOperationProvider = divideOperationProvider;
        this.calculationOperatorProvider = calculationOperatorProvider;
        this.defaultProvider = defaultProvider;
        this.booleanOperatorProvider = booleanOperatorProvider;
        this.addOperationProvider = addOperationProvider;
        this.subtractOperationProvider = subtractOperationProvider;
    }

    //Setup a businessRule to test a basic rule
     BusinessRule BasicRule() {
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
     BusinessRule BasicRuleAND() {
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
     BusinessRule BasicRuleOR() {
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
     BusinessRule RuleCalculate() {
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
     BusinessRule RuleMultipleCalculate() {
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
     BusinessRule RuleDefault() {
        BusinessRule businessRule = businessRuleProvider.get();
        businessRule.addChild(defaultProvider.get())
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("40")));
        return businessRule;
    }

    //Setup a businessrule to test Round rule
     BusinessRule RuleWithRound() {
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
     BusinessRule RulePercentage() {
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