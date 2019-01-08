package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.interfaces.agent.GameStore;
import org.han.ica.asd.c.interfaces.agent.IBusinessRuleLogger;
import org.han.ica.asd.c.interfaces.gamelogic.IRoundStore;
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


public class Persistence implements IRoundStore, IBusinessRuleLogger, GameStore {

	@Inject
	private RoundDAO roundDAO;

	@Inject
	private BeergameDAO beergameDAO;

	@Inject
	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnDAO;




	public Persistence(){
		//stub
	}

	public void saveRoundData(RoundDB rounddata)
	{
		roundDAO.createRound(rounddata.getRoundId());
	}

	public void saveRoundData(Round rounddata) {
		//stub
	}

	@Override
	public Round fetchRoundData(int roundId)
	{
		//stub
		return null;
	}

	public BeerGame getGameLog(String gameId)
	{
		return null;
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn)
	{
		// empty for now
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
