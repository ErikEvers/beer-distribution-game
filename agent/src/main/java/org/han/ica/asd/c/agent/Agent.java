package org.han.ica.asd.c.agent;

import org.han.ica.asd.c.businessrule.parser.replacer.NodeConverter;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.GameRoundAction;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Agent extends GameAgent implements IParticipant {
	private static final Logger LOGGER = Logger.getLogger(Agent.class.getName());
	private Configuration configuration;

	@Inject
	private IBusinessRules businessRules;

	@Inject
	private IPlayerGameLogic gameLogic;

	@Inject
	private IPersistence persistence;

	public Agent(){

	}

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
	 * @param beerGame  The round data of last round used to determine what actions the agent is going to do.
	 * @return          A GameRoundAction with all actions that the agent wants to do.
	 */
	private GameRoundAction generateRoundActions(BeerGame beerGame, int roundId) {
		Round round = beerGame.getRoundById(roundId);
		ActionCollector actionCollector = new ActionCollector();
		Iterator<GameBusinessRules> gameBusinessRulesIterator = this.getGameBusinessRules().iterator();

		while (actionCollector.hasBothActionTypes() && gameBusinessRulesIterator.hasNext()) {
			GameBusinessRules gameBusinessRules = gameBusinessRulesIterator.next();
			ActionModel actionModel = businessRules.evaluateBusinessRule(gameBusinessRules.getGameAST(), round, getFacility().getFacilityId());
			updateActionCollector(actionCollector, actionModel, gameBusinessRules);
		}

		persistence.logUsedBusinessRuleToCreateOrder(new GameBusinessRulesInFacilityTurn(
				getFacility().getFacilityId(),
				round.getRoundId(),
				getGameAgentName(),
				actionCollector.businessRulesList));
		return new GameRoundAction(actionCollector.orderMap, actionCollector.deliverMap);
	}

	/**
	 * Update actionCollector with triggered gameBusinessRule and deliver or order action. Preventing NULL values to be used as keys
	 * @param actionCollector			Action collector is the model that keeps all triggered actions and business rules
	 * @param actionModel            	Action model containing the information of the action
	 * @param gameBusinessRules         The business rule that is triggered
	 */
	private void updateActionCollector(ActionCollector actionCollector, ActionModel actionModel, GameBusinessRules gameBusinessRules) {
		try {
			if (actionModel != null) {
				if (actionCollector.needsOrderAction(actionModel)) {
					actionCollector.addOrderAction(this.resolveLowerFacilityId(actionModel.facilityId), actionModel, gameBusinessRules);
				} else if (actionCollector.needsDeliverAction(actionModel)) {
					actionCollector.addDeliverAction(this.resolveHigherFacilityId(actionModel.facilityId), actionModel, gameBusinessRules);
				}
			}
		} catch (FacilityNotFound exception) {
			LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
		}
	}

	/**
	 * Returns the facility of the identifying integer. When the facility is not found, it'll return NULL.
	 *
	 * ORDER
	 *
	 * @param   targetFacilityId The identifying integer of the facility that needs to be resolved
	 * @return  The facility below the current facility that needs to be resolved. NULL when facility is not found.
	 */
	private Facility resolveLowerFacilityId(int targetFacilityId) throws FacilityNotFound {
		if(getFacility().getFacilityId() == targetFacilityId)
			return getFacility();

		Facility facility = getFacility();
		Optional<Map.Entry<Facility, List<Facility>>> value = configuration.getFacilitiesLinkedTo().entrySet().stream()
				.filter(m -> m.getKey().getFacilityId() == facility.getFacilityId()).findFirst();

		List<Facility> links;
		if (value.isPresent()) {
			links = value.get().getValue();
		} else {
			links = new ArrayList<>();
		}

		if(targetFacilityId == NodeConverter.FIRST_FACILITY_ABOVE_BELOW){
            Collections.sort(links);
            return links.get(0);
        }

		for (Facility link : links) {
			if (targetFacilityId == link.getFacilityId()) {
				return link;
			}
		}

		throw new FacilityNotFound(targetFacilityId);
	}

	private boolean entryContainsFacilityInValue(Map.Entry<Facility, List<Facility>> entry){
	    Facility facility = getFacility();

	    List<Facility> list = entry.getValue();

	    for(Facility f : list){
	        if (f.getFacilityId() == facility.getFacilityId()){
	            return true;
            }
        }
	    return false;
    }

	/**
	 * Returns the facility of the identifying integer. When the facility is not found, it'll return NULL.
	 *
	 * DELIVER
	 *
	 * @param   targetFacilityId The identifying integer of the facility that needs to be resolved
	 * @return  The facility above the current facility that needs to be resolved. NULL when facility is not found.
	 */
	private Facility resolveHigherFacilityId(int targetFacilityId) throws FacilityNotFound {
		if (targetFacilityId == NodeConverter.FIRST_FACILITY_ABOVE_BELOW){
		    List<Map.Entry<Facility, List<Facility>>> entryList = new ArrayList<>(configuration.getFacilitiesLinkedTo().entrySet());

		    List<Facility> list = entryList.stream()
                    .filter(this::entryContainsFacilityInValue)
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());

			if (!list.isEmpty()) {
				return list.get(0);
			}
		} else {
		    Facility facility = getFacility();
		    if (facility.getFacilityId() == targetFacilityId){
		        return facility;
            }
			for (Map.Entry<Facility, List<Facility>> link : configuration.getFacilitiesLinkedTo().entrySet()) {
				if (link.getValue().stream().anyMatch(f -> f.getFacilityId() == facility.getFacilityId()) && link.getKey().getFacilityId() == targetFacilityId) {
					return link.getKey();
				}
			}
		}
		throw new FacilityNotFound(targetFacilityId);
	}

    @Override
    public GameRoundAction executeTurn() {
        return generateRoundActions(gameLogic.getBeerGame(), gameLogic.getRoundId());
    }

	@Override
    public Facility getParticipant() {
        return getFacility();
    }

    public void setConfiguration(Configuration configuration) {
	    this.configuration = configuration;
    }
}