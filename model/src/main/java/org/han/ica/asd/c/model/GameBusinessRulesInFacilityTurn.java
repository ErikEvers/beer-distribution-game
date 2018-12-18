package org.han.ica.asd.c.model;

import java.util.List;

public class GameBusinessRulesInFacilityTurn {
    private List<Round> rounds;
    private List<GameBusinessRules> businessRules;

    public GameBusinessRulesInFacilityTurn() {
        // Empty as it also needs to be instantiated when empty
    }

    public GameBusinessRulesInFacilityTurn(List<Round> rounds, List<GameBusinessRules> businessRules) {
        this.rounds = rounds;
        this.businessRules = businessRules;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<GameBusinessRules> getBusinessRules() {
        return businessRules;
    }

    public void setBusinessRules(List<GameBusinessRules> businessRules) {
        this.businessRules = businessRules;
    }
}
