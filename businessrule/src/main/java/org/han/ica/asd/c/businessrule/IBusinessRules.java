package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.UserInputException;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.model.Round;

import java.util.List;

public interface IBusinessRules {
    List<UserInputBusinessRule> programAgent(String agentName, List<UserInputBusinessRule> businessRules);
    Action evaluateBusinessRule(String businessRule, Round roundData);
}
