package org.han.ica.asd.c.model.dao_model;

public class Facility implements IDaoModel{
    private String gameId;
    private int facilityId;
    private String facilityType;
    private String playerId;
    private String gameAgentName;
    private boolean bankrupt;

    public Facility(String gameId, int facilityId, String facilityType, String playerId, String gameAgentName, boolean bankrupt) {
        this.gameId = gameId;
        this.facilityId = facilityId;
        this.facilityType = facilityType;
        this.playerId = playerId;
        this.gameAgentName = gameAgentName;
        this.bankrupt = bankrupt;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameAgentName() {
        return gameAgentName;
    }

    public void setGameAgentName(String gameAgentName) {
        this.gameAgentName = gameAgentName;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }
}
