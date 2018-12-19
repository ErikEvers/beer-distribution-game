package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.engine.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.public_interfaces.IBusinessRuleStore;
import org.han.ica.asd.c.businessrule.public_interfaces.IBusinessRules;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import java.util.List;

public class BusinessRuleHandler implements IBusinessRules {
    @Inject
    private IBusinessRuleStore businessRuleStore;

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

        businessRuleStore.synchronizeBusinessRules(agentName, parserPipeline.getBusinessRulesMap());

        return parserPipeline.getBusinessRulesInput();
    }

    public Action evaluateBusinessRule(String businessRule, Round roundData) {
        BusinessRule businessRuleAST = new BusinessRuleDecoder().decodeBusinessRule(businessRule);

        // TO-DO: 12/7/2018 Substitute variables in BusinessRule(tree)

        businessRuleAST.evaluateBusinessRule();

        if (businessRuleAST.isTriggered()){
            return businessRuleAST.getAction();
        }

        return null;
    }
}
