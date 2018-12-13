package org.han.ica.asd.c.leadermigration.testutil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.leadermigration.*;
import org.han.ica.asd.c.observers.IConnectorObserver;

public class CommunicationHelper implements IConnectorForLeaderElection {
    private Player elected;

    private LeaderMigration migrationObj;

    public Player startElection(Player[] players) {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IConnectorForLeaderElection.class).to(CommunicationHelper.class);
            }
        });

        this.migrationObj = injector.getInstance(LeaderMigration.class);
        this.migrationObj.startMigration(players);
        return this.elected;
    }

    public void addObserver(IConnectorObserver observer) {

    }

    public ElectionModel sendElectionMessage(ElectionModel election, Player player) {
        if(!player.isConnected()) {
            return null;
        }
        return new LeaderMigration().receiveElectionMessage(election);
    }

    public void sendVictoryMessage(Player victory, Player player) {
        if(!player.isConnected()) {
            System.out.println("TODO: disconnect na elected");
        }
        new LeaderMigration().receiveVictoryMessage(new ElectionModel());
        this.elected = victory;
    }
}
