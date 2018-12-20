package org.han.ica.asd.c.faultdetection.nodeinfolist;


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

public class NodeInfoList extends ArrayList<NodeInfo> {

    private ArrayList<NodeInfo> nodeList;

    public NodeInfoList() {
        nodeList = new ArrayList<>();
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

        for (NodeInfo node : nodeList) {
            list.add(node.getIp());
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

        for (NodeInfo node : nodeList) {
            if (node.getConnected()) {
                list.add(node.getIp());
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
        ArrayList<String> list = new ArrayList<>();

        for (NodeInfo node : nodeList) {
            if (node.getConnected() && !node.getLeader()) {
                list.add(node.getIp());
            }
        }
        return list;
    }


    public String getLeaderIp() {
        for (NodeInfo node : nodeList) {
            if (node.getConnected() && node.getLeader()) {
                return node.getIp();
            }
        }
        return null;
    }

    /**
     * Retrieves the value of 'isConnected' for the node that is linked to the given ip.
     * For the given ip it will lookup the value of isConnected in the 'NodeInfoList'.
     *
     * @param ip Is the ip that links to specific node.
     * @return The value of isConnected for the node that is linked to the ip.
     * @author Oscar
     * @see NodeInfoList
     */
    public boolean getStatusOfOneNode(String ip) {
        for (NodeInfo node : nodeList) {
            if (node.getIp().equals(ip)) {
                return node.getConnected();
            }
        }
        return false;
    }

    /**
     * Creates a NodeInfo object with the given ip and adds it to the 'NodeInfoList'.
     * It also initially sets the value of 'isConnected' to true.
     *
     * @param ip The ip of the node that needs to be added.
     * @author Oscar
     */
    public void addIp(String ip) {
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setIp(ip);
        nodeList.add(nodeInfo);
    }

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
        for (NodeInfo nodeInfo : nodeList) {
            if (nodeInfo.getIp().equals(ip)) {
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
    public NodeInfo get(int index) {
        return nodeList.get(index);
    }

    /**
     * Adds a NodeInfo object to the NodeInfoList, it then returns true after it did its job.
     *
     * @param n The NodeInfo object to be added to the NodeInfoList.
     * @return True after the object is added to the list.
     */
    @Override
    public boolean add(NodeInfo n) {
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

        ListIterator<NodeInfo> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            NodeInfo o1 = e1.next();
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