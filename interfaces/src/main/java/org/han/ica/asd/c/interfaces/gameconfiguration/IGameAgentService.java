package org.han.ica.asd.c.interfaces.gameconfiguration;

import org.han.ica.asd.c.exceptions.gameconfiguration.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.List;

public interface IGameAgentService {

  List<ProgrammedAgent> getAgentsForUI() throws NoProgrammedAgentsFoundException;

  GameAgent createGameAgentFromProgrammedAgent(Facility facility, ProgrammedAgent programmedAgent);

  List<GameAgent> fillEmptyFacilitiesWithDefaultAgents(BeerGame beerGame);
}
