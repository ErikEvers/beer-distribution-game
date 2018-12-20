package org.han.ica.asd.c.leadermigration.componentInterfaces;

import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.leadermigration.ElectionModel;
import org.han.ica.asd.c.model.dao_model.Player;

public interface IConnectorForLeaderElection {

  ElectionModel sendElectionMessage(ElectionModel election, Player player);

	void sendVictoryMessage(Player victory, Player player);

  void addObserver(IConnectorObserver observer);
}
