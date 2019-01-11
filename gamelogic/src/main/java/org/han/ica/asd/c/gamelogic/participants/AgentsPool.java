package org.han.ica.asd.c.gamelogic.participants;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;

import java.util.ArrayList;
import java.util.List;

public class AgentsPool {
    private List<IParticipant> agents = new ArrayList<>();

    public AgentsPool(){

    }

    public void addAgent(Agent agent){
        this.agents.add(agent);
    }

    public void removeAgent(Agent agent){
        this.agents.remove(agent);
    }

    public List<IParticipant> getAllAgents(){
        return this.agents;
    }


}
