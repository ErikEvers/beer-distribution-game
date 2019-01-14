package org.han.ica.asd.c.interfaces.gameconfiguration;

import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.List;

public interface IPersistenceProgrammedAgents {

  List<ProgrammedAgent> readAllAgents();
}
