package org.han.ica.asd.c.model.domain_objects;

import java.util.Collections;
import java.util.List;

public class GameAgent implements IDomainModel{
    private String gameAgentName;

    private List<GameBusinessRules> gameBusinessRulesList;

    private Facility facility;

    public GameAgent(String gameAgentName, Facility facility, List<GameBusinessRules> gameBusinessRulesList) {
        this.gameAgentName = gameAgentName;
        this.facility = facility;
        this.gameBusinessRulesList = Collections.unmodifiableList(gameBusinessRulesList);
    }

    public String getGameAgentName() {
        return gameAgentName;
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
}
