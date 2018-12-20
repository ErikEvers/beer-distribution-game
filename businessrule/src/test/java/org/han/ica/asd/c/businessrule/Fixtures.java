package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
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

class Fixtures {
    //Setup a businessRule to test a basic rule
    static BusinessRule BasicRule() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));

        return businessRule;
    }

    //Setup a businessRule to test a and rule.
    static BusinessRule BasicRuleAND() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20"))))
                .addChild(new BooleanOperator("and"))
                .addChild(new ComparisonStatement()
                        .addChild(new Comparison()
                                .addChild(new ComparisonValue().addChild(new Value().addValue("backlog")))
                                .addChild(new ComparisonOperator("higher"))
                                .addChild(new ComparisonValue().addChild(new Value().addValue("20"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));
        return businessRule;
    }

    //Setup a businessRule to test a or rule.
    static BusinessRule BasicRuleOR() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20"))))
                .addChild(new BooleanOperator("or"))
                .addChild(new ComparisonStatement()
                        .addChild(new Comparison()
                                .addChild(new ComparisonValue().addChild(new Value().addValue("backlog")))
                                .addChild(new ComparisonOperator("higher"))
                                .addChild(new ComparisonValue().addChild(new Value().addValue("20"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));
        return businessRule;
    }

    //Setup a businessRule to test a calculation.
    static BusinessRule RuleCalculate() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new AddOperation()
                                .addChild(new Value().addValue("20"))
                                .addChild(new CalculationOperator("+"))
                                .addChild(new Value().addValue("4"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));
        return businessRule;
    }

    //Setup a businessrule to test multiple calculations
    static BusinessRule RuleMultipleCalculate() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new SubtractOperation()
                                .addChild(new DivideOperation()
                                        .addChild(new Value().addValue("20"))
                                        .addChild(new CalculationOperator("/"))
                                        .addChild(new Value().addValue("4")))
                                .addChild(new CalculationOperator("-"))
                                .addChild(new Value().addValue("3"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));
        return businessRule;
    }

    //Setup a businessrule to test Default rule
    static BusinessRule RuleDefault() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new Default())
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("40")));
        return businessRule;
    }

    //Setup a businessrule to test t rule
    static BusinessRule RuleWithRound() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("round")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("11")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));
        return businessRule;
    }

    //Setup a businessrule to test Percentage rule
    static BusinessRule RulePercentage() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("is higher"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("20%").addValue("backlog")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value().addValue("30")));
        return businessRule;
    }
}