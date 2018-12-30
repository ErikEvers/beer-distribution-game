package org.han.ica.asd.c.model.domain_objects;

import java.util.Collections;
import java.util.List;

public class GameAgent implements IDomainModel {
    public final String gameAgentName;
    public final Facility facility;
    public final List<GameBusinessRules> gameBusinessRulesList;

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
}
