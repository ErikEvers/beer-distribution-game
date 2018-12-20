package org.han.ica.asd.c.agent;

import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.public_interfaces.IBusinessRules;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

public class Agent extends GameAgent /* implements IParticipant */{
    @Inject
    @Named("businessRules")
    private IBusinessRules businessRules;

    /**
     * Constructor
     */
    public Agent() {
        super(null, null);
    }

    /**
     * Constructor with default agent name and facility
     *
     * @param gameAgentName The name of the agent
     * @param facility      Which facility it's representing
     */
    public Agent(String gameAgentName, Facility facility) {
        super(gameAgentName, facility);
    }

    /**
     * Generates actions of an agent using the defined business rules.
     *
     * @return              A GameRoundAction with all actions that the agent wants to do.
     */
    public GameRoundAction generateRoundActions() {
        Map<Facility, Integer> targetOrderMap = new HashMap<>();
        Map<Facility, Integer> targetDeliverMap = new HashMap<>();

        for (GameBusinessRules gameBusinessRules : this.gameBusinessRulesList) {
            GameAgentAction action = this.retrieveActionFromBusinessRule(gameBusinessRules.getGameBusinessRule(), null);
            Facility targetFacility = this.resolveFacilityId(action.getTargetFacilityId());
            if(targetFacility != null) {
                if (targetOrderMap.isEmpty() && action instanceof GameAgentOrder) {
                    targetOrderMap.put(targetFacility, action.getAmount());
                } else if (targetDeliverMap.isEmpty() && action instanceof GameAgentDeliver) {
                    targetDeliverMap.put(targetFacility, action.getAmount());
                } else if (!targetOrderMap.isEmpty() && !targetDeliverMap.isEmpty()) {
                    break;
                }
            }
        }

        return new GameRoundAction(targetOrderMap, targetDeliverMap);
    }

    /**
     * Retrieve the action that is described in the business rule
     *
     * @param businessRule  The business rule script that describes the action when the case is true
     * @param round         The round with data to fill in variables that need the values
     * @return              Returns the action that results fom the variable filled business rules
     */
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

    /**
     * Returns the facility of the identifying integer. When the facility is not found, it'll return NULL.
     *
     * @param targetFacilityId  The identifying integer of the facility that needs to be resolved
     * @return                  The facility that needs to be resolved. NULL when facility is not found.
     */
    private Facility resolveFacilityId(int targetFacilityId) {
        for (FacilityLinkedTo link : facility.getFacilitiesLinkedTo()) {
            Facility targetFacility = link.getFacilityDeliver();
            if (targetFacilityId == targetFacility.getFacilityId()) {
                return targetFacility;
            }
        }
        return null;
    }

}
