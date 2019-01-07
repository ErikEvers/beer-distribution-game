package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FailLog {
    private HashMap<String, Integer> failLogHashMap = new HashMap<>();
    private int successSize = 0;

    @Inject
    private static Logger logger;

    @Inject
    NodeInfoList nodeInfoList;

    public FailLog() {
        //for inject purposes
    }

    HashMap<String, Integer> getFailLogHashMap() {
        return failLogHashMap;
    }

    //Increments the amount of fails for an Ip
    void increment(String ip) {
        putIpInListIfNotAlready(ip);
        failLogHashMap.put(ip, failLogHashMap.get(ip) + 1);

        logger.log(Level.INFO, "deze wordt geincrement : {0} met als waarde : {1}", new Object[]{ip, failLogHashMap.get(ip)});
    }

    public void reset(String ip) {
        //Sets the value of the amount of fails to 0 for an Ip
        putIpInListIfNotAlready(ip);
        failLogHashMap.put(ip, 0);
    }

    boolean checkIfIpIsFailed(String ip) {
        //Checks if the amount of fails for an Ip equals 3.
        putIpInListIfNotAlready(ip);

        return failLogHashMap.get(ip) == 3;
    }

    boolean isAlive(String ip) {
        //Checks if the Ip is already in the failLog. It returns true when it isn't.
        //If the Ip is already in the failLog it checks if the amount of fails equals 0.
        if (!failLogHashMap.containsKey(ip)) {
            return true;
        } else return failLogHashMap.get(ip) == 0;
    }

    int getSuccessSize() {
        //Returns the amount of successfully reached nodes.
        List<String> list = nodeInfoList.getActiveIps();
        successSize = list.size();

        for (String ip : list) {
            if (checkIfIpIsFailed(ip)) {
                successSize--;
            }
        }
        return successSize;
    }

    private void putIpInListIfNotAlready(String ip) {
        //Puts the Ip in the faillog if it isnt already in there.
        if (!failLogHashMap.containsKey(ip)) {
            add(ip);
        }
    }

    void add(String ip) {
        //Adds an Ip to the list.
        failLogHashMap.put(ip, 0);
    }

    /**
     * Sets new nodeInfoList.
     *
     * @param nodeInfoList New value of nodeInfoList.
     */
    public void setNodeInfoList(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
    }
}
