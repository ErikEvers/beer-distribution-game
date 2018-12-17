package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.leadermigration.testutil.CommunicationHelper;
import org.han.ica.asd.c.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest( ElectionHandler.class )
public class ElectionTest {

    private CommunicationHelper communicationHelper;
    private final String currentPlayerIP = "111";

    @Before
    public void testSetup() throws Exception {
			communicationHelper = new CommunicationHelper();

			PowerMockito.spy(ElectionHandler.class);
			PowerMockito.doReturn(currentPlayerIP).when(ElectionHandler.class, "getLocalIp");
    }

    @Test
    public void basicElectionTest() {
        Player[] players = new Player[3];
        players[0] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        players[1] = new Player("1", "2", "222", 2, "Henk", true);
        players[2] = new Player("1", "3", "333", 3, "Piet", true);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[2], elected);
    }

    @Test
    public void secondBasicElectionTest() {
        Player[] players = new Player[3];
        players[0] = new Player("1", "2", "222", 2, "Henk", true);
        players[1] = new Player("1", "3", "333", 3, "Piet", true);
        players[2] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }

    @Test
    public void singlePlayerElectionTest() {
        Player[] players = new Player[1];
        players[0] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[0], elected);
    }

    @Test
    public void playerDisconnectDuringElectionTest() {
        Player[] players = new Player[3];
        players[0] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        players[1] = new Player("1", "2", "222", 2, "Henk", true);
        players[2] = new Player("1", "3", "333", 3, "Piet", false);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }

    @Test(expected = AssertionError.class)
    public void playerNotElected() {
        Player[] players = new Player[3];
        players[0] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        players[1] = new Player("1", "2", "222", 2, "Henk", true);
        players[2] = new Player("1", "3", "333", 3, "Piet", true);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[0], elected);
    }

    @Test(expected = AssertionError.class)
    public void playerDisconnectAfterElection() {
        Player[] players = new Player[4];
        players[0] = new Player("2", "1", "111", 1, "Klaas", true);
        players[1] = new Player("2", "2", "222", 2, "Nameless", true);
        players[2] = new Player("2", "3", "333", 3, "Mark",true);
        players[3] = new Player("2", "4", "444", 4, "Peter", true);
        Player firstElected = communicationHelper.startElection(players);
        // Player 4 should win the algorithm
        players[3].setConnected(false);
        Player secondElected = communicationHelper.startElection(players);
        Assert.assertEquals(firstElected, secondElected);
    }

    @Test(expected = AssertionError.class)
    public void playerRejoinAfterElection() {
        Player[] players = new Player[4];
        players[0] = new Player("2", "1", "111", 1, "Klaas", true);
        players[1] = new Player("2", "2", "222", 2, "Nameless", true);
        players[2] = new Player("2", "3", "333", 3, "Mark",true);
        players[3] = new Player("2", "4", "444", 4, "Peter", false);
        Player firstElected = communicationHelper.startElection(players);
        // Player 4 should win the algorithm
        players[3].setConnected(true);
        Player secondElected = communicationHelper.startElection(players);
        Assert.assertEquals(firstElected, secondElected);
    }
}
