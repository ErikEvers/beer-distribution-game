package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectorForPlayer;
import org.han.ica.asd.c.interfaces.leadermigration.IConnectorForLeaderElection;

public interface IConnectorProvider {
    void setInstance(IConnector connector);

    IConnectorForSetup forSetup();

    IConnectorForPlayer forPlayer();

    IConnectorForLeader forLeader();

    IConnectorForLeaderElection forLeaderElection();
}
