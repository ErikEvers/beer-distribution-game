package org.han.ica.asd.c.interfaces.replay;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Round;

/**
 * Small interface to retrieve game and/or round data
 */
public interface IRetrieveReplayData {
    /**
     * @param gameId
     * Primary identifier to retrieve a game
     * @return
     * Returns a BeerGame object for a full replay capability
     */
    BeerGame retrieveGameData(String gameId);

    /**
     * @param gameId
     * Primary identifier to retrieve a game
     * @param roundId
     * Specify the round for which to retrieve the data
     * @return
     * Returns a single round for in-game replays
     */
    Round retrieveSingleRoundData(String gameId, int roundId);
}
