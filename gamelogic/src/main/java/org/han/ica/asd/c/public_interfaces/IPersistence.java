package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.model.FacilityLinkedTo;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.han.ica.asd.c.model.Beergame;

public interface IPersistence {
    void saveRoundData(Round roundData);
    Round fetchRoundData(String gameId, int roundId);

    void saveTurnData(FacilityTurn turn);
    FacilityTurn fetchTurnData(Round round, FacilityLinkedTo facility);

    Beergame getGameLog(String gameId);

    void logUsedBusinessRuleToCreateOrder(int facilityID, String gameId, FacilityLinkedTo facility,
                                          String businessRuleString, int outGoingOrderAmount);
}
