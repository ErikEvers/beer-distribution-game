package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.model.dao_model.BeerGame;
import org.han.ica.asd.c.model.dao_model.FacilityLinkedTo;
import org.han.ica.asd.c.model.dao_model.FacilityTurn;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.dao_model.Round;
import org.han.ica.asd.c.public_interfaces.IPersistence;

import javax.inject.Inject;
import javax.inject.Named;


public class Persistence implements IPersistence {

	@Inject
	@Named("RoundDAO")
	private IBeerDisitributionGameDAO roundDAO;

	@Inject
	@Named("BeergameDAO")
	private IBeerDisitributionGameDAO beergameDAO;

	@Inject
	@Named("GameBusinessRulesRulesInFacilityTurnDAO")
	private IBeerDisitributionGameDAO gameBusinessRulesInFacilityTurnDAO;

	@Inject
	@Named("FacilityTurnDAO")
	private IBeerDisitributionGameDAO facilityTurnDAO;


	public Persistence(){
		//Empty constructor for GUICE
	}

	public void saveRoundData(Round rounddata)
	{
		((RoundDAO)roundDAO).createRound(rounddata.getGameId(), rounddata.getRoundId());
	}

	public Round fetchRoundData(String gameId, int roundId)
	{
		return ((RoundDAO)roundDAO).getRound(gameId,roundId);
	}

	public BeerGame getGameLog(String gameId)
	{
		return ((BeergameDAO)beergameDAO).getGameLog(gameId);
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn)
	{
		((GameBusinessRulesInFacilityTurnDAO)gameBusinessRulesInFacilityTurnDAO).createTurn(gameBusinessRulesInFacilityTurn);
	}

	public FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility)
	{
		return ((FacilityTurnDAO)facilityTurnDAO).fetchTurn(round,facility);
	}

	public void saveTurnData(FacilityTurn turn)
	{
		((FacilityTurnDAO)facilityTurnDAO).createTurn(turn);
	}
}
