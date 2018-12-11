package org.han.ica.asd.c.public_interfaces;


import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.GameAgent;

public interface IPlayerComponent {
    void insertPlayer();
    void insertAgent();
    void seeOtherFacilities();
    void placeOrder(int amount);
    void selectAgent(GameAgent agent);
    void chooseFacility(Facility facility);
}
