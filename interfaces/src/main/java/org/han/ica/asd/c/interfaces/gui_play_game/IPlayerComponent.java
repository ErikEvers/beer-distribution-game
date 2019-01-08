package org.han.ica.asd.c.interfaces.gui_play_game;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public interface IPlayerComponent {
    void activatePlayer();
    void activateAgent();
    Round seeOtherFacilities();
    void placeOrder(int amount);
    void requestFacilityUsage(Facility facility);
    void requestFacilityInfo(String info);
    void selectAgent();
    List<String> getAllGames();
    void connectToGame(String game);
    List<Facility> getAllFacilities();
}
