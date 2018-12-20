package org.han.ica.asd.c.agent;

import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.public_interfaces.IBusinessRules;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent extends GameAgent {
    @Inject
    @Named("businessRules")
    private IBusinessRules businessRules;

    public Agent() {
        super(null, null);
    }
    public Agent(String gameAgentName, Facility facility) {
        super(gameAgentName, facility);
    }

//    @Inject
//    public Agent(@Named("businessRules") IBusinessRules businessRules) {
//        super(null, null);
//        this.businessRules = businessRules;
//    }

    public List<Map<Facility, Integer>> generateOrder() {
        Map<Facility, Integer> targetOrderMap = new HashMap<>();
        Map<Facility, Integer> targetDeliverMap = new HashMap<>();

        for (GameBusinessRules gameBusinessRules : this.gameBusinessRulesList) {
            GameAgentAction action = this.retrieveActionFromBusinessRule(gameBusinessRules.getGameBusinessRule(), null);
            Facility targetFacility = new Facility(new FacilityType(null, 0, 0, 0, 0, 0, 0), new ArrayList<>(), 1);
//            Facility targetFacility = resolveFacilityId(action.getTargetFacilityId());
            if (targetOrderMap.isEmpty() && action instanceof GameAgentOrder) {
                targetOrderMap.put(targetFacility, action.getAmount());
            } else if (targetDeliverMap.isEmpty() && action instanceof GameAgentDeliver) {
                targetDeliverMap.put(targetFacility, action.getAmount());
            } else if (!targetOrderMap.isEmpty() && !targetDeliverMap.isEmpty()) {
                break;
            }
        }

        List<Map<Facility, Integer>> actionList = new ArrayList<>();
        actionList.add(targetOrderMap);
        actionList.add(targetDeliverMap);

        return actionList;
    }

    private GameAgentAction retrieveActionFromBusinessRule(String businessRule, Round round) {
        GameAgentAction gameAgentAction = null;

        Action action = businessRules.evaluateBusinessRule(businessRule, round);

        if (action != null) {
            if (action.isOrderType()) {
                gameAgentAction = new GameAgentOrder();
            } else if (action.isDeliverType()) {
                gameAgentAction = new GameAgentDeliver();
            }

            if (gameAgentAction != null) {
                gameAgentAction.setAmount(action.getAmount());
                return gameAgentAction;
            }
        }

        return new GameAgentEmptyAction();
    }

    public Facility resolveFacilityId(int targetFacilityId) {
        for (FacilityLinkedTo link : facility.getFacilitiesLinkedTo()) {
            Facility targetFacility = link.getFacilityDeliver();
            if (targetFacilityId == targetFacility.getFacilityId()) {
                return targetFacility;
            }
        }
        return null;
    }

}
