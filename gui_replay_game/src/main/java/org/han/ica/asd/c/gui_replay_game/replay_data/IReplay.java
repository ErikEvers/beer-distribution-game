package org.han.ica.asd.c.gui_replay_game.replay_data;

import org.han.ica.asd.c.model.Round;

import java.util.List;

public interface IReplay {
    /**
     * Request data for total graph overview for a game
     * @param gameId
     * Primary identifier to locate a given game.
     * @return
     * A List of Rounds which contain the data for each facility.
     */
    List<Round> showGraphOverview(String gameId);

    /**
     * Request data for a graph overview up to a specified round
     * @param gameId
     * Primary identifier to locate a given game.
     * @param roundId
     * Primary identifier to locate a round within a game
     * @return
     * All Round data up to the given roundId where the index of the List corresponds with the round
     */
    List<Round> showRoundGraph(String gameId, int roundId);
}
