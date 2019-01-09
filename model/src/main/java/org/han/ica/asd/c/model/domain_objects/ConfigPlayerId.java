package org.han.ica.asd.c.model.domain_objects;

public class ConfigPlayerId {
    private String playerId;
    private Configuration config;

    public ConfigPlayerId(Configuration config, String playerId) {
        this.config = config;
        this.playerId = playerId;
    }
}
