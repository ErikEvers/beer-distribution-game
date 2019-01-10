package org.han.ica.asd.c.gameleader.testutil;

import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Round;

public class PersistenceStub implements IPersistence {

	@Override
	public void saveGameLog(BeerGame beerGame) {

	}

	@Override
	public void saveFacilityTurn(Round data) {

	}

	@Override
	public Round fetchFacilityTurn(int roundId) {
		return null;
	}

	@Override
	public void saveRoundData(Round data) {

	}

	@Override
	public Round fetchRoundData(int roundId) {
		return null;
	}

	@Override
	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {

	}
}
