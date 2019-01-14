package org.han.ica.asd.c.interfaces.gameleader;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Round;

/**
 * interface for persistence component
 * which is responsible for storing and fetching data to and from a persistent data store.
 */
public interface IPersistence {

    void saveGameLog(BeerGame beerGame, boolean isStarted);

    /**
     * Store the actions of a facility in a specific round.
     * @param data, the info to be saved.
     */
    void saveFacilityTurn(Round data);

    /**
     * Retrieve the data of a turn of a facility.
     * @param roundId, the identifier of the turn to retrieve.
     * @param facilityId, the identifier of the facility of which the round actions are to be retrieved.
     * @return the round data for that facility in that round.
     */
    Round fetchFacilityTurn(int roundId);

    /**
     * Store the actions of a specific round.
     * @param data, the info to be saved.
     */
    void saveRoundData(Round data);

    /**
     * The data of a specific round gets fetched by its id.
     * @param roundId, the id of the round that has to be fetched.
     * @return the round data at that round.
     */
    Round fetchRoundData(int roundId);

    void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn);

}
