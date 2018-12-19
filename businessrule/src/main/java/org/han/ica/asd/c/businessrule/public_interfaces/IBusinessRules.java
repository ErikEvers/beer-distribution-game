package org.han.ica.asd.c.businessrule.public_interfaces;

import org.han.ica.asd.c.model.pojo.GameAgentAction;
import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public interface IBusinessRules {
    List<UserInputBusinessRule> programAgent(String agentName, String businessRules);

    GameAgentAction evaluateBusinessRule(String businessRule, Round roundData);
}
