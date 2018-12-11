package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.domain.*;

public interface IPersistence {
    void saveRoundData(Round roundData);
    RoundModel fetchRoundData(String gameName, String gameDate, String gameEndDate);

    void saveTurnData(FacilityTurn turn);
    Turn fetchTurnData(Round round, FacilityLinkedTo facility);

    BeerGameModel getGameLog(String gameID);

    void logUsedBusinessRuleToCreateOrder(int facilityID, String gameID, FacilityLinkedTo facility,
                                          String businessRuleString, int outGoingOrderAmount);
}
