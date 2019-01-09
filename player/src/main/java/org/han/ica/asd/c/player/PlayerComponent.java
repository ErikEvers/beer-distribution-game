package org.han.ica.asd.c.player;

import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.util.List;

public class PlayerComponent implements IPlayerComponent {

    @Inject
    IPlayerGameLogic gameLogic;

    static Player player;

    public PlayerComponent() {
    }

	@Override
	public void activatePlayer() {
		//stub for now
	}

    @Override
    public void activateAgent() {
        //Yet to be implemented.
    }

    @Override
    public void requestFacilityUsage(Facility facility) {
        gameLogic.requestFacilityUsage(facility);
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

    @Override
    public BeerGame seeOtherFacilities() {
        return gameLogic.seeOtherFacilities();
    }
    
    @Override
    public void placeOrder(Facility facility, int amount) {
        //Not implemented yet
    }

    @Override
    public void chooseFacility(Facility facility) {
        //comm.chooseFacility(facility, player.getPlayerId());
    }

    @Override
    public String getFacilityName() {
        return player.getFacility().getFacilityType().getFacilityName();
    }

    public static void setPlayer(Player player) {
        PlayerComponent.player = player;
    }

    public static Player getPlayer() {
        return player;
    }
}
