package org.han.ica.asd.c.interfaces.replay;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

/**
 * Small interface to retrieve game and/or round data
 */
public interface IRetrieveReplayData {
    /**
     * @return
     * Returns a list of all available facilities in a BeerGame with gameId
     */
    List<Facility> getAllFacilities();

    /**
     * @return
     * Returns the current BeerGame
     */
    BeerGame getGameLog();

    /***
     *
     * @return
     * Returns a list with all BeerGames available in the database
     */
    List<BeerGame> getAllBeerGames();
}
