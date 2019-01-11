package org.han.ica.asd.c.gameconfiguration;

import javafx.scene.control.Alert;
import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedBusinessRules;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GameAgentService implements IGameAgentService {

    @Inject
    private ProgrammedAgentDAO programmedAgentDAO;
    private ProgrammedAgent defaultAgent;

    /**
     * Get all the ProgrammedAgents from the database. When the persistence layer returns no ProgrammedAgents, this
     * function will throw a Exception.
     *
     * @return -> A list with agents.
     * @throws NoProgrammedAgentsFoundException -> when no agents found
     */
    public List<ProgrammedAgent> getAgentsForUI() throws NoProgrammedAgentsFoundException {
        List<ProgrammedAgent> programmedAgents = programmedAgentDAO.readAllProgrammedAgents();
        for (ProgrammedAgent agent : programmedAgents) {
            if ("Default".equals(agent.getProgrammedAgentName())) {
                defaultAgent = agent;
            }
        }
        if (programmedAgents.isEmpty()) {
            throw new NoProgrammedAgentsFoundException("Could not find any programmed agents");
        }
        return programmedAgents;
    }

    /**
     * Creates a new GameAgent instance based on the supplied ProgrammedAgent and the Facility to which the GameAgent will be assigned.
     *
     * @param facility        the facility which the GameAgent will control
     * @param programmedAgent the Agent configuration that will control the facility
     */
    public GameAgent createGameAgentFromProgrammedAgent(Facility facility, ProgrammedAgent programmedAgent) {
        List<GameBusinessRules> gameBusinessRules = new ArrayList<>();
        for (ProgrammedBusinessRules businessRule : programmedAgent.getProgrammedBusinessRules()) {
            gameBusinessRules.add(new GameBusinessRules(businessRule.getProgrammedBusinessRule(), businessRule.getProgrammedAST()));
        }

        return new GameAgent(programmedAgent.getProgrammedAgentName(), facility, gameBusinessRules);
    }

    public BeerGame fillEmptyFacilitiesWithDefaultAgents(BeerGame beerGame) {
        if (throwAlertIfNoDefaultAgentAvailable(beerGame)) return beerGame;

        List<Facility> facilitiesNotTaken = new ArrayList<>(beerGame.getConfiguration().getFacilities());
        for (int i = 0; i < beerGame.getConfiguration().getFacilities().size(); i++) {
            for (int j = 0; j < beerGame.getAgents().size(); j++) {
                if (beerGame.getConfiguration().getFacilities().get(i).getFacilityId() == beerGame.getAgents().get(j).getFacility().getFacilityId()) {
                 facilitiesNotTaken.set(i, null);
                 break;
                }
            }
        }

        for (Facility f : facilitiesNotTaken) {
            if (f != null) {
                beerGame.getAgents().add(createGameAgentFromProgrammedAgent(f, defaultAgent));
            }
        }
        return beerGame;
    }

    private boolean throwAlertIfNoDefaultAgentAvailable(BeerGame beerGame) {
        if (defaultAgent == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "There is no local default agent!");
            alert.setHeaderText("Warning!");
            alert.show();
            return true;
        }
        return false;
    }
}