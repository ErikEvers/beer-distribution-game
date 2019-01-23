package org.han.ica.asd.c.player;

import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectorForPlayer;
import org.han.ica.asd.c.model.domain_objects.Round;

public class CommunicationStub implements IConnectorForPlayer {

	@Override
	public void sendTurnData(Round turn) throws SendGameMessageException {

	}

	@Override
	public void addObserver(IConnectorObserver observer) {

	}

	@Override
	public void removeObserver(IConnectorObserver observer) {

	}

	@Override
	public void startFaultDetector() {

	}
}
