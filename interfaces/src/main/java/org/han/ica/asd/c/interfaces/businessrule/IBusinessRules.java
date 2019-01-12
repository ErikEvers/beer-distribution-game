package org.han.ica.asd.c.interfaces.businessrule;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;

import java.util.List;

public interface IBusinessRules {
    List<UserInputBusinessRule> programAgent(String agentName, String businessRules);

    ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId);
}
