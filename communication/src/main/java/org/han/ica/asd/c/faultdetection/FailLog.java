package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FailLog {
    //TODO make the nodeinfolist the same over every class

    private NodeInfoList nodeInfoList;
    private HashMap<String, Integer> failLogHashMap;
    private int succesSize;

    private static final Logger LOGGER = Logger.getLogger(FailLog.class.getName());

    FailLog(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
        failLogHashMap = new HashMap<>();
        succesSize = 0;
    }

    HashMap<String, Integer> getFailLogHashMap() {
        return failLogHashMap;
    }

    //Increments the amount of fails for an Ip
    void increment(String ip) {
        putIpInListIfNotAlready(ip);
        failLogHashMap.put(ip, failLogHashMap.get(ip) + 1);

        LOGGER.log(Level.INFO,"deze wordt geincrement : {0} met als waarde : {1}", new Object[] {ip, failLogHashMap.get(ip)});
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

    int getSuccesSize() {
        //Returns the amount of successfully reached nodes.
        List<String> list = nodeInfoList.getActiveIps();
        succesSize = list.size();

        for (String ip : list) {
            if (checkIfIpIsFailed(ip)) {
                succesSize--;
            }
        }
        return succesSize;
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


}
