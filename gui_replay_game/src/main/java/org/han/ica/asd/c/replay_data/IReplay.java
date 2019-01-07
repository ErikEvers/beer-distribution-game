package org.han.ica.asd.c.replay_data;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public interface IReplay {
    List<BeerGame> getAllGames();
    List<Round> getAverageRounds(int gameId);
    List<Round> getAllRounds(int gameId);
    List<Facility> getAllFacilities(int gameId);
    int getGameSize(int gameId);
}
