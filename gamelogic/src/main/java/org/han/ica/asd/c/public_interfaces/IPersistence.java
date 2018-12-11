package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.domain.*;

public interface IPersistence {
    void saveRoundData(Round roundData);
    RoundModel fetchRoundData(String gameId, int roundId);

    void saveTurnData(FacilityTurn turn);
    Turn fetchTurnData(Round round, FacilityLinkedTo facility);

    BeerGameModel getGameLog(String gameId);

    void logUsedBusinessRuleToCreateOrder(int facilityID, String gameId, FacilityLinkedTo facility,
                                          String businessRuleString, int outGoingOrderAmount);
}
