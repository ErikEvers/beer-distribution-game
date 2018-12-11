package org.han.ica.asd.c.model;

import java.util.ArrayList;

public class Beergame {
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private ArrayList<Round> rounds;

    public Beergame(String gameName, String gameDate, String gameEndDate) {
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
        this.rounds = new ArrayList<>();
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

    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }

    public void addRound(Round round){
        rounds.add(round);
    }
}
