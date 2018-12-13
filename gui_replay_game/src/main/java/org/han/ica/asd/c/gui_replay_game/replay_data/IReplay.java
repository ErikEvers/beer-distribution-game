package org.han.ica.asd.c.gui_replay_game.replay_data;

import org.han.ica.asd.c.model.Round;

import java.util.List;

public interface IReplay {
    List<Round> showGraphOverview(String gameId);
    //TODO: implementeer showGraphDetails() Onbekend wat de "details" nou zijn
    List<Round> showRoundGraph(String gameId, int roundId);
    List<Round> stepForward();
    List<Round> stepBackwards();
}
