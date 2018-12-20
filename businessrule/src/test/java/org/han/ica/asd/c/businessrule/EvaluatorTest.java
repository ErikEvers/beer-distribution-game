package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.any;

class EvaluatorTest {
    private Evaluator evaluator = new Evaluator();

    @Test
    void testcheckRoundIsComparedToInt_ExceptionThrownRoundWithGameObject_SideRight() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("round")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);

        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, inputBusinessRule);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Round can only be compared to a number", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testcheckRoundIsComparedToInt_ExceptionThrownRoundWithGameObject_SideLeft() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("round")));
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);


        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, inputBusinessRule);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Round can only be compared to a number", inputBusinessRule.getErrorMessage());
    }

    @Test
    void testCheckSubTreeNotInt_NoExceptionThrown_RoundWithValue() {
        Comparison comparison = new Comparison();
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value().addValue("20")));
        UserInputBusinessRule inputBusinessRule = new UserInputBusinessRule("Businessrule",1);


        try {
            Whitebox.invokeMethod(evaluator, "checkRoundIsComparedToInt", comparison, inputBusinessRule, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(inputBusinessRule.getErrorMessage());
    }

    @Test
    void testEvaluate_Called_evaluate() throws Exception {
        Map<UserInputBusinessRule,BusinessRule> list = new HashMap<>();
        UserInputBusinessRule businessRulesInput = new UserInputBusinessRule("if inventory is 20 order 20",1);
        BusinessRule businessRulesParsed = new BusinessRule();
        list.put(businessRulesInput, businessRulesParsed);

        Evaluator spy = PowerMockito.spy(new Evaluator());

        spy.evaluate(list);
        PowerMockito.verifyPrivate(spy).invoke("evaluate",any());
    }
}