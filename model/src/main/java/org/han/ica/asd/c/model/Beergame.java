package org.han.ica.asd.c.model;

import java.util.ArrayList;

public class Beergame {
    private String gameId;
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private ArrayList<Round> rounds;
    private Configuration configuration;

    public Beergame(String gameId, String gameName, String gameDate, String gameEndDate, Configuration configuration) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
        this.rounds = new ArrayList<>();
        this.configuration = configuration;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
