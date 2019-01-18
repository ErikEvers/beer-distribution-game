package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.businessrule.parser.ast.BooleanLiteral;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

class ParserPipelineTest {

    private ParserPipeline parserPipeline;

    private Provider<ParserPipeline> parserPipelineProvider;


    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRuleStore.class).to(BusinessRuleStoreStub.class);
            }
        });
        parserPipelineProvider = injector.getProvider(ParserPipeline.class);
        parserPipeline = this.parserPipelineProvider.get();
    }

    @Test
    void testParserPipelineTest_getBusinessRulesMap() {
        parserPipeline.parseString("default order 20");

        List<UserInputBusinessRule> res = parserPipeline.getBusinessRulesInput();

        List<UserInputBusinessRule> exp = new ArrayList<>();
        exp.add(new UserInputBusinessRule("default order 20",1));

        assertEquals(exp.get(0).getBusinessRule(), res.get(0).getBusinessRule());
        assertEquals(exp.get(0).getLineNumber(), res.get(0).getLineNumber());
    }

    @Test
    void testFindWordInBusinessRule(){
        String businessRule = "if inventory is 20 then order 20";
        String res = "";

        try {
            res = Whitebox.invokeMethod(parserPipeline, "findWordInBusinessRule", businessRule,3,11);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String exp = "inventory";
        assertEquals(exp, res);
    }

    @Test
    void testEncodeBusinessRules(){
        parserPipeline.parseString("default order 20");

        try {
            Whitebox.invokeMethod(parserPipeline, "encodeBusinessRules");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> res = parserPipeline.getBusinessRulesMap();
        Map<String, String> exp = new HashMap<>();
        exp.put("0 default order 20","BR(D()A(AR(order)V(20)))");

        assertEquals(exp,res);
    }

    @Test
    void testSyntaxError(){
        boolean res = parserPipeline.parseString("defaul order 20");

        boolean exp = false;

        assertEquals(exp, res);
    }
}
