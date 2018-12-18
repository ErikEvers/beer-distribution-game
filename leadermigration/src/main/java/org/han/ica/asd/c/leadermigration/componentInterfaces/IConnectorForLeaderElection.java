package org.han.ica.asd.c.leadermigration.componentInterfaces;

import org.han.ica.asd.c.leadermigration.ElectionModel;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.observers.IConnectorObserver;

public interface IConnectorForLeaderElection {

  ElectionModel sendElectionMessage(ElectionModel election, Player player);

	void sendVictoryMessage(Player victory, Player player);

  void addObserver(IConnectorObserver observer);
}
