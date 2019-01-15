package org.han.ica.asd.c.interfaces.gui_play_game;


import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public interface IPlayerComponent {
    void activatePlayer();
    void activateAgent();
    BeerGame getBeerGame();
    Round getRound();
    void placeOrder(Facility facility, int amount);
    void sendDelivery(Facility facility, int amount);
    void submitTurn() throws SendGameMessageException;
    void requestFacilityUsage(Facility facility);
    void selectAgent();
    List<String> getAllGames();
    void connectToGame(String game);
    List<Facility> getAllFacilities();
    void chooseFacility(Facility facility);
    String getFacilityName();
    Player getPlayer();
    void setPlayer(Player player);
    void clearPlayer();
    void setUi(IPlayGame game);
    void clearUi();
}
