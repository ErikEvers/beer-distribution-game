package org.han.ica.asd.c.gamelogic.stubs;

import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;

import java.util.List;

public class BusinessRuleStub implements IBusinessRules {
    @Override
    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
        return null;
    }

    @Override
    public ActionModel evaluateBusinessRule(String businessRule, Round round, int facilityId) {
        return null;
    }
}
