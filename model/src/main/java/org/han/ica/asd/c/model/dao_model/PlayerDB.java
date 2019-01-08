package org.han.ica.asd.c.model.dao_model;

import org.han.ica.asd.c.model.domain_objects.Facility;

public class PlayerDB implements IDaoModel{
    private String gameId;
    private String playerId;
    private String ipAddress;
    private Facility facilityId;
    private String name;
    private boolean isConnected;


    public PlayerDB() {}

    public PlayerDB(String gameId, String playerId, String ipAddress, Facility facilityId, String name, boolean isConnected) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.ipAddress = ipAddress;
        this.facilityId = facilityId;
        this.name = name;
        this.isConnected = isConnected;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Facility getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Facility facilityId) {
        this.facilityId = facilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
