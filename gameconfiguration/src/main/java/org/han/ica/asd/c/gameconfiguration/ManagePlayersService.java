package org.han.ica.asd.c.gameconfiguration;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.Arrays;

public class ManagePlayersService {

	/**
	 * Fetch the currently connected players.
	 * @return
	 */
//	public Player[] getPlayers() {
//		return players;
//	}

	/**
	 * Remove a player from the game.
	 * @param toRemove the player to remove
	 */
	public BeerGame removePlayer(BeerGame beerGame, Player toRemove) {
		for (int i = 0; i < beerGame.getPlayers().size(); i++) {
			if (beerGame.getPlayers().get(i).getPlayerId().equals(toRemove.getPlayerId())) {
				beerGame.getPlayers().remove(i);
				return beerGame;
			}
		}
		return beerGame;
	}
}
