package org.han.ica.asd.c.interfaces.leadermigration;

import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.model.interface_models.ElectionModel;
import org.han.ica.asd.c.model.domain_objects.Player;

public interface IConnectorForLeaderElection {

  ElectionModel sendElectionMessage(ElectionModel election, Player player);

	void sendVictoryMessage(Player victory, Player player);

  void addObserver(IConnectorObserver observer);
}
