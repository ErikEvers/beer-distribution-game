package org.han.ica.asd.c;

public class Persistence implements IPersistence {
	BeerDistributionGameDAO beergameDAO;

	public void saveRoundData(Round rounddata)
	{
		if(!beergameDAO instanceof(RoundDAO)){
			beergameDAO = new RoundDAO();
		}

		beergameDAO.createRound(rounddata.getGameId(), rounddata.getRoundId());

	}

	public Round fetchRoundData(String gameID)
	{


	}

	public Beergame getGameLog(String gameID)
	{

	}

	public void logUsedBusinessRuleToCreateOrder(int facilityID, String gameID,String businessRule,
												 FacilityLinkedTo facility, int outGoingOrderAmount)
	{


	}

	public FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility)
	{

	}

	public void saveTurnData(FacilityTurn turn)
	{


	}
}
