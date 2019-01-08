package org.han.ica.asd.c.player;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.interfaces.player.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerComponent implements IPlayerComponent {
    IPlayerGameLogic gameLogic;
    private Configuration configuration;

    public PlayerComponent() {
        gameLogic = new GameLogic("", null, null, null);
    }

	public PlayerComponent(Configuration configuration) {
		this.configuration = configuration;
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
    public void placeOrder(int amount) {
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

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
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
    public List<Agent> getSavedAgents() {
        return gameLogic.getSavedAgents();
    }

    @Override
    public Map<Facility, List<Facility>> seeOtherFacilities() {
        //Fake method for testing purposes
        gameLogic.seeOtherFacilities();
        Map<Facility, List<Facility>> map = new HashMap<>();

        Facility facility = new Facility(new FacilityType("Distributor", 1, 1, 1, 2, 25, 1, 1), 0);
        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(new Facility(new FacilityType("Retailer", 1, 1, 1, 2, 25, 1, 2), 1));
        map.put(facility, facilityList);

        return map;
    }
    
    @Override
    public String requestFacilityInfo(Facility facility) {
        //Fake method for testing purposes
        return "placeholderfac overview turn x\nBacklog: 25\nInventory: 0\nMoney: 500"+facility.getFacilityId();
    }
}
