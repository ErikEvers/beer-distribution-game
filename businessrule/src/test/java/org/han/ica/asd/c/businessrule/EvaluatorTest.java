package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.any;

class EvaluatorTest {
    private Evaluator evaluator;

    private Provider<Value> valueProvider;
    private Provider<Comparison> comparisonProvider;
    private Provider<ComparisonValue> comparisonValueProvider;
    private Provider<ComparisonOperator> comparisonOperatorProvider;
    private Provider<BusinessRule> businessRuleProvider;
    private Provider<Evaluator> evaluatorProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRuleStore.class).to(BusinessRuleStoreStub.class);
            }
        });
        valueProvider = injector.getProvider(Value.class);
        comparisonOperatorProvider = injector.getProvider(ComparisonOperator.class);
        comparisonProvider = injector.getProvider(Comparison.class);
        comparisonValueProvider = injector.getProvider(ComparisonValue.class);
        businessRuleProvider = injector.getProvider(BusinessRule.class);
        evaluatorProvider = injector.getProvider(Evaluator.class);
        evaluator = evaluatorProvider.get();
    }

    @Test
    void testcheckRoundIsComparedToInt_ExceptionThrownRoundWithGameObject_SideRight() {
        Comparison comparison = comparisonProvider.get();
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")));
        comparison.addChild(comparisonOperatorProvider.get().addValue("is"));
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
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
        Comparison comparison = comparisonProvider.get();
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
        comparison.addChild(comparisonOperatorProvider.get().addValue("is"));
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")));
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
        Comparison comparison = comparisonProvider.get();
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
        comparison.addChild(comparisonOperatorProvider.get().addValue("is"));
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")));
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
        BusinessRule businessRulesParsed = businessRuleProvider.get();
        list.put(businessRulesInput, businessRulesParsed);

        Evaluator spy = PowerMockito.spy(evaluatorProvider.get());

        spy.evaluate(list);
        PowerMockito.verifyPrivate(spy).invoke("evaluate",any());
    }
}