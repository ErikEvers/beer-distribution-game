package org.han.ica.asd.c.leadermigration.componentInterfaces;

import org.han.ica.asd.c.model.domain_objects.Player;

public interface IPersistenceLeaderMigration {

  void saveNewLeader(Player newLeader);
}
