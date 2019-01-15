package org.han.ica.asd.c.agent;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.interface_models.ActionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ActionCollector {
    Map<Facility, Integer> orderMap = new HashMap<>();
    Map<Facility, Integer> deliverMap = new HashMap<>();
    List<GameBusinessRules> businessRulesList = new ArrayList<>();

    void addOrderAction(Facility facility, ActionModel actionModel, GameBusinessRules gameBusinessRules) {
        orderMap.put(facility, actionModel.amount);
        businessRulesList.add(gameBusinessRules);
    }

    void addDeliverAction(Facility facility, ActionModel actionModel, GameBusinessRules gameBusinessRules) {
        deliverMap.put(facility, actionModel.amount);
        businessRulesList.add(gameBusinessRules);
    }


    boolean needsOrderAction(ActionModel actionModel) {
        return orderMap.isEmpty() && actionModel.isOrderType();
    }

    boolean needsDeliverAction(ActionModel actionModel) {
        return deliverMap.isEmpty() && actionModel.isDeliverType();
    }

    boolean hasBothActionTypes() {
        return orderMap.isEmpty() || deliverMap.isEmpty();
    }
}
