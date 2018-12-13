package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
<<<<<<< HEAD
=======
import org.han.ica.asd.c.model.Round;
>>>>>>> bc343e718f8f015d33e57a900f8f154015caea8e

public class BusinessRuleHandler implements IBusinessRules{
    public void programAgent(String agentName, String businessRules){
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString(businessRules);
    }

    public Action evaluateBusinessRules(String businessRules, Round roundData){
        BusinessRule businessRule =  new BusinessRuleDecoder().decodeBusinessRule(businessRules);

        // TO-DO: 12/7/2018 Substitute variables in BusinessRule(tree)

        // TO-DO: 12/7/2018 Transform comparisons to true and false

        return (Action) businessRule.getChildren()
                .get(1);
    }

}
