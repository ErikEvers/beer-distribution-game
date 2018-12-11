package org.han.ica.asd.c.model;

import java.util.ArrayList;

public class Round {
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private int roundId;
    private ArrayList<FacilityTurn> turns;

    public Round(String gameName, String gameDate, String gameEndDate, int roundId) {
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
        this.roundId = roundId;
        turns = new ArrayList<>();
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameEndDate() {
        return gameEndDate;
    }

    public void setGameEndDate(String gameEndDate) {
        this.gameEndDate = gameEndDate;
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
