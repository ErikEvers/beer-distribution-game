package org.han.ica.asd.c.agent;

import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.*;
import org.han.ica.asd.c.model.interface_models.ActionModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent extends GameAgent implements IParticipant {
    private Configuration configuration;

    @Inject
    @Named("businessRules")
    private IBusinessRules businessRules;

    @Inject
    @Named("persistence")
    private IPersistence persistence;

    /**
     * Constructor with default agent name and facility
     *
     * @param gameAgentName The name of the agent
     * @param facility      Which facility it's representing
     */
    Agent(Configuration configuration, String gameAgentName, Facility facility, List<GameBusinessRules> gameBusinessRulesList) {
        super(gameAgentName, facility, gameBusinessRulesList);
        this.configuration = configuration;
    }

    /**
     * Generates actions of an agent using the defined business rules.
     *
     * @param round         The round data of last round used to determine what actions the agent is going to do.
     * @return              A GameRoundAction with all actions that the agent wants to do.
     */
    private GameRoundAction generateRoundActions(Round round) {
        Map<Facility, Integer>  targetOrderMap          = new HashMap<>();
        Map<Facility, Integer>  targetDeliverMap        = new HashMap<>();
        List<GameBusinessRules> triggeredBusinessRules  = new ArrayList<>();

        for (GameBusinessRules gameBusinessRules : this.getGameBusinessRules()) {
            ActionModel actionModel = businessRules.evaluateBusinessRule(gameBusinessRules.getGameAST(), round);
            if(actionModel != null) {
								if (targetOrderMap.isEmpty() && actionModel.isOrderType()) {
										Facility targetFacility = this.resolveLowerFacilityId(actionModel.facilityId);
										if(targetFacility == null) {
											break;
										}
										targetOrderMap.put(targetFacility, actionModel.amount);
										triggeredBusinessRules.add(gameBusinessRules);
								} else if (targetDeliverMap.isEmpty() && actionModel.isDeliverType()) {
										Facility targetFacility = this.resolveHigherFacilityId(actionModel.facilityId);
										if(targetFacility == null) {
											break;
										}
										targetDeliverMap.put(targetFacility, actionModel.amount);
										triggeredBusinessRules.add(gameBusinessRules);
								} else if (!targetOrderMap.isEmpty() && !targetDeliverMap.isEmpty()) {
										break;
								}
            }
        }

        persistence.logUsedBusinessRuleToCreateOrder(
            new GameBusinessRulesInFacilityTurn(getFacility().getFacilityId(), round.getRoundId(), getGameAgentName() + 1, triggeredBusinessRules));
        return new GameRoundAction(targetOrderMap, targetDeliverMap);
    }

    /**
     * Returns the facility of the identifying integer. When the facility is not found, it'll return NULL.
     *
     * @param targetFacilityId  The identifying integer of the facility that needs to be resolved
     * @return                  The facility below the current facility that needs to be resolved. NULL when facility is not found.
     */
    private Facility resolveLowerFacilityId(int targetFacilityId) {
				List<Facility> links = new ArrayList<>(configuration.getFacilitiesLinkedTo().get(getFacility()));
        for (Facility link : links) {
            if (targetFacilityId == link.getFacilityId()) {
                return link;
            }
        }
        return null;
    }

	/**
	 * Returns the facility of the identifying integer. When the facility is not found, it'll return NULL.
	 *
	 * @param targetFacilityId  The identifying integer of the facility that needs to be resolved
	 * @return                  The facility above the current facility that needs to be resolved. NULL when facility is not found.
	 */
	private Facility resolveHigherFacilityId(int targetFacilityId) {
		for(Map.Entry<Facility, List<Facility>> link: configuration.getFacilitiesLinkedTo().entrySet()) {
			if(link.getValue().stream().anyMatch(f -> f.getFacilityId() == getFacility().getFacilityId())) {
				if(link.getKey().getFacilityId() == targetFacilityId) {
					return link.getKey();
				}
			}
		}
		return null;
	}

	@Override
	public GameRoundAction executeTurn(Round round) {
		return generateRoundActions(round);
	}

	@Override
	public Facility getParticipant() {
		return getFacility();
	}
}
