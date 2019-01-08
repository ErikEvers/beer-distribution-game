package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Configuration;

public class ConfigurationMessage extends TransactionMessage {
    private Configuration configuration;

    public ConfigurationMessage(Configuration configuration) {
        super(5);
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
    
}
