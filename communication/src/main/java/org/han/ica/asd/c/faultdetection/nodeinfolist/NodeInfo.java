package org.han.ica.asd.c.faultdetection.nodeinfolist;

/**
 * In this pojo class all the details of a node are kept.
 * This POJO class is used in the 'NodeInfoList'.<br>
 * However this class will probably be removed when integrating with the persistence layer/component.<br>
 * If it doesn't get removed it will have to be modified/extended.
 *
 * @author Oscar
 * @see NodeInfoList
 */

public class NodeInfo {

    private String ip;
    private Boolean isConnected = true;
    private Boolean isLeader = false;

    public NodeInfo(){}

    public NodeInfo(String ip, Boolean isConnected, Boolean isLeader) {
        this.ip = ip;
        this.isConnected = isConnected;
        this.isLeader = isLeader;
    }

    public Boolean getLeader() {
        return isLeader;
    }

    public void setLeader(Boolean leader) {
        isLeader = leader;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }
}
