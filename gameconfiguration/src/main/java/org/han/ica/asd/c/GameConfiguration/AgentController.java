package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.interfaces.gameconfiguration.IPersistenceProgrammedAgents;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO -> reminder: Interfaces in right directory
public class AgentController implements IAgentController {

  private String gameId;
  private List<Facility> facilities = new ArrayList<>();
  private List<ProgrammedAgent> agents;
  @Inject IPersistenceProgrammedAgents iPersistenceProgrammedAgents;
  @Inject private static Logger LOGGER;

  public void setAgentsInFacilities(List<Facility> facilities, String gameId) {
    this.gameId = gameId;
    this.facilities = facilities;
    try {
      agents = new ArrayList<>();
      agents = getAllProgrammedAgents();
      // TODO -> The agents has to be send to the GUI, so the gameleader can pick one!
    } catch(NoProgrammedAgentsFoundException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public List<Facility> agentsFinished(List<Facility> facilities) {
    try {
      ProgrammedAgent defaultAgent = getDefaultAgent();
      for(Facility facility: facilities) {
        if(facility.getAgent() == null) {
          GameAgent gameAgent = new GameAgent(defaultAgent.getProgrammedAgentName(), facility);
          facility.setAgent(gameAgent);
        }
      }
    } catch(NoProgrammedAgentsFoundException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return facilities;
  }

  private List<ProgrammedAgent> getAllProgrammedAgents() throws NoProgrammedAgentsFoundException{
    List<ProgrammedAgent> agents = iPersistenceProgrammedAgents.getAllAgents();
    if(agents.isEmpty()) {
      throw new NoProgrammedAgentsFoundException("Could not find any programmed agents");
    }
    return agents;
  }

  //TODO -> Afspreken default agent name.

  private ProgrammedAgent getDefaultAgent() throws NoProgrammedAgentsFoundException {
    for(ProgrammedAgent agent: agents){
      if ("Default".equals(agent.getProgrammedAgentName())) {
        return agent;
      }
    }
    throw new NoProgrammedAgentsFoundException("No default agent found. Please create a default agent");
  }

}
