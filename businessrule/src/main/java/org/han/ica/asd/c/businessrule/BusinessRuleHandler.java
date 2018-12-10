package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.RoundData;
import org.han.ica.asd.c.businessrule.parser.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;

public class BusinessRuleHandler implements IBusinessRules {
    public void programAgent(String agentName, String businessRules) {
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString(businessRules);
    }

    public Action evaluateBusinessRules(String businessRules, RoundData roundData) {
        BusinessRule businessRule = new BusinessRuleDecoder().decodeBusinessRule(businessRules);

        // TO-DO: 12/7/2018 Substitute variables in BusinessRule(tree)

        // TO-DO: 12/7/2018 Transform comparisons to true and false
        businessRule.evaluateBusinessRule();

        return (Action) businessRule.getChildren()
                .get(1);
    }

    public static void main(String[] args) {
        BusinessRuleHandler handler = new BusinessRuleHandler();
        handler.evaluateBusinessRules("BR(CS(C(CV(Add(V(20)CalO(+)V(3)))ComO(>)CV(Add(V(10)CalO(+)V(1)))))A(AR(order)V(10)))", null);

    }
}
