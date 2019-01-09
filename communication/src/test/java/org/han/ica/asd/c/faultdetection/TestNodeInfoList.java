package org.han.ica.asd.c.faultdetection;

import junit.framework.TestCase;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TestNodeInfoList {

    NodeInfoList nodeInfoList;
    Player leaderPlayer;
    Leader leader;

    private String testIp1;
    private String testIp2;


    @BeforeEach
    void setUp() {
        testIp1 = "TestIp1";
        testIp2 = "TestIp2";
        Player leaderPlayer = mock(Player.class);
        Leader leader = new Leader(leaderPlayer);
        List<Player> playerList = spy(new ArrayList<>());
        nodeInfoList = new NodeInfoList(leader,playerList);
    }

    @Test
    @DisplayName("Test if getAllIps returns the right ips")
    void TestGetAllIps() {

        Player player1 = new Player();
        player1.setIpAddress(testIp1);
        nodeInfoList.add(player1);

        Player player2 = new Player();
        player2.setIpAddress(testIp2);
        nodeInfoList.add(player2);

        List<String> result = nodeInfoList.getAllIps();

        assertTrue(result.get(0).equals(testIp1) || result.get(1).equals(testIp2));
        assertTrue(result.get(0).equals(testIp2) || result.get(1).equals(testIp2));
    }

    @Test
    @DisplayName("Test if getActiveIps returns the right ips")
    void TestGetAllActiveIps() {

        Player player1 = new Player();
        player1.setIpAddress(testIp1);
        player1.setConnected(true);

        Player player2 = new Player();
        player2.setIpAddress(testIp2);
        player2.setConnected(false);

        nodeInfoList.add(player1);
        nodeInfoList.add(player2);

        List<String> result = nodeInfoList.getActiveIps();

        assertEquals(1, result.size());
        assertEquals(result.get(0), testIp1);
    }

    @Test
    @DisplayName("Test if getActiveIpsWithoutLeader returns the right ips")
    void TestGetAllActiveIpsThatAreNotLeader() {
        Player player1 = new Player();
        player1.setIpAddress(testIp1);
        player1.setConnected(true);

        Player player2 = new Player();
        player2.setIpAddress(testIp2);
        player2.setConnected(true);


        nodeInfoList.add(player1);
        nodeInfoList.add(player2);
        nodeInfoList.getLeader().setPlayer(player2);

        List<String> result = nodeInfoList.getActiveIpsWithoutLeader();

        assertEquals(1, result.size());
        assertEquals(result.get(0), testIp1);
    }


    @Test
    @DisplayName("Test if the UpdateIsConnected updates the value of a node")
    void TestUpdateIsConnected() {
        Player player = new Player();
        player.setIpAddress(testIp1);
        player.setConnected(false);

        nodeInfoList.add(player);
        nodeInfoList.updateIsConnected(testIp1, true);

        assertTrue(nodeInfoList.get(0).isConnected());
    }

    @Test
    @DisplayName("Test if the size method returns the right value")
    void TestSize() {
        Player player = new Player();
        player.setIpAddress(testIp1);
        player.setConnected(false);

        Player player2 = new Player();
        player2.setIpAddress(testIp2);
        player2.setConnected(true);

        nodeInfoList.add(player);
        nodeInfoList.add(player2);
        assertEquals(2, nodeInfoList.size());
    }

    @Test
    @DisplayName("Test the equals method works")
    void TestEquals() {
        NodeInfoList nodeInfoList2 = nodeInfoList;

        TestCase.assertEquals(nodeInfoList2, nodeInfoList2);
    }

    @Test
    @DisplayName("Test the hashcode method of the nodeInfoList")
    void TestHashCode() {
        assertEquals(new NodeInfoList().hashCode(), nodeInfoList.hashCode());
    }


    @Test
    @DisplayName("Test if the getResponse works happy flow")
    void TestGetLeaderIpHappyFlow() {
        Player player = new Player();
        player.setIpAddress(testIp1);

        nodeInfoList.getLeader().setPlayer(player);
        player.setConnected(true);

        nodeInfoList.add(player);

        assertEquals(testIp1, nodeInfoList.getLeaderIp());
    }

    @Test
    @DisplayName("Test if the getResponse returns null when the Leader is disconnected")
    void TestGetLeaderIpWhenLeaderIsDisconnected() {
        Player player = new Player();
        player.setIpAddress(testIp1);
        nodeInfoList.getLeader().setPlayer(player);
        player.setConnected(false);

        nodeInfoList.add(player);

        assertNull(nodeInfoList.getLeaderIp());
    }

    @Test
    @DisplayName("Test if the getResponse returns null when there is no Leader")
    void TestGetLeaderIpWhenThereAreNoLeaders() {
        Player player = new Player();
        player.setIpAddress(testIp1);
        player.setConnected(true);

        nodeInfoList.add(player);

        assertNull(nodeInfoList.getLeaderIp());
    }

    @Test
    @DisplayName("Test if the getResponse returns null when noone is connected")
    void TestGetLeaderIpWhenThereAreNoNodesConnected() {
        Player player = new Player();
        player.setIpAddress(testIp1);
        player.setConnected(false);

        nodeInfoList.add(player);

        assertNull(nodeInfoList.getLeaderIp());
    }
}
