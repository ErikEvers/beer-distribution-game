package org.han.ica.asd.c.player;

import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import java.util.List;
import java.util.Map;

public class PlayerComponent implements IPlayerComponent {

	private Configuration configuration;

	public PlayerComponent() {

	}

	public PlayerComponent(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void activatePlayer() {
		//stub for now
	}

	@Override
	public void activateGameAgent() {
		//stub for now
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Map<Facility, List<Facility>> seeOtherFacilities() {
		return configuration.getFacilitiesLinkedTo();
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
}
