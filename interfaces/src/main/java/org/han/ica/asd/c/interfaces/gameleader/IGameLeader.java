package org.han.ica.asd.c.interfaces.gameleader;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

public interface IGameLeader {

	BeerGame getBeerGame();
	RoomModel getRoomModel();
	void startGame();
}
