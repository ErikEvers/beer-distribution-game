package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.observers.IConnectorObserver;

public interface IConnectorForLeaderElection {

  ElectionModel sendElectionMessage(ElectionModel election, Player player);

  //TODO vervangen door de observer
  void addObserver(IConnectorObserver observer);
}
