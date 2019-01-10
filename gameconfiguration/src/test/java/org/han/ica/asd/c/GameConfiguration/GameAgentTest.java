package org.han.ica.asd.c.GameConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.gameconfiguration.GameAgentController;
import org.han.ica.asd.c.model.domain_objects.*;
import org.han.ica.asd.c.gameconfiguration.IGameAgentController;
import org.han.ica.asd.c.gameconfiguration.IGameConfigurationUserInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

public class GameAgentTest {

  private IGameAgentController gameAgentController;
  private IGameConfigurationUserInterface gameConfigurationUserInterface;
  private ProgrammedAgentDAO programmedAgentDAO;
  private IDatabaseConnection databaseConnection;
  private List<ProgrammedAgent> programmedAgents;
  private List<ProgrammedBusinessRules> programmedBusinessRules;
  private List<GameAgent> gameAgents;
  private FacilityType facilityType;

  @BeforeEach
  public void setup() {
    gameAgents = mock(List.class);
    programmedAgentDAO = mock(ProgrammedAgentDAO.class);
    gameConfigurationUserInterface = mock(IGameConfigurationUserInterface.class);
    databaseConnection = mock(IDatabaseConnection.class);
    programmedBusinessRules = mock(List.class);
    gameAgents = mock(List.class);
    facilityType = mock(FacilityType.class);

    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ProgrammedAgentDAO.class).toInstance(programmedAgentDAO);
        bind(IGameConfigurationUserInterface.class).toInstance(gameConfigurationUserInterface);
        bind(IDatabaseConnection.class).toInstance(databaseConnection);
        bind(List.class).annotatedWith(Names.named("ProgrammedBusinessRules")).toInstance(programmedBusinessRules);
        bind(List.class).annotatedWith(Names.named("GameAgents")).toInstance(gameAgents);
        bind(FacilityType.class).toInstance(facilityType);
      }
    });
    gameAgentController = mock(IGameAgentController.class);
    gameAgentController = injector.getInstance(GameAgentController.class);
  }

  @Test
  public void testGetAgents() {
    try {
      gameAgentController.getAgentsForUI();
    } catch (NoProgrammedAgentsFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetAgentsListNotEmpty() {
    List<ProgrammedAgent> programmedAgents = new ArrayList<>();
    List<ProgrammedBusinessRules> programmedBusinessRules = mock(List.class);
    for(int i = 0; i < 5; ++i) {
      programmedAgents.add(new ProgrammedAgent("Kees", programmedBusinessRules));
    }
    try {
      when(programmedAgentDAO.readAllProgrammedAgents()).thenReturn(programmedAgents);
      gameAgentController.getAgentsForUI();
    } catch (NoProgrammedAgentsFoundException e) {
      e.getMessage();
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetAgentsException() {
    List<ProgrammedAgent> programmedAgents = new ArrayList<>();
    try {
      when(programmedAgentDAO.readAllProgrammedAgents()).thenReturn(programmedAgents);
      gameAgentController.getAgentsForUI();
      doThrow(mock(NoProgrammedAgentsFoundException.class)).when(gameAgentController).getAgentsForUI();
    } catch (NoProgrammedAgentsFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void setDefaultAgentInFacilitiesTest() {
    Map<Facility, ProgrammedAgent> facilityProgrammedAgentMap = new HashMap<>();
    Map<Facility, ProgrammedAgent> testData = new HashMap<>();
    String[] programmedAgentNames = {"Agent 1", "Agent 2", "Agent 3", "Agent 4", "Default", "Agent 6", "Kees",
        "jan", "Piet", "Joost", "Brenda", "Tester", "Administrator", "Agent 14", "agent 15"};
    List<ProgrammedBusinessRules> businessRules = new ArrayList<>();
    List<GameAgent> gameAgents = new ArrayList<>();
    List<ProgrammedAgent> programmedAgents = new ArrayList<>();

    for(int i = 0; i < programmedAgentNames.length; ++i) {
      businessRules.add(new ProgrammedBusinessRules(programmedAgentNames[i], programmedAgentNames[i]));
      programmedAgents.add(new ProgrammedAgent(programmedAgentNames[i], businessRules));
    }
    for(int i = 0; i < programmedAgentNames.length; ++i) {
      facilityProgrammedAgentMap.put(new Facility(facilityType, i), new ProgrammedAgent(programmedAgentNames[i], businessRules));
      testData.put(new Facility(facilityType, i), null);
    }
    List<Facility> gameFacilities = new ArrayList<>();
    for(Map.Entry<Facility, ProgrammedAgent> set: facilityProgrammedAgentMap.entrySet()) {
      gameFacilities.add(set.getKey());
    }
    for(int i = 0; i < programmedAgentNames.length; ++i) {
      this.gameAgents.add(new GameAgent(programmedAgentNames[i], businessRules, gameFacilities.get(i)));
      gameAgents.add(new GameAgent(programmedAgentNames[i], businessRules, gameFacilities.get(i)));
    }
    List<GameAgent> testedList = gameAgentController.setAgentsInFacilities(facilityProgrammedAgentMap, programmedAgents);
    Assertions.assertEquals(gameAgents.size(), testedList.size());
  }

}
