package org.han.ica.asd.c.player;

import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public class CommunicationStub implements IConnectedForPlayer {

	@Override
	public void sendTurn(Round turn) throws SendGameMessageException {

	}

	@Override
	public void addObserver(IConnectorObserver observer) {

	}

	@Override
	public void startFaultDetector() {

	}
}
