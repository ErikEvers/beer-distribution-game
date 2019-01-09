package org.han.ica.asd.c.interfaces.gui_play_game;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;
import java.util.Map;

public interface IPlayerComponent {
    void activatePlayer();
    void activateAgent();
    Map<Facility, List<Facility>> seeOtherFacilities();
    void placeOrder(Facility facility, int amount);
    void requestFacilityUsage(Facility facility);
    String requestFacilityInfo(Facility facility);
    void selectAgent();
    List<String> getAllGames();
    void connectToGame(String game);
    List<Facility> getAllFacilities();
    void chooseFacility(Facility facility);
    String getFacilityName();
}
