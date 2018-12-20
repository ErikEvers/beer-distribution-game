package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.model.domain_objects.*;

public interface IPersistence {
    void saveRoundData(Round roundData);
    Round fetchRoundData(String gameId, int roundId);
    void saveTurnData(Round turn);
    Round fetchTurnData(Round round, FacilityLinkedTo facility);
    BeerGame getGameLog(String gameId);
    Player getPlayerById(String playerId);
    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);
}