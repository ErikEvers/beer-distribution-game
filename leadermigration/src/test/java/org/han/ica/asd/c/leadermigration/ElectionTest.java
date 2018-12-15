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

    @Before
    public void testSetup() throws Exception {
        communicationHelper = new CommunicationHelper();
        PowerMockito.spy(ElectionHandler.class);
        PowerMockito.doReturn("111").when(ElectionHandler.class, "getOwnIpAddress");
    }

    @Test
    public void basicElectionTest() {
        Player[] players = new Player[3];
        players[0] = new Player("1","1", "111", 1, "Joost", true);
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
        players[2] = new Player("1","1", "111", 1, "Joost", true);
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }

    @Test
    public void singlePlayerElectionTest() {
        Player[] players = new Player[1];
        players[0] = new Player("1","1", "111", 1, "Joost", true);
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[0], elected);
    }

    @Test
    public void playerDisconnectDuringElectionTest() {
        Player[] players = new Player[3];
        players[0] = new Player("1","1", "111", 1, "Joost", true);
        players[1] = new Player("1", "2", "222", 2, "Henk", true);
        players[2] = new Player("1", "3", "333", 3, "Piet", false);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }
}
