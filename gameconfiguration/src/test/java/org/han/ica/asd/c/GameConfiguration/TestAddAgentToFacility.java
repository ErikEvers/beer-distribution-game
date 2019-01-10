package org.han.ica.asd.c.GameConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.gameconfiguration.GameAgentController;
import org.han.ica.asd.c.model.domain_objects.*;
import org.han.ica.asd.c.gameconfiguration.IGameConfigurationUserInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class TestAddAgentToFacility {

  // Fields for Injector
  private GameAgentController gameAgentController;
  private FacilityType facilityType;
  private IGameConfigurationUserInterface iGameConfigurationUserInterface;
  private IDatabaseConnection iDatabaseConnection;

  // Fields for tests.
  private String[] programmedAgentNames = {"Agent 1", "Agent 2", "Agent 3", "Agent 4", "Default", "Agent 6", "Kees",
      "jan", "Piet", "Joost", "Brenda", "Tester", "Administrator", "Agent 14", "agent 15"};


  private List<ProgrammedBusinessRules> businessRules;
  private List<GameAgent> gameAgents;
  private List<ProgrammedAgent> programmedAgents;
  private List<Facility> gameFacilities;



  @BeforeEach
  public void setup(){
    // Mock injected fields
    gameAgentController = mock(GameAgentController.class);
    facilityType = mock(FacilityType.class);
    iGameConfigurationUserInterface = mock(IGameConfigurationUserInterface.class);
    iDatabaseConnection = mock(IDatabaseConnection.class);
    // Add injected fields to Injector
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(FacilityType.class).annotatedWith(Names.named("FacilityType")).toInstance(facilityType);
        bind(IGameConfigurationUserInterface.class).toInstance(iGameConfigurationUserInterface);
        bind(IDatabaseConnection.class).toInstance(iDatabaseConnection);
      }
    });
    gameAgentController = injector.getInstance(GameAgentController.class);
    // Test fields instantiate.
    businessRules = new ArrayList<>();
    gameAgents = new ArrayList<>();
    programmedAgents = new ArrayList<>();
    gameFacilities = new ArrayList<>();

    for (String name: programmedAgentNames) {
      businessRules.add(new ProgrammedBusinessRules(name, name));
    }
    for(int i = 0; i < programmedAgentNames.length; ++i) {
      programmedAgents.add(new ProgrammedAgent(programmedAgentNames[i], businessRules));
    }
  }

  @Test
  public void setDefaultAgentInFacilitiesTest() {
    Map<Facility, ProgrammedAgent> testData = new HashMap<>();
    for(int i = 0; i < programmedAgentNames.length; ++i)
      testData.put(new Facility(facilityType, i), null);
    for(Map.Entry<Facility, ProgrammedAgent> set: testData.entrySet())
      gameFacilities.add(set.getKey());
    for(int i = 0; i < programmedAgentNames.length; ++i)
      gameAgents.add(new GameAgent(programmedAgentNames[i], businessRules, gameFacilities.get(i)));
    List<GameAgent> testedList = gameAgentController.setAgentsInFacilities(testData, programmedAgents);
    Assertions.assertEquals(gameAgents.size(), testedList.size());
  }

  @Test
  public void setAssignedAgentsInFacilities() {
    Map<Facility, ProgrammedAgent> facilityProgrammedAgentMap = new HashMap<>();
    for(int i = 0; i < programmedAgentNames.length; ++i)
      facilityProgrammedAgentMap.put(new Facility(facilityType, i), programmedAgents.get(i));
    for(Map.Entry<Facility, ProgrammedAgent> set: facilityProgrammedAgentMap.entrySet())
      gameFacilities.add(set.getKey());
    for(int i = 0; i < programmedAgentNames.length; ++i)
      gameAgents.add(new GameAgent(programmedAgentNames[i], businessRules, gameFacilities.get(i)));
    List<GameAgent> testedList = gameAgentController.setAgentsInFacilities(facilityProgrammedAgentMap, programmedAgents);
    Assertions.assertEquals(gameAgents.size(), testedList.size());
  }
}
