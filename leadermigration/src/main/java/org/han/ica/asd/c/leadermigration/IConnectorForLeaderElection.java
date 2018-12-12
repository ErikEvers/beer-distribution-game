package org.han.ica.asd.c.leadermigration;

public interface IConnectorForLeaderElection {

  void sendElectionMessage(ElectionModel election, Player player);

  void sendAliveMessage(ElectionModel election, Player player);

  void sendVictoryMessage(Player victory, Player player);
}
