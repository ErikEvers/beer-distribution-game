package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;

public interface IAgentController {

  void setAgentsInFacilities(List<Facility> facilities, String gameId);
}
