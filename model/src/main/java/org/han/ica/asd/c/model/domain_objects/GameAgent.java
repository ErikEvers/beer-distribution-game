package org.han.ica.asd.c.model.domain_objects;

import java.util.ArrayList;
import java.util.List;

public class GameAgent implements IDomainModel {
    private String gameAgentName;
    protected Facility facility;
    protected List<GameBusinessRules> gameBusinessRulesList = new ArrayList<>();

    public GameAgent(String gameAgentName, Facility facility) {
        this.gameAgentName = gameAgentName;
        this.facility = facility;
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
}
