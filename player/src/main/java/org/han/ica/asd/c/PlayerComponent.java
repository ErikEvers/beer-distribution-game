package org.han.ica.asd.c;

import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

public class PlayerComponent implements IPlayerComponent {


    @Override
    public void activatePlayer() {
        //Not implemented yet
    }

    @Override
    public void activateGameAgent() {
        //Not implemented yet
    }

    @Override
    public Map<Facility, List<Facility>> seeOtherFacilities() {
        //Not implemented yet
        return null;
    }

    @Override
    public void placeOrder(Facility facility, int amount) {
        //Not implemented yet
    }

    @Override
    public void selectProgrammedAgent(ProgrammedAgent programmedagent) {
        //Not implemented yet
    }

    @Override
    public void chooseFacility(Facility facility) {
        //Not implemented yet
    }

    @Override
    public String getFacilityName() {
        //Not implemented yet
        return "";
    }
}
