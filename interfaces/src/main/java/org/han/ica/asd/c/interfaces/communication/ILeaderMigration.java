package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.model.dao_model.Player;
import org.han.ica.asd.c.model.interface_models.ElectionModel;
import org.han.ica.asd.c.model.interface_models.PlayerNotFoundException;

public interface ILeaderMigration {

  Player startMigration(Player[] players) throws PlayerNotFoundException;

  ElectionModel receiveElectionMessage(ElectionModel electionModel);

  void initialize();

  void receiveVictoryMessage(Player electedPlayer);

}
