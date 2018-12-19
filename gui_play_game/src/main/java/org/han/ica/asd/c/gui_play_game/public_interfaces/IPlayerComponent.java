package org.han.ica.asd.c.gui_play_game.public_interfaces;


import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

public interface IPlayerComponent {
    void activatePlayer();
    void activateGameAgent();
    void seeOtherFacilities();
    void placeOrder(int amount);
    void selectProgrammedAgent(ProgrammedAgent programmedagent);
    void chooseFacility(Facility facility);
}
