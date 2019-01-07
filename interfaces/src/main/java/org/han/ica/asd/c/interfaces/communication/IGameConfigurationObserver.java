package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.model.domain_objects.Configuration;

public interface IGameConfigurationObserver extends IConnectorObserver {
    void gameConfigurationReceived(Configuration configuration);
}
