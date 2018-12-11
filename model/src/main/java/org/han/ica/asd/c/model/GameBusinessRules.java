package org.han.ica.asd.c.model;

public class GameBusinessRules {
    private int facilityId;
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private String gameAgentName;
    private String gameBusinessRule;
    private String gameAST;

    public GameBusinessRules(int facilityId, String gameName, String gameDate, String gameEndDate, String gameAgentName, String gameBusinessRule, String gameAST) {
        this.facilityId = facilityId;
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
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

    public String getGameAST() {
        return gameAST;
    }

    public void setGameAST(String gameAST) {
        this.gameAST = gameAST;
    }
}
