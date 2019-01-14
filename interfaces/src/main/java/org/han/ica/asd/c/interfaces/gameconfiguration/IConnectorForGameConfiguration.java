package org.han.ica.asd.c.interfaces.gameconfiguration;

import org.han.ica.asd.c.model.domain_objects.Player;

/**
 * Interface for communication component, to be used by the game configuration component.
 */
public interface IConnectorForGameConfiguration {

	/**
	 * Retrieve the known players of the current room.
	 * @return the array of players.
	 */
	Player[] getPlayersFromRoom();

	/**
	 * Remove a player from the current room.
	 * @param toRemove the player that has to be removed.
	 * @return the updated array of players in the current room.
	 */
	Player[] removePlayerFromRoom(Player toRemove);
}
