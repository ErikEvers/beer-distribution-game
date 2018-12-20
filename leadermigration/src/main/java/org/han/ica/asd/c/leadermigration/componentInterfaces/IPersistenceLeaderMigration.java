package org.han.ica.asd.c.leadermigration.componentInterfaces;

import org.han.ica.asd.c.model.dao_model.Player;

public interface IPersistenceLeaderMigration {

  void saveNewLeader(Player newLeader);
}
