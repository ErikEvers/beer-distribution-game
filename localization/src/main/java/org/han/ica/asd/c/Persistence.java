package org.han.ica.asd.c;

public class Persistence implements IPersistence {
	private BeerDistributionGameDAO beergameDAO;

	public void saveRoundData(Round rounddata)
	{
		if(!(beergameDAO instanceof RoundDAO)){
			beergameDAO = new RoundDAO();
		}

		beergameDAO.createRound(rounddata.getGameId(), rounddata.getRoundId());

	}

	public Round fetchRoundData(String gameId, int roundId)
	{
		if(!(beergameDAO instanceof RoundDAO)){
			beergameDAO = new RoundDAO();
		}
		return beergameDAO.getRound(gameId,roundId);

	}

	public Beergame getGameLog(String gameId)
	{
		if(!(beergameDAO instanceof BeerGameDAO)){
			beergameDAO = new BeerGameDAO();
		}
		return beergameDAO.getGameLog(gameId);
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
