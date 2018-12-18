package org.han.ica.asd.c;

import org.han.ica.asd.c.model.*;
import org.han.ica.asd.c.public_interfaces.IPersistence;

import javax.inject.Inject;
import javax.inject.Provider;

public class Persistence implements IPersistence {
	private IBeerDisitributionGameDAO beergameDAO;
	private final Provider<RoundDAO> roundDAOProvider;
	private final Provider<BeergameDAO> beergameDAOProvider;
	private final Provider<GameBusinessRulesInFacilityTurnDAO> businessRulesInFacilityTurnDAOProvider;
	private final Provider<FacilityTurnDAO> facilityTurnDAOProvider;

	@Inject
	public Persistence(Provider<RoundDAO> roundDAOProvider,
					   Provider<BeergameDAO> beergameDAOProvider,
					   Provider<GameBusinessRulesInFacilityTurnDAO> businessRulesInFacilityTurnDAOProvider,
					   Provider<FacilityTurnDAO> facilityTurnDAOProvider) {

		this.roundDAOProvider = roundDAOProvider;
		this.beergameDAOProvider = beergameDAOProvider;
		this.businessRulesInFacilityTurnDAOProvider = businessRulesInFacilityTurnDAOProvider;
		this.facilityTurnDAOProvider = facilityTurnDAOProvider;
	}

	public void saveRoundData(Round rounddata)
	{
		if(!(beergameDAO instanceof RoundDAO)) {
			beergameDAO = roundDAOProvider.get();
		}
		((RoundDAO) beergameDAO).createRound(rounddata.getGameId(), rounddata.getRoundId());
	}

	public Round fetchRoundData(String gameId, int roundId)
	{
		if(!(beergameDAO instanceof RoundDAO)){
			beergameDAO = roundDAOProvider.get();
		}
		return ((RoundDAO) beergameDAO).getRound(gameId,roundId);

	}

	public BeerGame getGameLog(String gameId)
	{
		if(!(beergameDAO instanceof BeergameDAO)){
			beergameDAO = beergameDAOProvider.get();
		}
		return ((BeergameDAO)beergameDAO).getGameLog(gameId);
	}

	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn)
	{
		if(!(beergameDAO instanceof GameBusinessRulesInFacilityTurnDAO)){
			beergameDAO = businessRulesInFacilityTurnDAOProvider.get();
		}
		((GameBusinessRulesInFacilityTurnDAO)beergameDAO).createTurn(gameBusinessRulesInFacilityTurn);

	}

	public FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility)
	{
		if(!(beergameDAO instanceof FacilityTurnDAO)){
			beergameDAO = facilityTurnDAOProvider.get();
		}
		return ((FacilityTurnDAO)beergameDAO).fetchTurn(round,facility);
	}

	public void saveTurnData(FacilityTurn turn)
	{
		if(!(beergameDAO instanceof FacilityTurnDAO)){
			beergameDAO = facilityTurnDAOProvider.get();
		}

		((FacilityTurnDAO)beergameDAO).createTurn(turn);
	}
}
