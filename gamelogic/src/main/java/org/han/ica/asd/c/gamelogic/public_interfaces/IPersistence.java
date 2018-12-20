package org.han.ica.asd.c.gamelogic.public_interfaces;

<<<<<<< HEAD:gamelogic/src/main/java/org/han/ica/asd/c/gamelogic/public_interfaces/IPersistence.java

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
=======
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
>>>>>>> development:gamelogic/src/main/java/org/han/ica/asd/c/public_interfaces/IPersistence.java
}