package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.model.Round;

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

    public Action evaluateBusinessRule(String businessRule, Round roundData){
        BusinessRule businessRuleAST =  new BusinessRuleDecoder().decodeBusinessRule(businessRule);

        // TO-DO: 12/7/2018 Substitute variables in BusinessRule(tree)

        // TO-DO: 12/7/2018 Transform comparisons to true and false
        businessRuleAST.evaluateBusinessRule();

        return (Action) businessRuleAST.getChildren()
                .get(1);
    }
}
