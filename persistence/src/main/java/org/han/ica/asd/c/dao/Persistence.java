package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.interfaces.gamelogic.IPersistence;
import org.han.ica.asd.c.model.dao_model.FacilityLinkedToDB;
import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesInFacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.RoundDB;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
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
		//stub
	}

//	public void saveRoundData(Round rounddata)
//	{
//		roundDAO.createRound(rounddata.getGameId(), rounddata.getRoundId());
//		//((RoundDAO)roundDAO).createRound(rounddata.getGameId(), rounddata.getRoundId());
//	}


	public void saveRoundData(Round rounddata) {
		//stub
	}

	public Round fetchRoundData(String gameId, int roundId)
	{
		//stub
		return null;
	}

	@Override
	public Round fetchTurnData(Round round, Map<Facility, Facility> facilityLinkedTo) {
		return null;

	}

	public BeerGame getGameLog(String gameId)
	{

		return null; //((BeergameDAO)beergameDAO).getGameLog(gameId);
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn)
	{
		//gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);
	}

	public Round fetchTurnData(Round round, Facility facility)
	{
		//stub
		return null;
	}

	public Round fetchTurnData(RoundDB round, FacilityLinkedToDB facility)
	{
		//stub
		return null;
	}

	@Override
	public Player getPlayerById(String playerId) {
		//stub
		return null;
	}

	@Override
	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
		//stub
	}

	public void saveTurnData(Round turn)
	{
		//stub
	}

	public void saveTurnData(FacilityTurnDB turn)
	{
		//for stub
	}
}
