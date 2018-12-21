package org.han.ica.asd.c.gameleader.testutil;

import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.model.domain_objects.Round;

public class CommunicationStub implements IConnectorForLeader {

    @Override
    public void addObserver(IConnectorObserver observer) {

    }

    @Override
    public void sendRoundDataToAllPlayers(Round allData) {

    }
}