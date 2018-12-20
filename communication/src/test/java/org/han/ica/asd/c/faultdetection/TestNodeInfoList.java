package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfo;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

public class TestNodeInfoList {

    NodeInfo nodeInfo = mock(NodeInfo.class);
    NodeInfoList nodeInfoList = new NodeInfoList();

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Test if getAllIps returns the right ips")
    void TestGetAllIps() {
        String testIp1 = "TestIp1";
        String testIp2 = "TestIp2";

        NodeInfo nodeInfo1 = new NodeInfo();
        nodeInfo1.setIp(testIp1);

        NodeInfo nodeInfo2 = new NodeInfo();
        nodeInfo2.setIp(testIp2);


        nodeInfoList.add(nodeInfo1);
        nodeInfoList.add(nodeInfo2);

        List<String> result = nodeInfoList.getAllIps();

        assertTrue(result.get(0).equals(testIp1) || result.get(1).equals(testIp1));
        assertTrue(result.get(0).equals(testIp2) || result.get(1).equals(testIp2));

    }

    @Test
    @DisplayName("Test if getActiveIps returns the right ips")
    void TestGetAllActiveIps() {
        String testIp1 = "TestIp1";
        String testIp2 = "TestIp2";

        NodeInfo nodeInfo1 = new NodeInfo();
        nodeInfo1.setIp(testIp1);
        nodeInfo1.setConnected(true);

        NodeInfo nodeInfo2 = new NodeInfo();
        nodeInfo2.setIp(testIp2);
        nodeInfo2.setConnected(false);


        nodeInfoList.add(nodeInfo1);
        nodeInfoList.add(nodeInfo2);

        List<String> result = nodeInfoList.getActiveIps();

        assertEquals(1, result.size());
        assertEquals(result.get(0), testIp1);
    }

    @Test
    @DisplayName("Test if getActiveIpsWithoutLeader returns the right ips")
    void TestGetAllActiveIpsThatAreNotLeader() {
        String testIp1 = "TestIp1";
        String testIp2 = "TestIp2";

        NodeInfo nodeInfo1 = new NodeInfo();
        nodeInfo1.setIp(testIp1);
        nodeInfo1.setConnected(true);

        NodeInfo nodeInfo2 = new NodeInfo();
        nodeInfo2.setIp(testIp2);
        nodeInfo2.setConnected(true);
        nodeInfo2.setLeader(true);

        nodeInfoList.add(nodeInfo1);
        nodeInfoList.add(nodeInfo2);

        List<String> result = nodeInfoList.getActiveIpsWithoutLeader();

        assertEquals(1, result.size());
        assertEquals(result.get(0), testIp1);
    }


    @Test
    @DisplayName("Test if the getStatusOfOneNode returns the right value")
    void TestGetStatusOfOneNode() {
        String testIp1 = "TestIp1";
        String testIp2 = "TestIp2";

        NodeInfo nodeInfo1 = new NodeInfo();
        nodeInfo1.setIp(testIp1);
        nodeInfo1.setConnected(true);

        NodeInfo nodeInfo2 = new NodeInfo();
        nodeInfo2.setIp(testIp2);
        nodeInfo2.setConnected(false);

        nodeInfoList.add(nodeInfo1);

        assertTrue(nodeInfoList.getStatusOfOneNode(testIp1));
        assertFalse(nodeInfoList.getStatusOfOneNode(testIp2));
    }

    @Test
    @DisplayName("Test if AddIp function adds the ip to the nodeInfoList")
    void TestAddIp() {
        String testIp1 = "TestIp1";

        nodeInfoList.addIp(testIp1);

        assertEquals(testIp1, nodeInfoList.get(0).getIp());
    }

    @Test
    @DisplayName("Test if the UpdateIsConnected updates the value of a node")
    void TestUpdateIsConnected() {
        String testIp1 = "TestIp1";

        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setIp(testIp1);
        nodeInfo.setConnected(false);

        nodeInfoList.add(nodeInfo);

        nodeInfoList.updateIsConnected(testIp1, true);

        assertTrue(nodeInfoList.get(0).getConnected());
    }

    @Test
    @DisplayName("Test if the size method returns the right value")
    void TestSize() {

        nodeInfoList.addIp("TestIp");

        assertEquals(1, nodeInfoList.size());
    }

    @Test
    @DisplayName("Test the equals method works")
    void TestEquals() {

        NodeInfoList nodeInfoList2 = nodeInfoList;

        assertTrue(nodeInfoList2.equals(nodeInfoList2));

    }

    @Test
    @DisplayName("Test the hashcode method of the nodeInfoList")
    void TestHashCode() {
        assertEquals(new NodeInfoList().hashCode(), nodeInfoList.hashCode());
    }

    @Test
    @DisplayName("Test if the getLeaderIp works happy flow")
    void TestGetLeaderIpHappyFlow() {
        String testip = "TestIp";
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setIp(testip);
        nodeInfo.setLeader(true);
        nodeInfo.setConnected(true);

        nodeInfoList.add(nodeInfo);

        assertEquals(testip, nodeInfoList.getLeaderIp());
    }

    @Test
    @DisplayName("Test if the getLeaderIp returns null when the Leader is disconnected")
    void TestGetLeaderIpWhenLeaderIsDisconnected() {

        String testip = "TestIp";
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setIp(testip);
        nodeInfo.setLeader(true);
        nodeInfo.setConnected(false);

        nodeInfoList.add(nodeInfo);

        assertNull(nodeInfoList.getLeaderIp());

    }

    @Test
    @DisplayName("Test if the getLeaderIp returns null when there is no Leader")
    void TestGetLeaderIpWhenThereAreNoLeaders() {
        String testip = "TestIp";
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setIp(testip);
        nodeInfo.setLeader(false);
        nodeInfo.setConnected(true);

        nodeInfoList.add(nodeInfo);

        assertNull(nodeInfoList.getLeaderIp());
    }

    @Test
    @DisplayName("Test if the getLeaderIp returns null when noone is connected")
    void TestGetLeaderIpWhenThereAreNoNodesConnected() {
        String testip = "TestIp";
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setIp(testip);
        nodeInfo.setLeader(false);
        nodeInfo.setConnected(false);

        nodeInfoList.add(nodeInfo);

        assertNull(nodeInfoList.getLeaderIp());
    }

}
