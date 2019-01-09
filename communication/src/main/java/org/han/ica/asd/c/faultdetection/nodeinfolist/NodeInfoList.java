package org.han.ica.asd.c.faultdetection.nodeinfolist;


import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * This list class is used to keep all the details of all the nodes in.
 * This list has a couple of methods to make looping through the list easier.
 *
 * @author Oscar
 * @see NodeInfo
 */

public class NodeInfoList extends ArrayList<Player> {

    @Inject
    IGameStore persistence;

    private List<Player> nodeList;


    public NodeInfoList() {
        nodeList = persistence.getGameLog().getPlayers();
    }
    /**
     * Retrieves all the ip's from the 'NodeInfo' objects in the 'NodeInfoList'.
     *
     * @return A List of strings filled with all the ip's that are in the 'NodeInfoList'
     * @author Oscar
     * @see NodeInfo
     * @see NodeInfoList
     */
    public List<String> getAllIps() {
        ArrayList<String> list = new ArrayList<>();

        for (Player node : nodeList) {
            list.add(node.getIpAddress());
        }
        return list;
    }

    /**
     * Returns all the ip's that are currently connected (isConnected = true).
     * This is used to make sure the leader only tries to make connections with nodes that are known to be
     * connected to the game.
     *
     * @return A List of all the active ips/nodes.
     * @author Oscar
     * @see NodeInfoList
     */
    public List<String> getActiveIps() {
        ArrayList<String> list = new ArrayList<>();

        for (Player node : nodeList) {
            if (node.isConnected()) {
                list.add(node.getIpAddress());
            }
        }
        return list;
    }

    /**
     * Returns all the ip's that are currently connected (isConnected = true) and are not the leader(isLeader = false).
     * This is used to make sure that node only contact each other and not the leader when asking if other nodes can
     * reach the leader.
     * connected to the game.
     *
     * @return A List of all the active ips/nodes.
     * @author Oscar
     * @see NodeInfoList
     */

    public List<String> getActiveIpsWithoutLeader() {
        Player leader = persistence.getGameLog().getLeader().getPlayer();

        ArrayList<String> list = new ArrayList<>();

        for (Player node : nodeList) {
            if (node.isConnected() && node != leader) {
                list.add(node.getIpAddress());
            }
        }
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
        Player leader = persistence.getGameLog().getLeader().getPlayer();

        if (leader.isConnected()) {
                return leader.getIpAddress();
            }
        return null;
    }

//    /**
//     * Retrieves the value of 'isConnected' for the node that is linked to the given ip.
//     * For the given ip it will lookup the value of isConnected in the 'NodeInfoList'.
//     *
//     * @param ip Is the ip that links to specific node.
//     * @return The value of isConnected for the node that is linked to the ip.
//     * @author Oscar
//     * @see NodeInfoList
//     */
//    public boolean getStatusOfOneNode(String ip) {
//        for (Player node : nodeList) {
//            if (node.getIpAddress().equals(ip)) {
//                return node.isConnected();
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Creates a NodeInfo object with the given ip and adds it to the 'NodeInfoList'.
//     * It also initially sets the value of 'isConnected' to true.
//     *
//     * @param ip The ip of the node that needs to be added.
//     * @author Oscar
//     */
//    public void addIp(String ip) {
//        NodeInfo nodeInfo = new NodeInfo();
//        nodeInfo.setIp(ip);
//        nodeList.add(nodeInfo);
//    }

    /**
     * Updates the isConnected attribute of a specific node.
     * This node is identified by the ip address parameter.
     *
     * @param ip          The ip of the node that has to be updated.
     * @param isConnected The value that needs to be set to the isConnected attribute of the NodeInfo.
     * @author Oscar
     * @see NodeInfo
     */
    public void updateIsConnected(String ip, Boolean isConnected) {
        for (Player nodeInfo : nodeList) {
            if (nodeInfo.getIpAddress().equals(ip)) {
                nodeInfo.setConnected(isConnected);
            }
        }
    }

    /**
     * Retrieves the NodeInfo object for the given index.
     *
     * @param index The index of the NodeInfo that is required.
     * @return The requested NodeInfo object.
     */
    @Override
    public Player get(int index) { return nodeList.get(index);}

    /**
     * Adds a NodeInfo object to the NodeInfoList, it then returns true after it did its job.
     *
     * @param n The NodeInfo object to be added to the NodeInfoList.
     * @return True after the object is added to the list.
     */
    @Override
    public boolean add(Player n) {
        nodeList.add(nodeList.size(), n);
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
        return nodeList.size();
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

}