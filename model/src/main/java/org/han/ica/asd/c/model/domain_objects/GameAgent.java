package org.han.ica.asd.c.model.domain_objects;

import java.util.List;

public class GameAgent implements IDomainModel{
    private String gameAgentName;
    private int facilityId;
    private List<GameBusinessRules> gameBusinessRules;

    public GameAgent(String gameAgentName, int facilityId, List<GameBusinessRules> gameBusinessRules) {
        this.gameAgentName = gameAgentName;
        this.facilityId = facilityId;
        this.gameBusinessRules = gameBusinessRules;
    }

    public String getGameAgentName() {
        return gameAgentName;
    }

    public void setGameAgentName(String gameAgentName) {
        this.gameAgentName = gameAgentName;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public List<GameBusinessRules> getGameBusinessRules() {
        return gameBusinessRules;
    }

    public void setGameBusinessRules(List<GameBusinessRules> gameBusinessRules) {
        this.gameBusinessRules = gameBusinessRules;
    }
}
