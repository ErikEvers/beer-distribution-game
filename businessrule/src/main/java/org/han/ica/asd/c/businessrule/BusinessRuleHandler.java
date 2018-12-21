package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.engine.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;

import java.util.List;

public class BusinessRuleHandler implements IBusinessRules {
    /**
     * Parses the business rules and sends it to the persistence component
     *
     * @param agentName     Name for the agent
     * @param businessRules Business rules for the agent
     */
    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
        ParserPipeline parserPipeline = new ParserPipeline();
        if (!parserPipeline.parseString(businessRules)) {
            return parserPipeline.getBusinessRulesInput();
        }

        return parserPipeline.getBusinessRulesInput();
    }

    public ActionModel evaluateBusinessRule(String businessRule, Round roundData) {
        BusinessRule businessRuleAST = new BusinessRuleDecoder().decodeBusinessRule(businessRule);

        //TODO: 12/7/2018 Substitute variables in BusinessRule(tree)

        businessRuleAST.evaluateBusinessRule();

        if (businessRuleAST.isTriggered()){
            Action action = businessRuleAST.getAction();
        	return new ActionModel(
                    action.getType(),
                    action.getAmount(),
                    action.getFacilityId());
        }
        return null;
    }
}
