package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.model.interface_models.ElectionModel;

public interface IElectionObserver extends IConnectorObserver {
    ElectionModel electionReceived(ElectionModel election);
}
