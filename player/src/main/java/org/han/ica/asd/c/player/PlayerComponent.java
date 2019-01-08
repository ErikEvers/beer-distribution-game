package org.han.ica.asd.c.player;

import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerComponent implements IPlayerComponent {

	@Override
	public void activatePlayer() {
		//stub for now
	}

	@Override
	public void activateGameAgent() {
		//stub for now
	}

	public Map<Facility, List<Facility>> seeOtherFacilities() {
        //Fake method for testing purposes
        Map<Facility, List<Facility>> map = new HashMap<>();

        Facility facility = new Facility(new FacilityType("Distributor", 1, 1, 1, 2, 25, 1, 1), 0);
        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(new Facility(new FacilityType("Retailer", 1, 1, 1, 2, 25, 1, 2), 1));
        map.put(facility, facilityList);

        return map;
    }

	@Override
	public void placeOrder(int amount) {
		//stub for now
	}

	@Override
	public void selectProgrammedAgent(ProgrammedAgent programmedagent) {
		//stub for now
	}

	@Override
	public void chooseFacility(Facility facility) {
		//stub for now
	}

	public String requestFacilityInfo(Facility facility) {
        //Fake method for testing purposes
        return "placeholderfac overview turn x\nBacklog: 25\nInventory: 0\nMoney: 500";
    }
}