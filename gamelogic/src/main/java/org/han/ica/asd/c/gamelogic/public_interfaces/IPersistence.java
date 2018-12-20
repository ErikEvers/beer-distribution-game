package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.model.dao_model.BeerGameDB;
import org.han.ica.asd.c.model.dao_model.FacilityLinkedToDB;
import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesInFacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.RoundDB;

public interface IPersistence {
    void saveRoundData(RoundDB roundData);
    RoundDB fetchRoundData(String gameId, int roundId);

    void saveTurnData(FacilityTurnDB turn);
    FacilityTurnDB fetchTurnData(RoundDB round, FacilityLinkedToDB facility);

    BeerGameDB getGameLog(String gameId);

    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn);

}