package org.han.ica.asd.c.leadermigration.testutil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.exceptions.leadermigration.PlayerNotFoundException;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.leadermigration.ElectionHandler;
import org.han.ica.asd.c.model.interface_models.ElectionModel;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.leadermigration.IpHandler;
import org.han.ica.asd.c.leadermigration.LeaderMigration;
import org.han.ica.asd.c.interfaces.leadermigration.IConnectorForLeaderElection;
import org.han.ica.asd.c.interfaces.leadermigration.IPersistenceLeaderMigration;

public class CommunicationHelper implements IConnectorForLeaderElection {
    private LeaderMigration migrationObj;
    private Injector injector;
    private String currentPlayerIp;

    public void setInjector() {
        this.injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IConnectorForLeaderElection.class).to(CommunicationHelper.class);
                bind(IPersistenceLeaderMigration.class).to(PersistenceStub.class);
                requestStaticInjection(ElectionHandler.class);
								bind(IpHandler.class).to(IpHandlerStub.class);
            }
        });
		}

    public Player startElection(Player[] players) {
        this.setInjector();
        this.migrationObj = injector.getInstance(LeaderMigration.class);
				return this.migrationObj.startMigration(players,currentPlayerIp);
    }

    public void addObserver(IConnectorObserver observer) {

    }

    public ElectionModel sendElectionMessage(ElectionModel election, Player player) {
        if(!player.isConnected()) {
            return null;
        }
        this.setInjector();
        return injector.getInstance(LeaderMigration.class).receiveElectionMessage(election);
    }

    public void sendVictoryMessage(Player victory, Player player) {
        if(!player.isConnected()) {
            System.out.println("TODO: disconnect na elected");
        }
        this.setInjector();
				injector.getInstance(LeaderMigration.class).receiveVictoryMessage(victory);
    }

    public void setCurrentPlayerIp(String currentPlayerIp) {
        this.currentPlayerIp = currentPlayerIp;
    }
}
