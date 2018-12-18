package org.han.ica.asd.c.model.domain_objects;

public class GameAgent {
    private String gameAgentName;
    private int facilityId;

    public GameAgent(String gameAgentName, int facilityId) {
        this.gameAgentName = gameAgentName;
        this.facilityId = facilityId;
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
