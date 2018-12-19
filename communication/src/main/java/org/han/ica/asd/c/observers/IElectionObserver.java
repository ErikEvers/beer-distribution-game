package org.han.ica.asd.c.observers;

import domainobjects.Election;

public interface IElectionObserver extends IConnectorObserver {
    Election electionReceived(Election election);
}
