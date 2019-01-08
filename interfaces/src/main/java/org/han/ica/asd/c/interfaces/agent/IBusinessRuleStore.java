package org.han.ica.asd.c.interfaces.agent;

import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;

public interface IBusinessRuleStore {
    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);
}
