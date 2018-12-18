package org.han.ica.asd.c.dao_model;

import org.han.ica.asd.c.model.Configuration;
import org.han.ica.asd.c.model.Round;

import java.util.ArrayList;
import java.util.List;

public class BeerGame {
    private String gameId;
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private List<org.han.ica.asd.c.model.Round> rounds;
    private org.han.ica.asd.c.model.Configuration configuration;

    public BeerGame(String gameId, String gameName, String gameDate, String gameEndDate) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
        this.rounds = new ArrayList<>();
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

    public List<org.han.ica.asd.c.model.Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<org.han.ica.asd.c.model.Round> rounds) {
        this.rounds = rounds;
    }

    public void addRound(Round round){
        rounds.add(round);
    }

    public org.han.ica.asd.c.model.Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
