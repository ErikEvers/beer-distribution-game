package org.han.ica.asd.c.leadermigration.testutil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.han.ica.asd.c.leadermigration.*;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.observers.IConnectorObserver;

import java.util.logging.Logger;

public class CommunicationHelper implements IConnectorForLeaderElection {
    private LeaderMigration migrationObj;
    private Injector injector;

    private void setInjector() {
			injector = Guice.createInjector(new AbstractModule() {
				@Override
				protected void configure() {
					bind(IConnectorForLeaderElection.class).to(CommunicationHelper.class);
					bind(IPersistenceLeaderMigration.class).to(PersistenceStub.class);
					requestStaticInjection(ElectionHandler.class);
				}
			});
		}

    public Player startElection(Player[] players) {
    		setInjector();
        this.migrationObj = injector.getInstance(LeaderMigration.class);
				return this.migrationObj.startMigration(players);
    }

    public void addObserver(IConnectorObserver observer) {

    }

    public ElectionModel sendElectionMessage(ElectionModel election, Player player) {
        if(!player.isConnected()) {
            return null;
        }
				setInjector();
        return injector.getInstance(LeaderMigration.class).receiveElectionMessage(election);
    }

    public void sendVictoryMessage(Player victory, Player player) {
        if(!player.isConnected()) {
            System.out.println("TODO: disconnect na elected");
        }
				setInjector();
				injector.getInstance(LeaderMigration.class).receiveVictoryMessage(victory);
    }
}
