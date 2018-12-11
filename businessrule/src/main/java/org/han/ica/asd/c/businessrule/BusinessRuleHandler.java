package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ParserPipeline;

public class BusinessRuleHandler implements IBusinessRules{
    /**
     * Parses the business rules and sends it to the persistence component
     * @param agentName Name for the agent
     * @param businessRules Business rules for the agent
     */
    public void programAgent(String agentName, String businessRules){
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString(businessRules);
        // TO-DO: 12/7/2018 send parsed businessrules to IBusinessRulesStore
    }

    // TO-DO: 12/7/2018 public Action evaluateBusinessRules(String businessRule, RoundData roundData)

}
