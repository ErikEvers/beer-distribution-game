package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.interfaces.gameconfiguration.IPersistenceProgrammedAgents;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO -> reminder: Interfaces in right directory
public class AgentController implements IAgentController {

  private List<ProgrammedAgent> agents;
  @Inject IGameConfigurationUserInterface gameConfigurationUserInterface;
  @Inject IPersistenceProgrammedAgents iPersistenceProgrammedAgents;
  @Inject private static Logger LOGGER;

  /**
   * Get all the agents and send them to the UI, so the GameLeader can chose the agents for empty facilities.
   * @param facilities -> All the facilities get from the service.
   */
  public void getAgentsForUI(List<Facility> facilities) {
    try {
      // TODO -> The agents has to be send to the GUI, so the gameleader can pick one!
      agents = getAllProgrammedAgents();
      gameConfigurationUserInterface.sendAgentsToUI(agents);
    } catch(NoProgrammedAgentsFoundException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  /**
   * This function, sets the GameAgents objects to Facility objects
   * @param gameAgents -> GameAgent opbjects returned from the UI
   * @param facilities -> All the facilities in this game
   * @return -> List with facilities with the chosen agents.
   */
  public List<Facility> setAgentsInFacilities(List<GameAgent> gameAgents, List<Facility> facilities) {
    for(GameAgent gameAgent: gameAgents) {
      for(Facility facility: facilities) {
        if(gameAgent.getFacility().getFacilityId() == facility.getFacilityId())
          facility.setAgent(gameAgent);
      }
    }
    return facilities;
  }

  /**
   * This function is called from the interface when the Game Leader is done with setting the GameAgents on specific
   * facilities.
   * @param facilities -> All the facilities in this game.
   * @return The list with facilities with the linked GameAgents.
   */
  public List<Facility> agentsFinished(List<Facility> facilities) {
    try {
      ProgrammedAgent defaultAgent = getDefaultAgent(this.agents);
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

  /**
   * Get all the ProgrammedAgents from the database. When the persistence layer returns no ProgrammedAgents, this
   * function will throw a Exception.
   * @return -> A list with agents.
   * @throws NoProgrammedAgentsFoundException
   */
  private List<ProgrammedAgent> getAllProgrammedAgents() throws NoProgrammedAgentsFoundException{
    List<ProgrammedAgent> agents = iPersistenceProgrammedAgents.getAllAgents();
    if(agents.isEmpty()) {
      throw new NoProgrammedAgentsFoundException("Could not find any programmed agents");
    }
    return agents;
  }

  //TODO -> Afspreken default agent name.
  /**
   * To find the Default agent. The agent is found on the default agent name
   * @param agents -> All the agents from the database.
   * @return -> The default ProgrammedAgent in game
   * @throws NoProgrammedAgentsFoundException
   */
  private ProgrammedAgent getDefaultAgent(List<ProgrammedAgent> agents) throws NoProgrammedAgentsFoundException {
    for(ProgrammedAgent agent: agents){
      if ("Default".equals(agent.getProgrammedAgentName())) {
        return agent;
      }
    }
    throw new NoProgrammedAgentsFoundException("No default agent found. Please create a default agent");
  }

}
