package org.han.ica.asd.c.interfaces.gamelogic;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.Map;

public interface IPersistence {
    void saveRoundData(Round roundData);
    Round fetchRoundData(int roundId);
    void saveTurnData(Round turn);
    BeerGame getGameLog(String gameId);
    Player getPlayerById(String playerId);
    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);
}