package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.List;
import java.util.Map;

public interface IGameAgentController {

  List<ProgrammedAgent> getAgentsForUI() throws NoProgrammedAgentsFoundException;

  List<GameAgent> setAgentsInFacilities(Map<Facility, ProgrammedAgent> map);

}
