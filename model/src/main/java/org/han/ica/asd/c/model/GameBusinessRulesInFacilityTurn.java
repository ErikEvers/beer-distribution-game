package org.han.ica.asd.c.model;

public class GameBusinessRulesInFacilityTurn {
    private int roundId;
    private Facility facilityIdDeliver;
    private Facility facilityIdOrder;
    private String gameId;
    private String gameAgentName;
    private String gameBusinessRule;

    public GameBusinessRulesInFacilityTurn(int roundId, FacilityLinkedTo facilityLinkedTo, String gameId, String gameAgentName, String gameBusinessRule) {
        this.roundId = roundId;
        this.facilityIdDeliver = facilityLinkedTo.getFacilityDeliver();
        this.facilityIdOrder = facilityLinkedTo.getFacilityOrder();
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

    public Facility getFacilityDeliver() {
        return facilityIdDeliver;
    }

    public void setFacilityDeliver(Facility facilityIdDeliver) {
        this.facilityIdDeliver = facilityIdDeliver;
    }

    public Facility getFacilityOrder() {
        return facilityIdOrder;
    }

    public void setFacilityOrder(Facility facilityIdOrder) {
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
