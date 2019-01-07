package org.han.ica.asd.c.public_interfaces;


import org.han.ica.asd.c.model.dao_model.RoundDB;
import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;

public interface IPlayerComponent {
    void activatePlayer();
    void activateAgent();
    RoundDB seeOtherFacilities();
    void placeOrder(int amount);
    void requestFacilityUsage(Facility facility);
    void requestFacilityInfo(String info);
    void selectAgent();
    List<String> getAllGames();
    void connectToGame(String game);
    List<Facility> getAllFacilities();
}
