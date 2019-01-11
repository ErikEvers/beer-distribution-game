package org.han.ica.asd.c.faultdetection.nodeinfolist;


import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import static org.han.ica.asd.c.faultdetection.nodeinfolist.Condition.CONNECTED;
import static org.han.ica.asd.c.faultdetection.nodeinfolist.Condition.CONNECTEDWITHOUTLEADER;
import static org.han.ica.asd.c.faultdetection.nodeinfolist.Condition.UNFILTERED;

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

    public NodeInfoList() {
    //inject
    }

    public NodeInfoList(Leader leader, List<Player> playerList) {
        this.leader = leader;
        this.playerList = playerList;
    }

    public void init(List<Player> playerList, Leader leader){
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
     * @return A (filtered) List.
     * @author Tarik
     * @see NodeInfoList
     */
    public List<String>  getIpsFromPlayerList(Condition condition){
        ArrayList<String> list = new ArrayList<>();
        Player leaderPlayer = this.leader.getPlayer();
        playerList.forEach((node) -> {
            switch (condition) {
                case UNFILTERED:
                    list.add(node.getIpAddress());
                    break;
                case CONNECTED:
                    if(node.isConnected()) list.add(node.getIpAddress());
                    break;
                case CONNECTEDWITHOUTLEADER:
                    if (node.isConnected() && node != leaderPlayer) list.add(node.getIpAddress());
                    break;
            }
        });
        return list;
    }

    /**
     * Retreives the ip of the leader when there is one. If there isnt one it returns null.
     * Used by the 'MessageProcessor'
     *
     * @return String of the ip of the leader, or null when there isnt a leader active.
     * @author Oscar
     * @see org.han.ica.asd.c.messagehandler.MessageProcessor
     */
    public String getLeaderIp() {
        Player leaderPlayer = this.leader.getPlayer();
        if (leaderPlayer.isConnected()) {
                return leaderPlayer.getIpAddress();
            }
        return null;
    }

    /**
     * Updates the isConnected attribute of a specific node.
     * This node is identified by the ip address parameter.
     *
     * @param ip          The ip of the node that has to be updated.
     * @param isConnected The value that needs to be set to the isConnected attribute of the 'Player' Object.
     * @author Oscar
     * @see Player
     */
    public void updateIsConnected(String ip, Boolean isConnected) {
        for (Player player : playerList) {
            if (player.getIpAddress().equals(ip)) {
                player.setConnected(isConnected);
            }
        }
    }

    /**
     * Retrieves the 'Player' object for the given index.
     *
     * @param index The index of the 'Player' object that is required.
     * @return The requested 'Player' object.
     */
    @Override
    public Player get(int index) { return playerList.get(index);}

    /**
     * Adds a 'Player' object to the PlayerList, it then returns true after it did its job.
     *
     * @param p The 'Player' object to be added to the PlayerList.
     * @return True after the object is added to the list.
     */
    @Override
    public boolean add(Player p) {
        playerList.add(playerList.size(), p);
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
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        ListIterator<Player> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            Player o1 = e1.next();
            Object o2 = e2.next();
            if (!(Objects.equals(o1, o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
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

    public Leader getLeader(){
        return leader;
    }
}