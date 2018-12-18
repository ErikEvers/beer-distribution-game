package org.han.ica.asd.c.model.dao_model;

public class GameBusinessRulesInFacilityTurn {
    private int roundId;
    private int facilityIdDeliver;
    private int facilityIdOrder;
    private String gameId;
    private String gameAgentName;
    private String gameBusinessRule;
    private String gameAST;

    public GameBusinessRulesInFacilityTurn(int roundId, int facilityIdOrder, int facilityIdDeliver, String gameId, String gameAgentName, String gameBusinessRule, String gameAST) {
        this.roundId = roundId;
        this.facilityIdDeliver = facilityIdDeliver;
        this.facilityIdOrder = facilityIdOrder;
        this.gameId = gameId;
        this.gameAgentName = gameAgentName;
        this.gameBusinessRule = gameBusinessRule;
        this.gameAST = gameAST;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getFacilityIdDeliver() {
        return facilityIdDeliver;
    }

    public void setFacilityIdDeliver(int facilityIdDeliver) {
        this.facilityIdDeliver = facilityIdDeliver;
    }

    public int getFacilityIdOrder() {
        return facilityIdOrder;
    }

    public void setFacilityIdOrder(int facilityIdOrder) {
        this.facilityIdOrder = facilityIdOrder;
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
