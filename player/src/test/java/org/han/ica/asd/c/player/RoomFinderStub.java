package org.han.ica.asd.c.player;

import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.interfaces.communication.IFinder;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.List;

public class RoomFinderStub implements IFinder {
	@Override
	public List<String> getAvailableRooms() throws DiscoveryException {
		return null;
	}

	@Override
	public RoomModel createGameRoomModel(String roomName, String leaderIP, String password) throws DiscoveryException {
		return null;
	}

	@Override
	public RoomModel joinGameRoomModel(String roomName, String hostIP, String password) throws DiscoveryException, RoomException {
		return null;
	}

	@Override
	public void startGameRoom(String roomName) throws DiscoveryException {

	}

	@Override
	public RoomModel getRoom(RoomModel roomModel) throws DiscoveryException {
		return null;
	}

	@Override
	public void removeHostFromRoom(RoomModel roomModel, String hostIP) throws DiscoveryException {

	}
}
