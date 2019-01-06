package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;

public interface IAgentController {

  List<Facility> setAgentsInFacilities(List<Facility> facilities, String gameId);

  List<Facility> agentsFinished(List<Facility> facilities);
}
