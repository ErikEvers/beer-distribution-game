package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.interfaces.gameconfiguration.IPersistenceProgrammedAgents;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentController implements IAgentController {

  private String gameId;
  private List<Facility> facilities;
  @Inject IPersistenceProgrammedAgents iPersistenceProgrammedAgents;
  @Inject private static Logger LOGGER;

  public void setAgentsInFacilities(List<Facility> facilities, String gameId) {
    this.gameId = gameId;
    this.facilities = facilities;
    try {
      List<ProgrammedAgent> agents = getAllProgrammedAgents();
    } catch(NoProgrammedAgentsFoundException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public List<ProgrammedAgent> getAllProgrammedAgents() throws NoProgrammedAgentsFoundException{
    List<ProgrammedAgent> agents = iPersistenceProgrammedAgents.getAllAgents();
    if(agents.isEmpty()) {
      throw new NoProgrammedAgentsFoundException("Could not find any programmed agents");
    }
    return agents;
  }

}
