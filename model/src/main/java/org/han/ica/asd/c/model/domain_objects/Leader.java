package org.han.ica.asd.c.model.domain_objects;

import java.time.LocalDateTime;

public class Leader implements IDomainModel{
    private Player player;
    private LocalDateTime timestamp;

    public Leader(Player player) {
        this.player = player;
        timestamp = LocalDateTime.now();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
}
