package org.han.ica.asd.c.public_interfaces;


import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.List;

public interface IPlayerComponent {
    void activatePlayer();
    void activateAgent();
    void seeOtherFacilities();
    void placeOrder(int amount);
    void chooseFacility(Facility facility);
    void requestFacilityInfo(String info);
    void selectAgent();
    List<String> getAllGames();
    void connectToGame();
}
