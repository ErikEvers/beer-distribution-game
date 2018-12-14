package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.junit.jupiter.api.Test;

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

        assertEquals(exp.get(0).getBusinessRule(),res.get(0).getBusinessRule());
        assertEquals(exp.get(0).getLineNumber(),res.get(0).getLineNumber());
    }
}
