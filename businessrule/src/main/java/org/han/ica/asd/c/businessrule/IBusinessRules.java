package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.model.domain_objects.Round;

public interface IBusinessRules {
    void programAgent(String agentName, String businessRules);
    Action evaluateBusinessRule(String businessRule, Round roundData);
}
