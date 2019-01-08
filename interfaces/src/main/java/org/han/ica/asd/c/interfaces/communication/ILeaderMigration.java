package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.model.interface_models.ElectionModel;
import org.han.ica.asd.c.exceptions.leadermigration.PlayerNotFoundException;
import org.han.ica.asd.c.model.domain_objects.Player;

public interface ILeaderMigration {

  Player startMigration(Player[] players) throws PlayerNotFoundException;

  ElectionModel receiveElectionMessage(ElectionModel electionModel);

  void initialize();

  void receiveVictoryMessage(Player electedPlayer);

}
