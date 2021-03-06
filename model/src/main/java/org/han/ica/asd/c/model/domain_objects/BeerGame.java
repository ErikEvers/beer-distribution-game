package org.han.ica.asd.c.model.domain_objects;

import java.util.List;

public class BeerGame {

    private Leader leader;
    private List<Player> players;
    private List<GameAgent> agents;
    private Configuration configuration;
    private List<Round> rounds;
    private String gameId;
    private String gameName;
    private String gameDate;
    private String gameEndDate;


    public BeerGame(Leader leader, List<Player> players, List<GameAgent> agents, Configuration configuration, //NOSONAR
                    List<Round> rounds, String gameId, String gameName, String gameDate, String gameEndDate) //NOSONAR
    {
        this.leader = leader;
        this.players = players;
        this.agents = agents;
        this.configuration = configuration;
        this.rounds = rounds;
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
    }

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<GameAgent> getAgents() {
        return agents;
    }

    public void setAgents(List<GameAgent> agents) {
        this.agents = agents;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
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
}
