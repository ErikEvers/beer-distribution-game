package org.han.ica.asd.c.gameconfiguration;

import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameAgentService implements IGameAgentService {

  @Inject private ProgrammedAgentDAO programmedAgentDAO;
  private ProgrammedAgent defaultAgent;
  /**
   * Get all the ProgrammedAgents from the database. When the persistence layer returns no ProgrammedAgents, this
   * function will throw a Exception.
   * @return -> A list with agents.
   * @throws NoProgrammedAgentsFoundException -> when no agents found
   */
  public List<ProgrammedAgent> getAgentsForUI() throws NoProgrammedAgentsFoundException {
    List<ProgrammedAgent> programmedAgents = programmedAgentDAO.readAllProgrammedAgents();
    for(ProgrammedAgent agent : programmedAgents) {
      if("Default".equals(agent.getProgrammedAgentName())) {
        defaultAgent = agent;
      }
    }
    if(programmedAgents.isEmpty()) {
      throw new NoProgrammedAgentsFoundException("Could not find any programmed agents");
    }
    return programmedAgents;
  }

  public GameAgent createGameAgentFromProgrammedAgent(Facility facility, ProgrammedAgent programmedAgent) {
    List<GameBusinessRules> gameBusinessRules = new ArrayList<>();
    for (ProgrammedBusinessRules businessRule : programmedAgent.getProgrammedBusinessRules()) {
      gameBusinessRules.add(new GameBusinessRules(businessRule.getProgrammedBusinessRule(), businessRule.getProgrammedAST()));
    }

    return new GameAgent(programmedAgent.getProgrammedAgentName(), facility, gameBusinessRules);
  }

  public List<GameAgent> setAgentsInFacilities(Map<Facility, ProgrammedAgent> map) {
//    List<GameAgent> gameAgents = new ArrayList<>();
//    for(Map.Entry<Facility, ProgrammedAgent> set: map.entrySet()) {
//      gameAgents.add(new GameAgent(set.getValue().getProgrammedAgentName(), set.getValue().getProgrammedBusinessRules(), set.getKey()));
//    }
//    for(Map.Entry<Facility, ProgrammedAgent> set: map.entrySet()) {
//      for(GameAgent agent: gameAgents) {
//        if(!set.getKey().equals(agent.getFacility())) {
//          gameAgents.add(new GameAgent(defaultAgent.getProgrammedAgentName(), defaultAgent.getProgrammedBusinessRules(), set.getKey()));
//        }
//      }
//    }
    return null;

  }

}
