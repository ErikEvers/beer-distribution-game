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

	public void logUsedBusinessRuleToCreateOrder(int roundId, String gameID, String businessRule,
												 FacilityLinkedTo facility)
	{
		if(!(beergameDAO instanceof FacilityTurn_GameBusinessRules)){
			beergameDAO = new FacilityTurn_GameBusinessRules();
		}
		beergameDAO.createRT_GameBusinessRules_Turn(roundId,gameID,facility.FacilityIdDeliver, facility.FacilityIdOrder, businessRule);

	}

	public FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility)
	{

	}

	public void saveTurnData(FacilityTurn turn)
	{


	}
}
