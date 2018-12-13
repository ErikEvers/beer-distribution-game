package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.model.Round;

import java.util.List;

public interface IBusinessRules {
    List<UserInputBusinessRule> programAgent(String agentName, String businessRules);
    Action evaluateBusinessRule(String businessRule, Round roundData);
}
