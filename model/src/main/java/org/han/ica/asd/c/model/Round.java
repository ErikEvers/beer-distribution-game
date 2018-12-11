package org.han.ica.asd.c.model;

import java.util.ArrayList;

public class Round {
    private String gameId;
    private int roundId;
    private ArrayList<FacilityTurn> turns;

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

    public ArrayList<FacilityTurn> getTurns() {
        return turns;
    }

    public void setTurns(ArrayList<FacilityTurn> turns) {
        this.turns = turns;
    }

    public void addTurn(FacilityTurn turn){
        turns.add(turn);
    }
}
