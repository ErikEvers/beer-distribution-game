package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.ast.Action;
import org.han.ica.asd.c.businessrule.ast.ActionReference;
import org.han.ica.asd.c.businessrule.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.ast.Default;
import org.han.ica.asd.c.businessrule.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.ast.operations.Value;
import org.han.ica.asd.c.businessrule.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.ast.operators.ComparisonOperator;

import java.util.ArrayList;
import java.util.List;

class Fixtures {
    //Setup a businessRule to test a basic rule
    static BusinessRule BasicRule() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value("20")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value("30")));

        return businessRule;
    }

    //Setup a businessRule to test a and rule.
    static BusinessRule BasicRuleAND() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value("20"))))
                .addChild(new BooleanOperator("and"))
                .addChild(new ComparisonStatement()
                        .addChild(new Comparison()
                                .addChild(new ComparisonValue().addChild(new Value("backlog")))
                                .addChild(new ComparisonOperator("higher than"))
                                .addChild(new ComparisonValue().addChild(new Value("20"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value("30")));
        return businessRule;
    }

    //Setup a businessRule to test a or rule.
    static BusinessRule BasicRuleOR() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value("20"))))
                .addChild(new BooleanOperator("or"))
                .addChild(new ComparisonStatement()
                        .addChild(new Comparison()
                                .addChild(new ComparisonValue().addChild(new Value("backlog")))
                                .addChild(new ComparisonOperator("higher than"))
                                .addChild(new ComparisonValue().addChild(new Value("20"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value("30")));
        return businessRule;
    }

    //Setup a businessRule to test a calculation.
    static BusinessRule RuleCalculate() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new AddOperation()
                                .addChild(new Value("20"))
                                .addChild(new CalculationOperator("+"))
                                .addChild(new Value("4"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value("30")));
        return businessRule;
    }

    //Setup a businessrule to test multiple calculations
    static BusinessRule RuleMultipleCalculate() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value("inventory")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new SubtractOperation()
                                .addChild(new DivideOperation()
                                        .addChild(new Value("20"))
                                        .addChild(new CalculationOperator("/"))
                                        .addChild(new Value("4")))
                                .addChild(new CalculationOperator("-"))
                                .addChild(new Value("3"))))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value("30")));
        return businessRule;
    }

    static BusinessRule RuleDefault() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new Default())
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value("40")));
        return businessRule;
    }

    static BusinessRule RuleWithRound() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value("round")))
                        .addChild(new ComparisonOperator("is"))
                        .addChild(new ComparisonValue().addChild(new Value("11")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value("30")));
        return businessRule;
    }

    static BusinessRule RulePercentage() {
        BusinessRule businessRule = new BusinessRule();
        businessRule.addChild(new ComparisonStatement()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value("inventory")))
                        .addChild(new ComparisonOperator("is higher than"))
                        .addChild(new ComparisonValue().addChild(new Value("20% backlog")))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new Value("30")));
        return businessRule;
    }
}
