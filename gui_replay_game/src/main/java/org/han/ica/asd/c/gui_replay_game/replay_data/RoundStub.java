package org.han.ica.asd.c.gui_replay_game.replay_data;

public class RoundStub {
    private int roundId;
    private int budget;

    public RoundStub(int roundId, int budget) {
        this.roundId = roundId;
        this.budget = budget;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
}
