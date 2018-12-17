package org.han.ica.asd.c.faultdetection.nodeinfolist;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


public class NodeInfoList extends ArrayList<NodeInfo> {

    private ArrayList<NodeInfo> nodeList;

    public NodeInfoList() {
        nodeList = new ArrayList<>();
    }

    public List<String> getAllIps() {
        ArrayList<String> list = new ArrayList<>();

        for (NodeInfo node : nodeList) {
            list.add(node.getIp());
        }
        return list;
    }

    public List<String> getActiveIps() {
        ArrayList<String> list = new ArrayList<>();

        for (NodeInfo node : nodeList) {
            if (node.getConnected()) {
                list.add(node.getIp());
            }
        }
        return list;
    }

    public List<String> getActiveIpsWithoutLeader() {
        ArrayList<String> list = new ArrayList<>();

        for (NodeInfo node : nodeList) {
            if (node.getConnected() && !node.getLeader()) {
                list.add(node.getIp());
            }
        }
        return list;
    }
    public boolean getStatusOfOneNode(String ip) {
        for (NodeInfo node : nodeList) {
            if (node.getIp().equals(ip)) {
                return node.getConnected();
            }
        }
        return false;
    }

    public void addIp(String ip) {
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setIp(ip);
        nodeList.add(nodeInfo);
    }

    public void updateIsConnected(String ip, Boolean isConnected) {
        for (NodeInfo nodeInfo : nodeList) {
            if (nodeInfo.getIp().equals(ip)) {
                nodeInfo.setConnected(isConnected);
            }
        }
    }

    @Override
    public NodeInfo get(int index) {
        return nodeList.get(index);
    }

    @Override
    public boolean add(NodeInfo n) {
        nodeList.add(nodeList.size(), n);
        return true;
    }

    @Override
    public int size() {
        return nodeList.size();
    }

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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}