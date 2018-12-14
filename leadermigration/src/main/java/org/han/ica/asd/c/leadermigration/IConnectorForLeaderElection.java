package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.observers.IConnectorObserver;

public interface IConnectorForLeaderElection {

  ElectionModel sendElectionMessage(ElectionModel election, Player player);

	void sendVictoryMessage(Player victory, Player player);

  //TODO vervangen door de observer
  void addObserver(IConnectorObserver observer);
}
