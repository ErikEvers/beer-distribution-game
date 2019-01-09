package org.han.ica.asd.c.agent;

import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.GameRoundAction;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;

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
	public Agent(Configuration configuration, String gameAgentName, Facility facility, List<GameBusinessRules> gameBusinessRulesList) {
        super(gameAgentName, facility, gameBusinessRulesList);
        this.configuration = configuration;
    }

	/**
	 * Generates actions of an agent using the defined business rules.
	 *
	 * @param round     The round data of last round used to determine what actions the agent is going to do.
	 * @return          A GameRoundAction with all actions that the agent wants to do.
	 */
	private GameRoundAction generateRoundActions(Round round) {
		Map<Facility, Integer> targetOrderMap = new HashMap<>();
		Map<Facility, Integer> targetDeliverMap = new HashMap<>();
		List<GameBusinessRules> triggeredBusinessRules = new ArrayList<>();
		Iterator<GameBusinessRules> gameBusinessRulesIterator = this.getGameBusinessRules().iterator();

		UnaryOperator<Boolean> canAddToOrderMap = isOrderType -> targetOrderMap.isEmpty() && isOrderType;
		UnaryOperator<Boolean> canAddToDeliverMap = isDeliverType -> targetDeliverMap.isEmpty() && isDeliverType;
		BooleanSupplier shouldIterate = () -> (targetDeliverMap.isEmpty() || targetOrderMap.isEmpty()) && gameBusinessRulesIterator.hasNext();

		while (shouldIterate.getAsBoolean()) {
			GameBusinessRules gameBusinessRules = gameBusinessRulesIterator.next();
			ActionModel actionModel = businessRules.evaluateBusinessRule(gameBusinessRules.getGameAST(), round, getFacility().getFacilityId());
			if (actionModel != null) {
				if (canAddToOrderMap.apply(actionModel.isOrderType())) {
					this.updateTargetMap(this.resolveLowerFacilityId(actionModel.facilityId), actionModel.amount, targetOrderMap, triggeredBusinessRules, gameBusinessRules);
				} else if (canAddToDeliverMap.apply(actionModel.isDeliverType())) {
					this.updateTargetMap(this.resolveHigherFacilityId(actionModel.facilityId), actionModel.amount, targetDeliverMap, triggeredBusinessRules, gameBusinessRules);
				}
			}
		}

		persistence.logUsedBusinessRuleToCreateOrder(
				new GameBusinessRulesInFacilityTurn(getFacility().getFacilityId(), round.getRoundId(), getGameAgentName() + 1, triggeredBusinessRules));
		return new GameRoundAction(targetOrderMap, targetDeliverMap);
	}

	/**
	 * Update targetMap with triggered gameBusinessRule. Preventing NULL values to be used as keys
	 * @param targetFacility            The facility that is targeted by the business rule
	 * @param amount                    The amount of the action
	 * @param targetMap                 The map the action will be stored in
	 * @param triggeredBusinessRules    A list of triggered business rules
	 * @param gameBusinessRules         The business rule that is triggered
	 */
	private void updateTargetMap(Facility targetFacility, int amount, Map<Facility, Integer> targetMap, List<GameBusinessRules> triggeredBusinessRules, GameBusinessRules gameBusinessRules) {
		if(targetFacility != null) {
			targetMap.put(targetFacility, amount);
			triggeredBusinessRules.add(gameBusinessRules);
		}
	}

	/**
	 * Returns the facility of the identifying integer. When the facility is not found, it'll return NULL.
	 *
	 * @param   targetFacilityId The identifying integer of the facility that needs to be resolved
	 * @return  The facility below the current facility that needs to be resolved. NULL when facility is not found.
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
	 * @param   targetFacilityId The identifying integer of the facility that needs to be resolved
	 * @return  The facility above the current facility that needs to be resolved. NULL when facility is not found.
	 */
	private Facility resolveHigherFacilityId(int targetFacilityId) {
		for (Map.Entry<Facility, List<Facility>> link : configuration.getFacilitiesLinkedTo().entrySet()) {
			if (link.getValue().stream().anyMatch(f -> f.getFacilityId() == getFacility().getFacilityId()) && link.getKey().getFacilityId() == targetFacilityId) {
				return link.getKey();
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