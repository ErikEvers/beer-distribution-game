package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.model.dao_model.BeerGameDB;
import org.han.ica.asd.c.model.dao_model.FacilityLinkedToDB;
import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesInFacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.RoundDB;
import org.han.ica.asd.c.public_interfaces.IPersistence;

import javax.inject.Inject;


public class Persistence implements IPersistence {

	@Inject
	private RoundDAO roundDAO;

	@Inject
	private BeergameDAO beergameDAO;

	@Inject
	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnDAO;

	@Inject
	private FacilityTurnDAO facilityTurnDAO;


	public Persistence(){
		//Empty constructor for GUICE
	}

	public void saveRoundData(RoundDB rounddata)
	{
		roundDAO.createRound(rounddata.getGameId(), rounddata.getRoundId());
	}

	public RoundDB fetchRoundData(String gameId, int roundId)
	{
		return roundDAO.getRound(gameId,roundId);
	}

	public BeerGameDB getGameLog(String gameId)
	{
		return beergameDAO.getGameLog(gameId);
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn)
	{
		gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);
	}

	public FacilityTurnDB fetchTurnData(RoundDB round, FacilityLinkedToDB facility)
	{
		return facilityTurnDAO.fetchTurn(round,facility);
	}

	public void saveTurnData(FacilityTurnDB turn)
	{
		facilityTurnDAO.createTurn(turn);
	}
}
