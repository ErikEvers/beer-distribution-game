package org.han.ica.asd.c.model.domain_objects;

import java.util.List;

public class GameAgent implements IDomainModel{
    private String gameAgentName;
    private Facility facility;
    private List<GameBusinessRules> gameBusinessRules;


    public GameAgent(String gameAgentName, Facility facility, List<GameBusinessRules> gameBusinessRules) {
        this.gameAgentName = gameAgentName;
        this.facility = facility;
        this.gameBusinessRules = gameBusinessRules;
    }

    public String getGameAgentName() {
        return gameAgentName;
    }

    public void setGameAgentName(String gameAgentName) {
        this.gameAgentName = gameAgentName;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public List<GameBusinessRules> getGameBusinessRules() {
        return gameBusinessRules;
    }

    public void setGameBusinessRules(List<GameBusinessRules> gameBusinessRules) {
        this.gameBusinessRules = gameBusinessRules;
    }
}
