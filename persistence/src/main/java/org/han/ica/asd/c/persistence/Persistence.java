package org.han.ica.asd.c.persistence;


import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.interfaces.gamelogic.IPersistence;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import java.util.Map;


public class Persistence implements IPersistence {

	@Inject
	private RoundDAO roundDAO;

	@Inject
	private BeergameDAO beergameDAO;

	@Inject
	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnDAO;


	public Persistence(){
		//Empty constructor for Guice
	}

	public void saveRoundData(Round rounddata)
	{
		roundDAO.createRound(rounddata.getRoundId());
	}

	@Override
	public Round fetchRoundData(String gameId, int roundId) {
		return roundDAO.getRound(roundId);
	}

	@Override
	public Round fetchTurnData(Round round, Map<Facility, Facility> facilityLinkedTo) {
		return roundDAO.getRound(round.getRoundId());
	}

	public BeerGame getGameLog(String gameId)
	{
		return beergameDAO.getGameLog();
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn)
	{
		gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);
	}


	@Override
	public Player getPlayerById(String playerId) {
		//stub
		return null;
	}


	public void saveTurnData(Round round)
	{
		roundDAO.createRound(round.getRoundId());
		for (FacilityTurn facilityTurn: round.getFacilityTurns()) {
			roundDAO.createFacilityTurn(round.getRoundId(),facilityTurn);
		}

		for (FacilityTurnOrder facilityTurnOrder: round.getFacilityOrders()) {
			roundDAO.createFacilityOrder(round.getRoundId(),facilityTurnOrder);
		}

		for (FacilityTurnDeliver facilityTurnDeliver: round.getFacilityTurnDelivers()) {
			roundDAO.createFacilityDeliver(round.getRoundId(),facilityTurnDeliver);
		}
	}
}
