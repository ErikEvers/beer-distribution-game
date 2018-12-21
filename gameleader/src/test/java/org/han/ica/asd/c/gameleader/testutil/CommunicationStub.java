package org.han.ica.asd.c.gameleader.testutil;

import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.observers.IConnectorObserver;

public class CommunicationStub implements IConnectorForLeader {

    @Override
    public void addObserver(IConnectorObserver observer) {

    }

    @Override
    public void sendRoundDataToAllPlayers(Round allData) {

    }
}
