package org.han.ica.asd.c;

import org.han.ica.asd.c.interfaces.communication.IConnector;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.communication.IConnectorProvider;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectorForPlayer;
import org.han.ica.asd.c.interfaces.leadermigration.IConnectorForLeaderElection;

public class ConnectorProvider implements IConnectorProvider {
    private static Connector connectorInstance;
    public void setInstance(IConnector connector){
        connectorInstance = (Connector) connector;
    }

    public IConnectorForSetup forSetup(){
        return connectorInstance;
    }

    public IConnectorForPlayer forPlayer(){
        return connectorInstance;
    }

    public IConnectorForLeader forLeader(){
        return connectorInstance;
    }

    public IConnectorForLeaderElection forLeaderElection() {
        return connectorInstance;
    }
}
