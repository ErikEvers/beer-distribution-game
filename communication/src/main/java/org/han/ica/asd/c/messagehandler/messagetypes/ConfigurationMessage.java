package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Configuration;

public class ConfigurationMessage extends GameMessage {
    private Configuration configuration;

    public ConfigurationMessage(Configuration configuration) {
        super(5);
        this.configuration = configuration;
    }
}
