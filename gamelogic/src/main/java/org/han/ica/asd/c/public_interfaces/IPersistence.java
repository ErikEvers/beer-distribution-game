package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.dao_model.BeerGame;
import org.han.ica.asd.c.dao_model.FacilityLinkedTo;
import org.han.ica.asd.c.dao_model.FacilityTurn;
import org.han.ica.asd.c.dao_model.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.dao_model.Round;

public interface IPersistence {
    void saveRoundData(Round roundData);
    Round fetchRoundData(String gameId, int roundId);

    void saveTurnData(FacilityTurn turn);
    FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility);

    BeerGame getGameLog(String gameId);

    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);
}