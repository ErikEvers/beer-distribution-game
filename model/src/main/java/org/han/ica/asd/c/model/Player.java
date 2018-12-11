package org.han.ica.asd.c.model;

public class Player {
    private String gameId;
    private String playerId;
    private String ipAddress;
    private int facilityId;
    private String name;
    private boolean isConnected;

    public Player(String gameId, String playerId, String ipAddress, int facilityId, String name, boolean isConnected) {
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

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
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
