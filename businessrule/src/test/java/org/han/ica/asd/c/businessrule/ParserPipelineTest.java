package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserPipelineTest {

    ParserPipeline parserPipeline = new ParserPipeline();

    @Test
    void testParserPipelineTest_getBusinessRulesMap() {
        List<String> res = parserPipeline.getBusinessRulesInput();

        List<String> exp = new ArrayList<>();

        assertEquals(res, exp);
    }
}
