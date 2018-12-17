package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.model.Player;

public interface ILeaderMigration {

  Player startMigration(Player[] players);

  ElectionModel receiveElectionMessage(ElectionModel electionModel);

  void initialize();

  void receiveVictoryMessage(Player electedPlayer);

}
