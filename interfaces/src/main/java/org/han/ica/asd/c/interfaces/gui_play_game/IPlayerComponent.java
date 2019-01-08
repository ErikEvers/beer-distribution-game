package org.han.ica.asd.c.interfaces.gui_play_game;


import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.List;
import java.util.Map;

public interface IPlayerComponent {
    void activatePlayer();
    void activateGameAgent();
    Map<Facility, List<Facility>> seeOtherFacilities();
    void placeOrder(Facility facility, int amount);
    void selectProgrammedAgent(ProgrammedAgent programmedagent);
    void chooseFacility(Facility facility);
    String getFacilityName();
}
