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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
		//Empty constructor for GUICE
	}

//	public void saveRoundData(Round rounddata)
//	{
//		roundDAO.createRound(rounddata.getGameId(), rounddata.getRoundId());
//		//((RoundDAO)roundDAO).createRound(rounddata.getGameId(), rounddata.getRoundId());
//	}

	public void saveRoundData(Round rounddata) {
	    // empty for stub
    }

	public Round fetchRoundData(String gameId, int roundId)
	{
		return null;
		// ((RoundDAO)roundDAO).getRound(gameId,roundId);
	}

	@Override
	public Round fetchTurnData(Round round, Map<Facility, Facility> facilityLinkedTo) {
		return null;

	}

	public BeerGame getGameLog(String gameId)
	{

		return null;//((BeergameDAO)beergameDAO).getGameLog(gameId);
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn)
	{
		gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);
	}

	public Round fetchTurnData(Round round, Facility facility)
	{
		return null; //return ((FacilityTurnDAO)facilityTurnDAO).fetchTurn(round,facility);
	}

    public Round fetchTurnData(RoundDB round, FacilityLinkedToDB facility)
    {
        return null; //return ((FacilityTurnDAO)facilityTurnDAO).fetchTurn(round,facility);
    }

	@Override
	public Player getPlayerById(String playerId) {
		throw new NotImplementedException();
	}

	@Override
	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {

	}

	public void saveTurnData(Round turn)
	{
		//((FacilityTurnDAO)facilityTurnDAO).createTurn(turn);
	}

    public void saveTurnData(FacilityTurnDB turn)
    {
        // for stub
    }
}
