package org.han.ica.asd.c;

import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.model.dao_model.RoundDB;
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
        //Yet to be implemented.
    }

    @Override
    public void activateAgent() {
        //Yet to be implemented.
    }

    @Override
    public RoundDB seeOtherFacilities() {
        return gameLogic.seeOtherFacilities();
    }

    @Override
    public void placeOrder(int amount) {
        //Yet to be implemented.
    }

    @Override
    public void requestFacilityUsage(Facility facility) {
        gameLogic.requestFacilityUsage(facility);
    }

    @Override
    public void requestFacilityInfo(String info) {
        //Yet to be implemented.
    }

    @Override
    public void selectAgent() {
        //Yet to be implemented.
    }

    @Override
    public List<String> getAllGames() {
        return gameLogic.getAllGames();
    }

    @Override
    public void connectToGame(String game) {
        gameLogic.connectToGame(game);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return gameLogic.getAllFacilities();
    }
}
