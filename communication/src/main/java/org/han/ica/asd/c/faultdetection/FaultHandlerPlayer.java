package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.ConnectorProvider;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.ILeaderMigration;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will call methods on external interfaces when needed. Example: The leader is disconnected from the game, this
 * class will then call the 'LeaderMigration' to make sure the leader wil be replaced with a new leader.
 * It also keeps track whether the other nodes are able to reach the leader or not. And whether im able to reach other nodes.
 *
 * @author Tarik
 */
public class FaultHandlerPlayer {

    private NodeInfoList nodeInfoList;

    private List<IConnectorObserver> observers;
    private int amountOfConnectionsWithLeader;
    private int amountOfFailingIps;
    private int amountOfActiveIps;
    private int filteredAmount;

    @Inject
     private static Logger logger;

    FaultHandlerPlayer() {
        amountOfConnectionsWithLeader = 0;
    }

    /**
     * This method decides whether the leader died or the machine running this application
     * died based on the connection data.
     * It also knows if there is another problem where both didn't die but still can't connect.
     *
     * @return who died for tests
     * @author Tarik
     */
    public String whoIsDead() {
        if (amountOfFailingIps == (amountOfActiveIps - filteredAmount)) {
            //TODO This should trigger Rejoin GUI and/or Request now it sets this machine as leader so it starts agents
            notifyObserversThisMachineDied();
            logger.log(Level.INFO, "Deze machine kan niemand bereiken");
            return "imDead";
        } else {
            if (amountOfConnectionsWithLeader == 0) {
                notifyObserversLeaderDied();
                logger.log(Level.INFO, "De leider kan niet worden bereikt, en is dus uitgevallen.");
                return "leaderIsDead";
            } else {
                //TODO This should start a relay or netwok segmentations choosing a leader between players who can't connect with the leader

                //Because this machine cant reach leader
                // and there is no relay or segmentation system, the leader replaces this player with an agent
                // this means this machine also becomes a game leader and starts agents.
                notifyObserversThisMachineDied();
                return "leaderIsNotCompletelyDead";
            }
        }
    }

    /**
     * Notifies every observer that the leader can't be reached and is disconnected.
     *
     * @author Tarik
     */
    private void notifyObserversLeaderDied() {
        if (observers != null && !observers.isEmpty()) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof ILeaderMigration) {
                    Player[] players = nodeInfoList.getPlayersWithoutLeader();
                    ((ILeaderMigration) observer).startMigration(players, nodeInfoList.getMyIp());
                }
            }
        }
    }

    /**
     * Notifies leadermigration that this machine is disconnected.
     *
     * @author Tarik
     */
    private void notifyObserversThisMachineDied() {
        if (observers != null && !observers.isEmpty()) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof ILeaderMigration) {
                    List<Player> list = nodeInfoList.getPlayerList();
                    for (Player player:list) {
                        player.setConnected(false);
                        if(player.getIpAddress().equals(nodeInfoList.getMyIp())) {
                            ((ILeaderMigration) observer).receiveVictoryMessage(player);
                        }
                    }
                }
            }
        }
    }

    /**
     * Resets the values
     *
     * @author Tarik
     */
    public void reset() {
        resetAmountOfFailingIps();
        resetAmountOfConnectionsWithLeader();
        filteredAmount = 0;
    }

    /**
     * Resets the amountOfFailingIps
     *
     * @author Tarik
     */
    public void resetAmountOfFailingIps() {
        amountOfFailingIps = 0;
    }

    /**
     * Resets the amountOfConnectionsWithLeader
     *
     * @author Tarik
     */
    public void resetAmountOfConnectionsWithLeader() {
        amountOfConnectionsWithLeader = 0;
    }

    /**
     * Increments the amountOfConnectionsWithLeader
     *
     * @author Tarik
     */
    public void incrementAmountOfConnectionsWithLeader() {
        amountOfConnectionsWithLeader++;
    }

    /**
     * Increments the amountOfFailingIps
     *
     * @author Tarik
     */
    public void incrementAmountOfFailingIps() {
        amountOfFailingIps++;
    }

    /**
     * Sets new amountOfActiveIps.
     *
     * @param amountOfActiveIps New value of amountOfActiveIps.
     */
    void setAmountOfActiveIps(int amountOfActiveIps) {
        this.amountOfActiveIps = amountOfActiveIps;
    }

    /**
     * Sets new amountOfFailingIps.
     *
     * @param amountOfFailingIps New value of amountOfFailingIps.
     */
    void setAmountOfFailingIps(int amountOfFailingIps) {
        this.amountOfFailingIps = amountOfFailingIps;
    }

    /**
     * Sets new filteredAmount.
     *
     * @param filteredAmount New value of filteredAmount.
     */
    public void setFilteredAmount(int filteredAmount) {
        this.filteredAmount = filteredAmount;
    }

    /**
     * Sets new nodeInfoList.
     *
     * @param nodeInfoList New value of nodeInfoList.
     */
    void setNodeInfoList(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
    }

    /**
     * Sets new logger.
     *
     * @param logger New value of logger.
     */
    public void setLogger(Logger logger) {
        FaultHandlerPlayer.logger = logger;
    }
    /**
     * Sets new observers.
     *
     * @param observers New value of observers.
     */
    public void setObservers(List<IConnectorObserver> observers) {
        this.observers = observers;
    }

    /**
     * Gets amountOfActiveIps.
     *
     * @return Value of amountOfActiveIps.
     */
    int getAmountOfActiveIps() {
        return amountOfActiveIps;
    }

    /**
     * Gets amountOfFailingIps.
     *
     * @return Value of amountOfFailingIps.
     */
    int getAmountOfFailingIps() {
        return amountOfFailingIps;
    }

    /**
     * Gets amountOfConnectionsWithLeader.
     *
     * @return Value of amountOfConnectionsWithLeader.
     */
    int getAmountOfConnectionsWithLeader() {
        return amountOfConnectionsWithLeader;
    }
}
