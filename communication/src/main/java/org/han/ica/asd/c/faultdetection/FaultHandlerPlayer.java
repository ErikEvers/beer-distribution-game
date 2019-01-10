package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.exceptions.leadermigration.PlayerNotFoundException;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.ILeaderMigration;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.util.List;

public class FaultHandlerPlayer {

    @Inject
    private NodeInfoList nodeInfoList;

    private List<IConnectorObserver> observers;

    private int amountOfConnectionsWithLeader;
    private int amountOfFailingIps;
    private int amountOfActiveIps;
    private int filteredAmount;

    int getAmountOfActiveIps() {
        return amountOfActiveIps;
    }

    int getAmountOfFailingIps() {
        return amountOfFailingIps;
    }

    int getAmountOfConnectionsWithLeader() {
        return amountOfConnectionsWithLeader;
    }

    FaultHandlerPlayer() {
        amountOfConnectionsWithLeader = 0;
    }

    public void reset() {
        resetAmountOfFailingIps();
        resetAmountOfConnectionsWithLeader();
        filteredAmount = 0;
    }

    public void resetAmountOfConnectionsWithLeader() {
        amountOfConnectionsWithLeader = 0;
    }

    public void incrementAmountOfConnectionsWithLeader() {
        amountOfConnectionsWithLeader++;
    }

    public void resetAmountOfFailingIps() {
        amountOfFailingIps = 0;
    }

    public void incrementAmountOfFailingIps() {
        amountOfFailingIps++;
    }

    public String whoIsDead(NodeInfoList nodeInfoList) {
        if (amountOfFailingIps == (amountOfActiveIps-filteredAmount)) {
            System.out.println("I AM DEAD");
            //TODO This should trigger Rejoin GUI and/or Request
            return "imDead";
        } else {
            if(amountOfConnectionsWithLeader == 0) {
                System.out.println("LEADER IS DEAD");
                notifyObserversLeaderDied(nodeInfoList);
                return "leaderIsDead";
            }else{
                //TODO This should start a relay
                return "leaderIsNotCompletelyDead";
            }
        }
    }

    private void notifyObserversLeaderDied(NodeInfoList nodeInfoList) {
        if (!observers.isEmpty()) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof ILeaderMigration) {
                    Player[] players = nodeInfoList.getPlayersWithoutLeader();
                    ((ILeaderMigration) observer).startMigration(players, nodeInfoList.getMyIp());
                }
            }
        }
    }



    void setAmountOfActiveIps(int amountOfActiveIps) {
        this.amountOfActiveIps = amountOfActiveIps;
    }

    void setAmountOfFailingIps(int amountOfFailingIps) {
        this.amountOfFailingIps = amountOfFailingIps;
    }

    public void setFilteredAmount(int filteredAmount) {
        this.filteredAmount = filteredAmount;
    }

    public void setObservers(List<IConnectorObserver> observers) {
        this.observers = observers;
    }

}
