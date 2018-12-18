package org.han.ica.asd.c.model.dao_model;

import java.sql.Timestamp;

public class Leader implements IDaoModel{
    private String gameId;
    private String playerId;
    private Timestamp timestamp;

    public Leader(String gameId, String playerId, Timestamp timestamp) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.timestamp = timestamp;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
