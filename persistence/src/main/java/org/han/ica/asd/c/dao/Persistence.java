package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.gamelogic.public_interfaces.IPersistence;
import org.han.ica.asd.c.model.dao_model.BeerGameDB;
import org.han.ica.asd.c.model.dao_model.FacilityLinkedToDB;
import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesInFacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.RoundDB;
import org.han.ica.asd.c.model.domain_objects.Player;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

	public void saveRoundData(RoundDB rounddata)
	{
		((RoundDAO)roundDAO).createRound(rounddata.getGameId(), rounddata.getRoundId());
	}

	public RoundDB fetchRoundData(String gameId, int roundId)
	{
		return ((RoundDAO)roundDAO).getRound(gameId,roundId);
	}

	public BeerGameDB getGameLog(String gameId)
	{
		return ((BeergameDAO)beergameDAO).getGameLog(gameId);
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn)
	{
		((GameBusinessRulesInFacilityTurnDAO)gameBusinessRulesInFacilityTurnDAO).createTurn(gameBusinessRulesInFacilityTurn);
	}

	public FacilityTurnDB fetchTurnData(RoundDB round, FacilityLinkedToDB facility)
	{
		return ((FacilityTurnDAO)facilityTurnDAO).fetchTurn(round,facility);
	}

	@Override
	public Player getPlayerById(String playerId) {
		throw new NotImplementedException();
	}

	public void saveTurnData(FacilityTurnDB turn)
	{
		((FacilityTurnDAO)facilityTurnDAO).createTurn(turn);
	}
}
