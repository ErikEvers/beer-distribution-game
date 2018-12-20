package org.han.ica.asd.c.model.dao_model;

public class GameAgentDB implements IDaoModel{
    private String gameId;
    private String gameAgentName;
    private int facilityId;

    public GameAgentDB(String gameId, String gameAgentName, int facilityId) {
        this.gameId = gameId;
        this.gameAgentName = gameAgentName;
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

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
}
