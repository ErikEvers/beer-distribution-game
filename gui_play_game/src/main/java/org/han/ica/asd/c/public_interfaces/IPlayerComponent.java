package org.han.ica.asd.c.public_interfaces;


import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.ProgrammedAgent;

public interface IPlayerComponent {
    void activatePlayer();
    void activateGameAgent();
    void seeOtherFacilities();
    void placeOrder(int amount);
    void selectProgrammedAgent(ProgrammedAgent programmedagent);
    void chooseFacility(Facility facility);
}
