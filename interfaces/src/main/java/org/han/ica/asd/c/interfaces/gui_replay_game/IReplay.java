package org.han.ica.asd.c.interfaces.gui_replay_game;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public interface IReplay {
    /**
     * @return
     * Returns a list of all available BeerGames from the database
     */
    List<BeerGame> getAllGames();


    /**
     * @param gameId
     * Primary identifier for the selected game
     * @return
     * Returns a list of all calculated average Rounds
     */
    List<Round> getAverageRounds(int gameId);

    /**
     * @param gameId
     * Primary identifier for the selected game
     * @return
     * Returns a list of all available rounds in a BeerGame with gameId
     */
    List<Round> getAllRounds(int gameId);

    /**
     * @param gameId
     * Primary identifier for the selected game
     * @return
     * Returns a list of all available facilities in a BeerGame with gameId
     */
    List<Facility> getAllFacilities(int gameId);

    /**
     * @param gameId
     * Primary identifier for the selected game
     * @return
     * Returns the size of a BeerGame with gameId
     */
    int getGameSize(int gameId);
}
