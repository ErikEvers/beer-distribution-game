package org.han.ica.asd.c.model.domain_objects;

import java.util.List;

public class GameBusinessRulesInFacilityTurn implements IDomainModel{
    int facilityId;
    int roundId;
    String gameAgentName;
    List<GameBusinessRules> gameBusinessRulesList;

    public GameBusinessRulesInFacilityTurn() {
        // Empty as it also needs to be instantiated when empty
    }

    public GameBusinessRulesInFacilityTurn(int facilityId, int roundId, String gameAgentName, List<GameBusinessRules> gameBusinessRulesList) {
        this.facilityId = facilityId;
        this.roundId = roundId;
        this.gameAgentName = gameAgentName;
        this.gameBusinessRulesList = gameBusinessRulesList;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public String getGameAgentName() {
        return gameAgentName;
    }

    public void setGameAgentName(String gameAgentName) {
        this.gameAgentName = gameAgentName;
    }

    public List<GameBusinessRules> getGameBusinessRulesList() {
        return gameBusinessRulesList;
    }

    public void setGameBusinessRulesList(List<GameBusinessRules> gameBusinessRulesList) {
        this.gameBusinessRulesList = gameBusinessRulesList;
    }
}
