package org.han.ica.asd.c.faultdetection.nodeinfolist;

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
