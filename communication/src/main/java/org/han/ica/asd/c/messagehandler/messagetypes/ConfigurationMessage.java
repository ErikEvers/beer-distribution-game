package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Configuration;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.CONFIGURATION_MESSAGE;

public class ConfigurationMessage extends TransactionMessage {
    private Configuration configuration;

    public ConfigurationMessage(Configuration configuration) {
        super(CONFIGURATION_MESSAGE);
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
