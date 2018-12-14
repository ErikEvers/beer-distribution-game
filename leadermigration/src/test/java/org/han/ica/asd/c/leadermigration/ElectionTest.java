package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.leadermigration.testutil.CommunicationHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest( ElectionHandler.class )
public class ElectionTest {

    private CommunicationHelper communicationHelper;

    @Before
    public void testSetup() {
        communicationHelper = new CommunicationHelper();
    }

    @Test
    public void basicElectionTest() {
        Player[] players = new Player[3];
        players[0] = new Player("1", "111");
        players[1] = new Player("2", "222");
        players[2] = new Player("3", "333");

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[2], elected);
    }

    @Test
    public void secondBasicElectionTest() {
        Player[] players = new Player[3];
        players[0] = new Player("3", "333");
        players[1] = new Player("1", "111");
        players[2] = new Player("2", "222");
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[0], elected);
    }

    @Test
    public void singlePlayerElectionTest() {
        Player[] players = new Player[1];
        players[0] = new Player("1", "111");
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[0], elected);
    }

    @Test
    public void playerDisconnectDuringElectionTest() {
        Player[] players = new Player[3];
        players[0] = new Player("1", "111");
        players[1] = new Player("2", "222");
        players[2] = new Player("3", "333");

        players[2].setConnected(false);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }
}
