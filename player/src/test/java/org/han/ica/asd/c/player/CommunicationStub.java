package org.han.ica.asd.c.player;

import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public class CommunicationStub implements IConnectedForPlayer {
	@Override
	public boolean sendTurnData(Round turn) {
		return true;
	}

	@Override
	public void addObserver(IConnectorObserver observer) {

	}
}
