package org.han.ica.asd.c.gameconfiguration;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.Arrays;

public class ManagePlayersService {

	private Player[] players = new Player[]{new Player("1", "111", new Facility(), "Henk", true), new Player("2", "222", new Facility(), "Henk", true)};

	/**
	 * Fetch the currently connected players.
	 * @return
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Remove a player from the game.
	 * @param toRemove the player to remove
	 */
	public void removePlayer(Player toRemove) {
		players = Arrays.stream(players).filter(player -> !player.getPlayerId().equals(toRemove.getPlayerId())).toArray(Player[]::new);
	}
}
