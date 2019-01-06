package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;

import java.util.List;

public interface IAgentController {

  void getAgentsForUI(List<Facility> facilities);

  List<Facility> agentsFinished(List<Facility> facilities);

  public List<Facility> setAgentsInFacilities(List<GameAgent> gameAgents, List<Facility> facilities);
}
