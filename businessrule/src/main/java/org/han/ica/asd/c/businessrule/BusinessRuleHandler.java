package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ParserPipeline;

public class BusinessRuleHandler implements IBusinessRules{
    public static void main(String[] args) {
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString("if inventory of below is smallest then order 20");
    }

    public void programAgent(String agentName, String businessRules){
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString(businessRules);
        // TO-DO: 12/7/2018 send parsed businessrules to IBusinessRulesStore
    }

    // TO-DO: 12/7/2018 public Action evaluateBusinessRules(String businessRule, RoundData roundData)

}
