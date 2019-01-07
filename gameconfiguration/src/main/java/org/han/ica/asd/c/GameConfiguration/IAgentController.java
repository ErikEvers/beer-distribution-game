package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import javax.rmi.PortableRemoteObject;
import java.util.List;

public interface IAgentController {

  void getAgentsForUI(List<Facility> facilities);

  List<Facility> agentsFinished(List<Facility> facilities, List<ProgrammedAgent> agents);

  List<Facility> setAgentsInFacilities(List<GameAgent> gameAgents, List<Facility> facilities);

  ProgrammedAgent getDefaultAgent(List<ProgrammedAgent> agents, String defaultName) throws NoProgrammedAgentsFoundException;
}
