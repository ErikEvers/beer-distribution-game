package org.han.ica.asd.c.gameconfiguration;

import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.List;
import java.util.Map;

public interface IGameAgentService {

  List<ProgrammedAgent> getAgentsForUI() throws NoProgrammedAgentsFoundException;

  GameAgent createGameAgentFromProgrammedAgent(Facility facility, ProgrammedAgent programmedAgent);

  BeerGame fillEmptyFacilitiesWithDefaultAgents(BeerGame beerGame);
}
