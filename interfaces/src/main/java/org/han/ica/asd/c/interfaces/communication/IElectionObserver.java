package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.model.interface_models.Election;

public interface IElectionObserver extends IConnectorObserver {
    Election electionReceived(Election election);
}
