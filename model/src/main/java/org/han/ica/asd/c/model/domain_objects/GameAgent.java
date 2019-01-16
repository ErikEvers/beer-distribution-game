package org.han.ica.asd.c.model.domain_objects;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class GameAgent implements IDomainModel, Serializable {
    private String gameAgentName;
    private Facility facility;
    private List<GameBusinessRules> gameBusinessRulesList;

    public GameAgent(String gameAgentName, Facility facility, List<GameBusinessRules> gameBusinessRules) {
        this.gameAgentName = gameAgentName;
        this.facility = facility;
        this.gameBusinessRulesList = Collections.unmodifiableList(gameBusinessRules);
    }

    public GameAgent() {
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
        return gameBusinessRulesList;
    }

    public void setGameBusinessRules(List<GameBusinessRules> gameBusinessRules) {
        this.gameBusinessRulesList = gameBusinessRules;
    }

    @Override
    public String toString() {
        return this.gameAgentName;
    }
}
