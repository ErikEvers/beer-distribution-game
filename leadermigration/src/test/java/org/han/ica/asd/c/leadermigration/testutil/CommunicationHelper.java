package org.han.ica.asd.c.leadermigration.testutil;

import org.han.ica.asd.c.leadermigration.*;
import org.han.ica.asd.c.observers.IConnectorObserver;

public class CommunicationHelper implements IConnectorForLeaderElection {
    private Player elected;

    private LeaderMigration migrationObj;

    public Player startElection(Player[] players) {
        this.migrationObj = new LeaderMigration();
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
        new LeaderMigration().receiveVictoryMessage(victory);
        this.elected = victory;
    }
}
