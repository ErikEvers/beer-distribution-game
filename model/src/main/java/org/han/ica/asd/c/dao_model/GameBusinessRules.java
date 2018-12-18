package org.han.ica.asd.c.dao_model;

public class GameBusinessRules {
    private int facilityId;
    private String gameId;
    private String gameAgentName;
    private String gameBusinessRule;
    private String gameAST;

    public GameBusinessRules(int facilityId, String gameId, String gameAgentName, String gameBusinessRule, String gameAST) {
        this.facilityId = facilityId;
        this.gameId = gameId;
        this.gameAgentName = gameAgentName;
        this.gameBusinessRule = gameBusinessRule;
        this.gameAST = gameAST;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameAgentName() {
        return gameAgentName;
    }

    public void setGameAgentName(String gameAgentName) {
        this.gameAgentName = gameAgentName;
    }

    public String getGameBusinessRule() {
        return gameBusinessRule;
    }

    public void setGameBusinessRule(String gameBusinessRule) {
        this.gameBusinessRule = gameBusinessRule;
    }

    public String getGameAST() {
        return gameAST;
    }

    public void setGameAST(String gameAST) {
        this.gameAST = gameAST;
    }
}
