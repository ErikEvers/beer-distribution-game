import org.han.ica.asd.c.*;
import org.han.ica.asd.c.model.*;
import org.han.ica.asd.c.public_interfaces.IPersistence;

public class Persistence implements IPersistence {
	private IBeerDisitributionGameDAO beergameDAO;

	public void saveRoundData(Round rounddata)
	{
		if(!(beergameDAO instanceof RoundDAO)) {
			beergameDAO = new RoundDAO();
		}
		((RoundDAO) beergameDAO).createRound(rounddata.getGameId(), rounddata.getRoundId());
	}

	public Round fetchRoundData(String gameId, int roundId)
	{
		if(!(beergameDAO instanceof RoundDAO)){
			beergameDAO = new RoundDAO();
		}
		return ((RoundDAO) beergameDAO).getRound(gameId,roundId);

	}

	public BeerGame getGameLog(String gameId)
	{
		if(!(beergameDAO instanceof BeergameDAO)){
			beergameDAO = new BeergameDAO();
		}
		return ((BeergameDAO)beergameDAO).getGameLog(gameId);
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn)
	{
		if(!(beergameDAO instanceof GameBusinessRulesInFacilityTurnDAO)){
			beergameDAO = new GameBusinessRulesInFacilityTurnDAO();
		}
		((GameBusinessRulesInFacilityTurnDAO)beergameDAO).createTurn(gameBusinessRulesInFacilityTurn);

	}

	public FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility)
	{
		if(!(beergameDAO instanceof FacilityTurnDAO)){
			beergameDAO = new FacilityTurnDAO();
		}
		return ((FacilityTurnDAO)beergameDAO).fetchTurn(round,facility);
	}

	public void saveTurnData(FacilityTurn turn)
	{
		if(!(beergameDAO instanceof FacilityTurnDAO)){
			beergameDAO = new FacilityTurnDAO();
		}

		((FacilityTurnDAO)beergameDAO).createTurn(turn);

	}
}
