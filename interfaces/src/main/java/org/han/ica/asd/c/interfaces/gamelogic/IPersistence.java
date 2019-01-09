package org.han.ica.asd.c.interfaces.gamelogic;

import org.han.ica.asd.c.model.domain_objects.*;

import java.util.Map;

public interface IPersistence {
    void saveRoundData(Round roundData);
    Round fetchRoundData(String gameId, int roundId);
    void saveTurnData(Round turn);
    Round fetchTurnData(Round round, Map<Facility, Facility> facilityLinkedTo);
    BeerGame getGameLog(String gameId);
    Player getPlayerById(String playerId);
    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);
    void saveSelectedAgent(ProgrammedAgent programmedAgent);
}