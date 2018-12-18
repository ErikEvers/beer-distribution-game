package org.han.ica.asd.c.leadermigration.componentInterfaces;

import org.han.ica.asd.c.model.Player;

public interface IPersistenceLeaderMigration {

  void saveNewLeader(Player newLeader);
}
