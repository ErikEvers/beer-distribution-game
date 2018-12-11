package org.han.ica.asd.c.data;

import org.han.ica.asd.c.model.Beergame;
import org.han.ica.asd.c.model.Round;

/**
 * Small interface to retrieve game and/or round data
 */
public interface IRetrieveReplayData {
    /**
     * @param gameId
     * Primary identifier to retrieve a game
     * @return
     * Returns a Beergame object for a full replay capability
     */
    Beergame retrieveGameData(String gameId);

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
