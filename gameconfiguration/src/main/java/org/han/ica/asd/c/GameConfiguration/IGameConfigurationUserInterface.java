package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.List;

public interface IGameConfigurationUserInterface {

  void sendAgentsToUI(List<ProgrammedAgent> agents);
}