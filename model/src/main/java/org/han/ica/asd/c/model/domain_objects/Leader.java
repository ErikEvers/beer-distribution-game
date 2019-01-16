package org.han.ica.asd.c.model.domain_objects;

import java.io.Serializable;

public class Leader implements IDomainModel, Serializable {
    private Player player;
    private String timestamp = "";

    public Leader(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
}
