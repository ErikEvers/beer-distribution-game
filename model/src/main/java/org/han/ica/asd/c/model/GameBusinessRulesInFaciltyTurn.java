package org.han.ica.asd.c.model;

public class GameBusinessRulesInFaciltyTurn {
    private int roundId;
    private int facilityIdDeliver;
    private int facilityIdOrder;
    private String gameId;
    private String gameAgentName;
    private String gameBusinessRule;

    public GameBusinessRulesInFaciltyTurn(int roundId, int facilityIdDeliver, int facilityIdOrder, String gameId, String gameAgentName, String gameBusinessRule) {
        this.roundId = roundId;
        this.facilityIdDeliver = facilityIdDeliver;
        this.facilityIdOrder = facilityIdOrder;
        this.gameId = gameId;
        this.gameAgentName = gameAgentName;

        this.gameBusinessRule = gameBusinessRule;
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
}
