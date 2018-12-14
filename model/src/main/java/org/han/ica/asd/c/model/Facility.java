package org.han.ica.asd.c.model;

public class Facility {
    private String gameId;
    private int facilityId;
    private FacilityType facilityType;
    private String ipAddress;
    private String gameAgentName;


    public Facility(String gameId, int facilityId, FacilityType facilityType, String ipAddress, String gameAgentName) {
        this.gameId = gameId;
        this.facilityId = facilityId;
        this.facilityType = facilityType;
        this.ipAddress = ipAddress;
        this.gameAgentName = gameAgentName;
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

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGameAgentName() {
        return gameAgentName;
    }

    public void setGameAgentName(String gameAgentName) {
        this.gameAgentName = gameAgentName;
    }
}
