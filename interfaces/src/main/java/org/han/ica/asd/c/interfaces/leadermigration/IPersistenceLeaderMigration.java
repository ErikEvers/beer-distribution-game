package org.han.ica.asd.c.interfaces.leadermigration;

import org.han.ica.asd.c.model.dao_model.Player;

public interface IPersistenceLeaderMigration {

  void saveNewLeader(Player newLeader);
}
