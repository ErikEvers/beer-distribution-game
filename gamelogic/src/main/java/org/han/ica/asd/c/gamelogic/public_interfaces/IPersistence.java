package org.han.ica.asd.c.gamelogic.public_interfaces;


import org.han.ica.asd.c.model.*;
import org.han.ica.asd.c.model.dao_model.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.*;

import java.util.Map;

public interface IPersistence {
    void saveRoundData(Round roundData);

    Round fetchRoundData(String gameId, int roundId);

    void saveTurnData(Map<Facility, Map<Facility, Integer>> turn);

    FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility);

    BeerGame getGameLog(String gameId);

    Player getPlayerById(String playerId);
    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);
}