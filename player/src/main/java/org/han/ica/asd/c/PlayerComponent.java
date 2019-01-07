package org.han.ica.asd.c;

import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PlayerComponent implements IPlayerComponent {


    @Override
    public void activatePlayer() {

    }

    @Override
    public void activateGameAgent() {

    }

    @Override
    public void seeOtherFacilities() {

    }

    @Override
    public void placeOrder(Facility facility, int amount) {
        throw new NotImplementedException();
    }

    @Override
    public void selectProgrammedAgent(ProgrammedAgent programmedagent) {

    }

    @Override
    public void chooseFacility(Facility facility) {

    }

    @Override
    public String getFacilityName() {

        throw new NotImplementedException();
    }
}
