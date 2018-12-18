package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.mocks.GenerateOrderMock;
import org.han.ica.asd.c.businessrule.parser.ast.Action;

public interface IBusinessRules {
    void programAgent(String agentName, String businessRules);
    Action evaluateBusinessRule(String businessRule, GenerateOrderMock generateOrderMock, int facilityId);
}
