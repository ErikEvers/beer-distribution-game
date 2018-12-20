package org.han.ica.asd.c;

import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.public_interfaces.IPlayerComponent;

import java.util.List;

public class PlayerComponent implements IPlayerComponent {
    IPlayerGameLogic gameLogic;

    public PlayerComponent() {
        gameLogic = new GameLogic("", null, null, null);
    }


    @Override
    public void activatePlayer() {

    }

    @Override
    public void activateAgent() {

    }

    @Override
    public void seeOtherFacilities() {

    }

    @Override
    public void placeOrder(int amount) {

    }

    @Override
    public void chooseFacility(Facility facility) {

    }

    @Override
    public void requestFacilityInfo(String info) {

    }

    @Override
    public void selectAgent() {

    }

    @Override
    public List<String> getAllGames() {
        return null;
    }

    @Override
    public void connectToGame() {

    }
}
