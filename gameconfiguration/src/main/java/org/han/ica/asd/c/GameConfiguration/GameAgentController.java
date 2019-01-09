package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameAgentController implements IGameAgentController {


  @Inject private IGameConfigurationUserInterface gameConfigurationUserInterface;
  @Inject private ProgrammedAgentDAO programmedAgentDAO;
  @Inject private static final Logger LOGGER = Logger.getLogger(org.han.ica.asd.c.GameConfiguration.GameAgentController.class.getName());

  /**
   * Get all the ProgrammedAgents from the database. When the persistence layer returns no ProgrammedAgents, this
   * function will throw a Exception.
   * @return -> A list with agents.
   */
  public void getAgentsForUI() throws NoProgrammedAgentsFoundException {
    List<ProgrammedAgent> programmedAgents = programmedAgentDAO.readAllProgrammedAgents();
    if(!programmedAgents.isEmpty()) {
      gameConfigurationUserInterface.sendAgentsToUI(programmedAgents);
    } else {
      throw new NoProgrammedAgentsFoundException("No programmed agents found.");
    }
  }

  /**
   * This function is called from the GUI when the gameleader is finished with linking programmed agents to facilities.
   * When there are still facilities with no agents, this function will link default agents to the facilities.
   * @param map -> Hashmap with facilities and programmed agents. programmed agent can be null
   * @param programmedAgents -> all the programmed agents.
   * @return -> List of all registered GameAgents.
   */
  public List<GameAgent> setAgentsInFacilities(Map<Facility, ProgrammedAgent> map, List<ProgrammedAgent> programmedAgents) {
    List<GameAgent> gameAgents = new ArrayList<>();
    for(Map.Entry<Facility, ProgrammedAgent> set: map.entrySet()) {
      if(set.getValue() != null)
        gameAgents.add(new GameAgent(set.getValue().getProgrammedAgentName(), set.getValue().getProgrammedBusinessRules(), set.getKey()));
    }
    try {
      ProgrammedAgent defaultAgent = getDefaultAgent(programmedAgents);
      for(Map.Entry<Facility, ProgrammedAgent> set: map.entrySet()) {
        if(set.getValue() == null) {
          gameAgents.add(new GameAgent(defaultAgent.getProgrammedAgentName(), defaultAgent.getProgrammedBusinessRules(), set.getKey()));
        }
      }
    } catch (NoProgrammedAgentsFoundException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return gameAgents;
  }

  /**
   * Get the default programmed agent from the list.
   * @param programmedAgents -> List with all the programmed agents
   * @return -> The default agent
   * @throws NoProgrammedAgentsFoundException -> When there was no default agent found
   */
  private ProgrammedAgent getDefaultAgent(List<ProgrammedAgent> programmedAgents) throws NoProgrammedAgentsFoundException {
    for(ProgrammedAgent programmedAgent : programmedAgents) {
      if("Default".equals(programmedAgent.getProgrammedAgentName())) {
        return programmedAgent;
      }
    }
    throw new NoProgrammedAgentsFoundException("No default agent found. Create an agent with the name 'Default'.");
  }
}
