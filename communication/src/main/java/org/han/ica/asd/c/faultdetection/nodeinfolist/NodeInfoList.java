package org.han.ica.asd.c.faultdetection.nodeinfolist;

import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.messagehandler.MessageProcessor;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.ArrayList;
import java.util.List;

import static org.han.ica.asd.c.faultdetection.nodeinfolist.Condition.*;

/**
 * This list class is used to keep all the details of all the nodes in.
 * This list has a couple of methods to make looping through the list easier.
 *
 * @author Oscar, Tarik
 * @see Player
 */
public class NodeInfoList extends ArrayList<Player> {
    private List<Player> playerList;
    private Leader leader;
    private String myIp;

    public NodeInfoList() {
        //inject
    }

    public NodeInfoList(Leader leader, List<Player> playerList) {
        this.leader = leader;
        this.playerList = playerList;
    }

    /**
     * Initializes the nodeInfoList with the references to the Player list
     * and the leader from the persistence component
     *
     * @param playerList The list with the 'Player' objects from the persistence layer.
     * @param leader     The Leader object from the persistence layer.
     * @author Oscar, Tarik
     * @see NodeInfoList
     * @see IGameStore
     */
    public void init(List<Player> playerList, Leader leader) {
        this.playerList = playerList;
        this.leader = leader;
    }

    /**
     * Retrieves all the ip's from the 'Player' objects in the 'PlayerList'.
     *
     * @return A List of strings filled with all the ip's that are in the 'PlayerList'
     * @author Oscar, Tarik
     * @see Player
     * @see NodeInfoList
     */
    public List<String> getAllIps() {
        return getIpsFromPlayerList(UNFILTERED);
    }

    /**
     * Returns all the ip's that are currently connected (isConnected = true).
     * This is used to make sure the leader only tries to make connections with nodes that are known to be
     * connected to the game.
     *
     * @return A List of all the active ips/nodes.
     * @author Oscar, Tarik
     * @see NodeInfoList
     */
    public List<String> getActiveIps() {
        return getIpsFromPlayerList(CONNECTED);
    }

    /**
     * Returns all the ip's that are currently connected (isConnected = true) and are not the leader(isLeader = false).
     * This is used to make sure that node only contact each other and not the leader when asking if other nodes can
     * reach the leader.
     * connected to the game.
     *
     * @return A List of all the active ips/nodes.
     * @author Oscar, Tarik
     * @see NodeInfoList
     */
    public List<String> getActiveIpsWithoutLeader() {
        return getIpsFromPlayerList(CONNECTEDWITHOUTLEADER);
    }

    /**
     * Returns a list with ips after filtering with predefined conditions.
     *
     * @param condition The enum with the requested condition
     * @return A (filtered) List.
     * @author Tarik
     * @see Condition
     * @see NodeInfoList
     */
    private List<String> getIpsFromPlayerList(Condition condition) {
        ArrayList<String> list = new ArrayList<>();
        Player leaderPlayer = this.leader.getPlayer();
        playerList.forEach((node) -> {
            switch (condition) {
                case UNFILTERED:
                    list.add(node.getIpAddress());
                    break;
                case CONNECTED:
                    if (node.isConnected()) list.add(node.getIpAddress());
                    break;
                case CONNECTEDWITHOUTLEADER:
                    if (node.isConnected() && (!node.getPlayerId().equals(leaderPlayer.getPlayerId()))) list.add(node.getIpAddress());
                    break;
                default:
                    break;
            }
        });
        return list;
    }

    /**
     * Returns the player array without the leader.
     *
     * @return A (filtered) Player Array.
     * @author Tarik
     * @see NodeInfoList
     */
    public Player[] getPlayersWithoutLeader() {
        Player leaderPlayer = this.leader.getPlayer();

        List<Player> playerListCopy = new ArrayList<>(playerList);
        playerListCopy.removeIf(p -> p == leaderPlayer);

        return playerListCopy.toArray(new Player[0]);
    }

    /**
     * Retreives the ip of the leader when there is one. If there isnt one it returns null.
     * Used by the 'MessageProcessor'
     *
     * @return String of the ip of the leader, or null when there isnt a leader active.
     * @author Oscar
     * @see MessageProcessor
     */
    public String getLeaderIp() {
        Player leaderPlayer = this.leader.getPlayer();
        if (leaderPlayer.isConnected()) {
            return leaderPlayer.getIpAddress();
        }
        return null;
    }

    /**
     * Updates the isConnected attribute of a specific player using a recursive function.
     * This node is identified by the ip address parameter.
     *
     * @param ip          The ip of the node that has to be updated.
     * @param isConnected The value that needs to be set to the isConnected attribute of the 'Player' Object.
     * @author Oscar
     * @see Player
     */
    public void updateIsConnected(String ip, boolean isConnected) {
        updatePlayerIsConnectedRecursion(playerList.size() - 1, ip, isConnected);
    }

    /**
     * Retrieves the specific Player from the list recursively, and updates the value of isConnected of a specific player.
     *
     * @param n           which index in the list to check.
     * @param ip          the identifier used to identify which player needs to be updated.
     * @param isConnected the value with which the isConnected value needs to be updated.
     * @author Oscar
     */
    private void updatePlayerIsConnectedRecursion(int n, String ip, boolean isConnected) {
        Player player = playerList.get(n);
        if (player.getIpAddress().equals(ip)) {
            player.setConnected(isConnected);
        } else if (n > 0) {
            updatePlayerIsConnectedRecursion(n - 1, ip, isConnected);
        }
    }

    /**
     * Retrieves the 'Player' object for the given index.
     *
     * @param index The index of the 'Player' object that is required.
     * @return The requested 'Player' object.
     */
    @Override
    public Player get(int index) {
        return playerList.get(index);
    }

    /**
     * Adds a 'Player' object to the PlayerList, it then returns true after it did its job.
     *
     * @param p The 'Player' object to be added to the PlayerList.
     * @return True after the object is added to the list.
     */
    @Override
    public boolean add(Player p) {
        playerList.add(p);
        return true;
    }

    /**
     * Returns the amount of items in the 'NodeInfoList'.
     *
     * @return The amount of items in the 'NodeInfoList'.
     * @see NodeInfoList
     */
    @Override
    public int size() {
        return playerList.size();
    }

    /**
     * Checks whether the object o equals this 'NodeInfoList' class.
     *
     * @param o The object that is compared to this 'NodeInfoList'.
     * @return True when the object o equals 'NodeInfoList'. false when different.
     * @author Oscar
     * @see NodeInfoList
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        return false;
    }

    /**
     * Returns the hashCode of the 'NodeInfoList' instance.
     *
     * @return Returns the hashCode of this object.
     * @author Oscar
     * @see NodeInfoList
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Gets leader.
     *
     * @return Value of leader.
     */
    public Leader getLeader() {
        return leader;
    }

    /**
     * Gets playerList.
     *
     * @return Value of playerList.
     */
    public List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * Gets myIp.
     *
     * @return Value of myIp.
     */
    public String getMyIp() {
        return myIp;
    }

    /**
     * Sets new myIp.
     *
     * @param myIp New value of myIp.
     */
    public void setMyIp(String myIp) {
        this.myIp = myIp;
    }
}