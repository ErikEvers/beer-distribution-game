package org.han.ica.asd.c.gui_replay_game.data;

import org.han.ica.asd.c.model.Beergame;
import org.han.ica.asd.c.model.Round;

/**
 * Small interface to retrieve game and/or round data
 */
public interface IRetrieveReplayData {
    /**
     * @param gameID
     * Primary identifier to retrieve a game
     * @return
     * Returns a Beergame object for a full replay capability
     */
    Beergame retrieveGameData(String gameID);

    /**
     * @param gameID
     * Primary identifier to retrieve a game
     * @return
     * Returns a single round for in-game replays
     */
    Round retrieveSingleRoundData(String gameID, int roundID);
}
