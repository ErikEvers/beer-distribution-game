package org.han.ica.asd.c.interfaces.gameleader;

import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.exceptions.gameleader.BeerGameException;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

public interface IGameLeader {

	BeerGame getBeerGame();
	RoomModel getRoomModel();
	void startGame() throws BeerGameException, TransactionException;
}
