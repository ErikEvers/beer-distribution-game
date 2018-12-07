package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ParserPipeline;

public class BusinessRuleHandler implements IBusinessRules{
    public static void main(String[] args) {
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString("if above is the lowest then order 40");

        System.out.println("---- The given input ----");
        System.out.println(parserPipeline.getBusinessRulesInput());
        System.out.println("---- The generated Businessrules Map ----");
        System.out.println(parserPipeline.getBusinessRulesMap());
    }

    public void programAgent(String agentName, String businessRules){
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString("if inventory is 20 then order 40");
        // send parsed businessrules to IBusinessRulesStore
    }

    // TODO:   public Action evaluateBusinessRules(String businessRule, RoundData roundData){
    //        return null;
    //    }

}
