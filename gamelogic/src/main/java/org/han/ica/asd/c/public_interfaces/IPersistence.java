package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.model.*;

public interface IPersistence {
    void saveRoundData(Round roundData);
    Round fetchRoundData(String gameId, int roundId);

    void saveTurnData(FacilityTurn turn);
    FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility);

    BeerGame getGameLog(String gameId);

    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);
}