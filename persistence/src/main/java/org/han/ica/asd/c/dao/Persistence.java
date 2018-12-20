package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.interfaces.gamelogic.IPersistence;
import org.han.ica.asd.c.model.dao_model.BeerGameDB;
import org.han.ica.asd.c.model.dao_model.FacilityLinkedToDB;
import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesInFacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.RoundDB;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityLinkedTo;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
<<<<<<< HEAD
		roundDAO.createRound(rounddata.getGameId(), rounddata.getRoundId());
=======
		//((RoundDAO)roundDAO).createRound(rounddata.getGameId(), rounddata.getRoundId());
>>>>>>> team3
	}

	public void saveRoundData(Round rounddata) {
	    // empty for stub
    }

	public Round fetchRoundData(String gameId, int roundId)
	{
<<<<<<< HEAD
		return roundDAO.getRound(gameId,roundId);
=======
		return null;
		// ((RoundDAO)roundDAO).getRound(gameId,roundId);
	}

	@Override
	public Round fetchTurnData(Round round, FacilityLinkedTo facility) {
		return null;
>>>>>>> team3
	}

	public BeerGame getGameLog(String gameId)
	{
<<<<<<< HEAD
		return beergameDAO.getGameLog(gameId);
=======
		return null;//((BeergameDAO)beergameDAO).getGameLog(gameId);
>>>>>>> team3
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn)
	{
		gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);
	}

	public Round fetchTurnData(Round round, Facility facility)
	{
<<<<<<< HEAD
		return facilityTurnDAO.fetchTurn(round,facility);
=======
		return null; //return ((FacilityTurnDAO)facilityTurnDAO).fetchTurn(round,facility);
	}

    public Round fetchTurnData(RoundDB round, FacilityLinkedToDB facility)
    {
        return null; //return ((FacilityTurnDAO)facilityTurnDAO).fetchTurn(round,facility);
    }

	@Override
	public Player getPlayerById(String playerId) {
		throw new NotImplementedException();
>>>>>>> team3
	}

	@Override
	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {

	}

	public void saveTurnData(Round turn)
	{
<<<<<<< HEAD
		facilityTurnDAO.createTurn(turn);
=======
		//((FacilityTurnDAO)facilityTurnDAO).createTurn(turn);
>>>>>>> team3
	}

    public void saveTurnData(FacilityTurnDB turn)
    {
        // for stub
    }
}
