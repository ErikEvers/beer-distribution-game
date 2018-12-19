package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TestFailLog {
    private FailLog failLog;
    private HashMap<String, Integer> beginHashMap;


    @Mock
    NodeInfoList nodeInfoList = new NodeInfoList();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        failLog = new FailLog(nodeInfoList);
        beginHashMap = failLog.getFailLogHashMap();
    }

    @Test
    @DisplayName("Test if a string can be added to the list")
    void TestIfIpIsAdded() {
        assertEquals(beginHashMap.size(), 0, "List was not empty");

        //Execute the method
        String ipToAdd = "testIp";
        failLog.add(ipToAdd);
        HashMap<String, Integer> result = failLog.getFailLogHashMap();

        //Assert that the method did its job
        assertTrue(result.containsKey(ipToAdd), "Ip was not added to the list");
        int resultValue = result.get(ipToAdd);
        assertEquals(0, resultValue, "Value of the key(ip) was not 0");
    }

    @Test
    @DisplayName("Test if the value of key(ip) gets incremented with 1")
    void TestIfIpIsIncremented() {

        //Execute the method
        String ipToAdd = "testIp";
        failLog.increment(ipToAdd);
        HashMap<String, Integer> result = failLog.getFailLogHashMap();
        int resultValue = result.get(ipToAdd);

        //Assert that the method did its job
        assertTrue(result.containsKey(ipToAdd), "Ip was not added to the list");
        assertEquals(1, resultValue, "Value of the key(ip) was not incremented");
    }

    @Test
    @DisplayName("Test if the value of a specific key gets set to 0 in the failLogHashmap")
    void TestIfAmountOfFailsIsSetToZero() {

        String ipToAdd = "testIp";
        failLog.increment(ipToAdd);
        HashMap<String, Integer> startSituation = failLog.getFailLogHashMap();
        assertTrue(startSituation.containsKey(ipToAdd));
        int startValue = startSituation.get(ipToAdd);
        assertEquals(1, startValue);

        failLog.reset(ipToAdd);

        HashMap<String, Integer> result = failLog.getFailLogHashMap();

        int resultValue = result.get(ipToAdd);

        assertEquals(0, resultValue);
    }

    @Test
    @DisplayName("Test if the method returns three when the key(ip) has a value of 3")
    void TestIfCheckIsFailedReturnsTrueWhenAmountOfFailsEqualsThree() {

        String ipToAdd = "testIp";
        failLog.increment(ipToAdd);
        failLog.increment(ipToAdd);
        failLog.increment(ipToAdd);
        HashMap<String, Integer> startSituation = failLog.getFailLogHashMap();
        assertTrue(startSituation.containsKey(ipToAdd));
        int startValue = startSituation.get(ipToAdd);
        assertEquals(3, startValue);

        assertEquals(true, failLog.checkIfIpIsFailed(ipToAdd));
    }

    @Test
    @DisplayName("Test if the method returns false when the key(ip) has a value less than 3")
    void TestIfCheckIsFailedReturnsFalseWhenAmountOfFailsIsLessThanThree() {

        String ipToAdd = "testIp";
        failLog.increment(ipToAdd);
        HashMap<String, Integer> startSituation = failLog.getFailLogHashMap();
        assertTrue(startSituation.containsKey(ipToAdd));
        int startValue = startSituation.get(ipToAdd);
        assertEquals(1, startValue);

        assertEquals(false, failLog.checkIfIpIsFailed(ipToAdd));

    }

    @Test
    @DisplayName("Test if the method returns false when the key(ip) has a value more than 3")
    void TestIfCheckIsFailedReturnsFalseWhenAmountOfFailsIsMoreThanThree() {

        String ipToAdd = "testIp";
        failLog.increment(ipToAdd);
        failLog.increment(ipToAdd);
        failLog.increment(ipToAdd);
        failLog.increment(ipToAdd);
        HashMap<String, Integer> startSituation = failLog.getFailLogHashMap();
        assertTrue(startSituation.containsKey(ipToAdd));
        int startValue = startSituation.get(ipToAdd);
        assertEquals(4, startValue);

        assertEquals(false, failLog.checkIfIpIsFailed(ipToAdd));

    }

    @Test
    @DisplayName("Test if isalive returns true when the key(ip) is not yet in the list")
    void TestIfIsAliveReturnsTrueWhenTheIpIsNotInTheList() {
        assertEquals(beginHashMap.size(), 0, "List was not empty");

        HashMap<String, Integer> result = failLog.getFailLogHashMap();

        assertTrue(failLog.isAlive("IpThatIsNotInTheList"), "IsAlive did not return true");

    }

    @Test
    @DisplayName("Test if isalive returns true when the key(ip) is in the list and has a value of 0")
    void TestIfIsAliveReturnsTrueWhenTheIpIsInTheListWithTheValueOnZero() {

        String ipToAdd = "testIp";
        failLog.add(ipToAdd);
        HashMap<String, Integer> result = failLog.getFailLogHashMap();
        assertTrue(result.containsKey(ipToAdd), "Ip was not added to the list");
        int resultValue = result.get(ipToAdd);

        assertEquals(0, resultValue, "Value of the key(ip) was not 0");

        assertTrue(failLog.isAlive(ipToAdd));
    }

    @Test
    @DisplayName("Test if isalive returns false when the key(ip) is in the list and has a value of > 0")
    void TestIfIsAliveReturnsFalseWhenTheIpIsInTheListWithAValueGreaterThanZero() {

        //setup the values in the hashmap
        String ipToAdd = "testIp";
        failLog.add(ipToAdd);
        failLog.increment(ipToAdd);
        HashMap<String, Integer> result = failLog.getFailLogHashMap();
        assertTrue(result.containsKey(ipToAdd), "Ip was not added to the list");
        int resultValue = result.get(ipToAdd);

        assertEquals(1, resultValue, "Value of the key(ip) was not 1");

        //Assert that the method returns the correct boolean value
        assertFalse(failLog.isAlive(ipToAdd), "IsAlive did not return false");
    }

    @Test
    @DisplayName("Test if SuccesSize returns the correct amount of successes with 1 ip that isfailed")
    void TestIfGetSuccesSizeReturnsTheCorrectValueWithoutAnyIpsThatFailed() {
        List<String> mockList = new ArrayList<>();
        mockList.add("TestIp");
        mockList.add("TestIp2");
        mockList.add("TestIp3");

        when(nodeInfoList.getActiveIps()).thenReturn(mockList);

        assertEquals(3, failLog.getSuccesSize());

    }

    @Test
    @DisplayName("Test if SuccesSize returns the correct amount of successes with 1 ip that isfailed")
    void TestIfGetSuccesSizeReturnsTheCorrectValueWithOneIpThatFailed() {
        List<String> mockList = new ArrayList<>();
        mockList.add("TestIpThatHasFailed3Times");
        mockList.add("TestIp2");
        mockList.add("TestIp3");

        failLog.increment("TestIpThatHasFailed3Times");
        failLog.increment("TestIpThatHasFailed3Times");
        failLog.increment("TestIpThatHasFailed3Times");

        when(nodeInfoList.getActiveIps()).thenReturn(mockList);

        assertEquals(2, failLog.getSuccesSize());

    }

    @Test
    @DisplayName("Test if SuccesSize returns the correct amount of successes with 2 ips that isfailed")
    void TestIfGetSuccesSizeReturnsTheCorrectValueWithMultipleIpsThatAreFailed() {
        String ip1 = "TestIpThatHasFailed3Times";
        String ip2 = "TestIp2ThatHasFailed3Times";
        List<String> mockList = new ArrayList<>();
        mockList.add(ip1);
        mockList.add(ip2);
        mockList.add("TestIp3");

        failLog.increment(ip1);
        failLog.increment(ip1);
        failLog.increment(ip1);

        failLog.increment(ip2);
        failLog.increment(ip2);
        failLog.increment(ip2);

        when(nodeInfoList.getActiveIps()).thenReturn(mockList);

        assertEquals(1, failLog.getSuccesSize());

    }


}


