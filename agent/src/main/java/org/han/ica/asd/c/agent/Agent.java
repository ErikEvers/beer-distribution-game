package org.han.ica.asd.c.agent;

import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.model.domain_objects.*;
import org.han.ica.asd.c.model.interface_models.ActionModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent extends GameAgent /* implements IParticipant */{
    @Inject
    @Named("businessRules")
    private IBusinessRules businessRules;

    @Inject
    @Named("persistance")
    private IPersistence persistence;

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
     * @param round         The round data of last round used to determine what actions the agent is going to do.
     * @return              A GameRoundAction with all actions that the agent wants to do.
     */
    public GameRoundAction generateRoundActions(Round round) {
        Map<Facility, Integer>  targetOrderMap          = new HashMap<>();
        Map<Facility, Integer>  targetDeliverMap        = new HashMap<>();
        List<GameBusinessRules> triggeredBusinessRules  = new ArrayList<>();
        List<Round>             triggeredRounds         = new ArrayList<>();

        for (GameBusinessRules gameBusinessRules : this.gameBusinessRulesList) {
        	// Hier ergens GameBusinessRulesInFacilityTurn aanmaken met getriggerde Business rules en de verwante Rounds

            ActionModel actionModel = businessRules.evaluateBusinessRule(gameBusinessRules.getGameBusinessRule(), round);
            if(actionModel != null) {
                Facility targetFacility = this.resolveFacilityId(actionModel.facilityId);
                if(targetFacility != null) {
                    if (targetOrderMap.isEmpty() && actionModel.isOrderType()) {
                        targetOrderMap.put(targetFacility, actionModel.amount);
                        triggeredBusinessRules.add(gameBusinessRules);
                        triggeredRounds.add(round);
                    } else if (targetDeliverMap.isEmpty() && actionModel.isDeliverType()) {
                        targetDeliverMap.put(targetFacility, actionModel.amount);
                        triggeredBusinessRules.add(gameBusinessRules);
                        triggeredRounds.add(round);
                    } else if (!targetOrderMap.isEmpty() && !targetDeliverMap.isEmpty()) {
                        break;
                    }
                }
            }
        }

        persistence.logUsedBusinessRuleToCreateOrder(
            new GameBusinessRulesInFacilityTurn(triggeredRounds, triggeredBusinessRules));
        return new GameRoundAction(targetOrderMap, targetDeliverMap);
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
