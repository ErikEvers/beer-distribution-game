package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to keep track of the amount of fail for every ip .
 *
 * @author Oscar, Tarik
 * @see FaultDetectorLeader
 */
public class FailLog {
    @Inject
    private static Logger logger;

    NodeInfoList nodeInfoList;

    private HashMap<String, Integer> failLogHashMap = new HashMap<>();
    private int successSize = 0;

    public FailLog() {
        //For inject purposes
    }

    /**
     * Increments the amount of fails for an Ip.
     *
     * @param ip The Ip where the fail amount needs to be incremented.
     * @author Oscar
     */
    void increment(String ip) {
        putIpInListIfNotAlready(ip);
        failLogHashMap.put(ip, failLogHashMap.get(ip) + 1);
        logger.log(Level.INFO, "deze wordt geincrement : {0} met als waarde : {1}", new Object[]{ip, failLogHashMap.get(ip)});
    }

    /**
     * Sets the value of the amount of fails to 0 for an Ip.
     *
     * @param ip The Ip where the fail amount needs to be reset.
     * @author Oscar
     */
    public void reset(String ip) {
        putIpInListIfNotAlready(ip);
        failLogHashMap.put(ip, 0);
    }

    /**
     * Checks if the amount of fails for an Ip equals 3.
     *
     * @param ip The Ip that need to be checked.
     * @return boolean indicating whether ip has failed three times.
     * @author Oscar
     */
    boolean checkIfIpIsFailed(String ip) {
        putIpInListIfNotAlready(ip);
        return failLogHashMap.get(ip) == 3;
    }

    /**
     * Checks if the Ip is already in the failLog. It returns true when it isn't.
     * If the Ip is already in the failLog it checks if the amount of fails equals 0.
     *
     * @param ip The Ip that needs to be checked.
     * @return boolean indicating whether ip is reachable
     * @author Oscar
     */
    boolean isAlive(String ip) {
        if (!failLogHashMap.containsKey(ip)) {
            return true;
        } else return failLogHashMap.get(ip) == 0;
    }

    /**
     * Returns the amount of successfully reached nodes.
     *
     * @return amount of reachable nodes.
     * @author Oscar
     */
    int getSuccessSize() {
        List<String> list = nodeInfoList.getActiveIpsWithoutLeader();
        successSize = list.size();
            for (String ip : list) {
                if (checkIfIpIsFailed(ip)) {
                    successSize--;
                }
            }
        return successSize;
    }

    /**
     * Puts the Ip in the faillog if it isnt already in there.
     *
     * @param ip The Ip value.
     * @author Oscar
     */
    private void putIpInListIfNotAlready(String ip) {
        if (!failLogHashMap.containsKey(ip)) {
            add(ip);
        }
    }

    /**
     * Adds an Ip to the list.
     *
     * @param ip Added Ip value.
     * @author Oscar
     */
    void add(String ip) {
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

    /**
     * Gets failLogHashMap.
     *
     * @return Value of failLogHashMap.
     */
    HashMap<String, Integer> getFailLogHashMap() {
        return failLogHashMap;
    }
}
