package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

class ParserPipelineTest {

    private ParserPipeline parserPipeline = new ParserPipeline();

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
    void testFindEndErrorWord(){
        String businessRule = "if inventory is 20 then order 20";
        int res = -1;

        try {
            res = Whitebox.invokeMethod(parserPipeline, "findEndErrorWord", businessRule,12);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int exp = 11;
        assertEquals(exp, res);
    }

    @Test
    void testFindBeginErrorWord(){
        String businessRule = "if inventory is 20 then order 20";
        int res = -1;

        try {
            res = Whitebox.invokeMethod(parserPipeline, "findBeginErrorWord", businessRule,11);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int exp = 3;
        assertEquals(exp, res);
    }

    @Test
    void test(){
        boolean res = parserPipeline.parseString("defaul order 20");

        boolean exp = false;

        assertEquals(exp, res);
    }
}
