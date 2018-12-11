package org.han.ica.asd.c.model;

public class FacilityTurn_GameBusinessRules {
    private int roundId;
    private int facilityIdDeliver;
    private int facilityIdOrder;
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private String gameAgentName;
    private String gameBusinessRule;

    public FacilityTurn_GameBusinessRules(int roundID, int facilityIdDeliver, int facilityIdOrder, String gameName, String gameDate, String gameEndDate, String gameAgentName, String gameBusinessRule) {
        this.roundId = roundID;
        this.facilityIdDeliver = facilityIdDeliver;
        this.facilityIdOrder = facilityIdOrder;
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameEndDate() {
        return gameEndDate;
    }

    public void setGameEndDate(String gameEndDate) {
        this.gameEndDate = gameEndDate;
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
