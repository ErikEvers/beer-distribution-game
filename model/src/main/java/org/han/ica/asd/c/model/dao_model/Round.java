package org.han.ica.asd.c.model.dao_model;

import java.util.ArrayList;
import java.util.List;

public class Round implements IDaoModel{
    private String gameId;
    private int roundId;
    private List<FacilityTurn> turns;

    public Round(String gameId, int roundId) {
        this.gameId = gameId;
        this.roundId = roundId;
        turns = new ArrayList<>();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public List<FacilityTurn> getTurns() {
        return turns;
    }

    public void setTurns(List<FacilityTurn> turns) {
        this.turns = turns;
    }

    public void addTurn(FacilityTurn turn){
        turns.add(turn);
    }
}
