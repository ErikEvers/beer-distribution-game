package org.han.ica.asd.c.interfaces.gui_play_game;


import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.List;
import java.util.Map;

public interface IPlayerComponent {
    void activatePlayer();
    void activateAgent();
    BeerGame seeOtherFacilities();
    void placeOrder(Facility facility, int amount);
    void sendDelivery(Facility facility, int amount);
    void submitTurn();
    void requestFacilityUsage(Facility facility);
    void selectAgent();
    List<String> getAllGames();
    void connectToGame(String game);
    List<Facility> getAllFacilities();
    void chooseFacility(Facility facility);
    String getFacilityName();
    void startNewTurn();
    Player getPlayer();
    void setPlayer(Player player);
    void clearPlayer();
    void setUi(IPlayGame game);
    void clearUi();
}
