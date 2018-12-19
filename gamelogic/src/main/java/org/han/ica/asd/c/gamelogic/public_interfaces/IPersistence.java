package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.model.domain_objects.*;

import java.util.Map;

public interface IPersistence {
    void saveRoundData(Round roundData);
    Round fetchRoundData(String gameId, int roundId);
    void saveTurnData(Map<Facility, Map<Facility, Integer>> turn);
    Round fetchTurnData(Round round, FacilityLinkedTo facility);
    BeerGame getGameLog(String gameId);
    Player getPlayerById(String playerId);
    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);
}