package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ParserPipeline;

public class BusinessRuleHandler implements IBusinessRules{
    public void programAgent(String agentName, String businessRules){
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString(businessRules);
    }
}
