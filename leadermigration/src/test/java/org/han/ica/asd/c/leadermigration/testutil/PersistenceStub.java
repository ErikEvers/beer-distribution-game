package org.han.ica.asd.c.leadermigration.testutil;

import org.han.ica.asd.c.leadermigration.componentInterfaces.IPersistenceLeaderMigration;
import org.han.ica.asd.c.model.dao_model.Player;

public class PersistenceStub implements IPersistenceLeaderMigration {

	@Override
	public void saveNewLeader(Player newLeader) {

	}
}
