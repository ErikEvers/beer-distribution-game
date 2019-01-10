package org.han.ica.asd.c.player;

import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.model.domain_objects.Round;

public class CommunicationStub implements IConnectedForPlayer {
	@Override
	public void sendTurnData(Round turn) {

	}

	@Override
	public void addObserver(IConnectorObserver observer) {

	}
}
