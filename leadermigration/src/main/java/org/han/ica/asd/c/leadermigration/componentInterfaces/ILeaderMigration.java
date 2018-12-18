package org.han.ica.asd.c.leadermigration.componentInterfaces;

import org.han.ica.asd.c.exceptions.PlayerNotFoundException;
import org.han.ica.asd.c.leadermigration.ElectionModel;
import org.han.ica.asd.c.model.Player;

public interface ILeaderMigration {

  Player startMigration(Player[] players) throws PlayerNotFoundException;

  ElectionModel receiveElectionMessage(ElectionModel electionModel);

  void initialize();

  void receiveVictoryMessage(Player electedPlayer);

}
