package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.model.ProgrammedAgent;
import org.han.ica.asd.c.public_interfaces.IPlayerComponent;

public class PlayerComponent implements IPlayerComponent {
    private Player player = new Player("0", "0", "0", null, "1234", true);

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
    public void placeOrder(int amount) {

    }

    @Override
    public void selectProgrammedAgent(ProgrammedAgent programmedagent) {

    }

    @Override
    public void chooseFacility(Facility facility) {
        player.setFacilityId(facility);
    }

    @Override
    public String getFacilityName() {
        return "facilityName";
    }
}
