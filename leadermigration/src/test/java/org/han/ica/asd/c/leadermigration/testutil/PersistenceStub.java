package org.han.ica.asd.c.leadermigration.testutil;

import org.han.ica.asd.c.interfaces.leadermigration.IPersistenceLeaderMigration;
import org.han.ica.asd.c.model.domain_objects.Player;

public class PersistenceStub implements IPersistenceLeaderMigration {

	@Override
	public void saveNewLeader(Player newLeader) {

	}
}
