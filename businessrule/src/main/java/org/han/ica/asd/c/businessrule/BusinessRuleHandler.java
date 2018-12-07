package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ParserPipeline;

public class BusinessRuleHandler implements IBusinessRules{
    public void programAgent(String agentName, String businessRules){
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString("if inventory is 20 then order 40");
        // TODO: send parsed businessrules to IBusinessRulesStore
    }

    // TODO:   public Action evaluateBusinessRules(String businessRule, RoundData roundData){
    //        return null;
    //    }

}
