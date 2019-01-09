package org.han.ica.asd.c.model.dao_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class RoundDB implements IDaoModel{
    private String gameId;
    private int roundId;
    private List<FacilityTurnDB> turns;

    public RoundDB(String gameId, int roundId) {
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

    public List<FacilityTurnDB> getTurns() {
        return turns;
    }

    public void setTurns(List<FacilityTurnDB> turns) {
        this.turns = turns;
    }

    public void addTurn(FacilityTurnDB turn){
        turns.add(turn);
    }

}
