package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.model.*;

import java.util.Map;

public interface IPersistence {
    void saveRoundData(Round roundData);

    Round fetchRoundData(String gameId, int roundId);

    void saveTurnData(Map<Facility, Map<Facility, Integer>> turn);

    FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility);

    BeerGame getGameLog(String gameId);

    void logUsedBusinessRuleToCreateOrder(int facilityID, String gameId, FacilityLinkedTo facility,
                                          String businessRuleString, int outGoingOrderAmount);

    Player getPlayerById(String playerId);
}
