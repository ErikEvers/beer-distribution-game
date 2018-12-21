package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.model.interface_models.PlayerNotFoundException;
import org.han.ica.asd.c.leadermigration.testutil.CommunicationHelper;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.mock;

public class ElectionTest {

    private CommunicationHelper communicationHelper;
    private final String currentPlayerIP = "111";

    @Before
    public void testSetup() {
			communicationHelper = new CommunicationHelper();
    }

    @Test
    public void basicElectionTest() throws PlayerNotFoundException {
        Player[] players = new Player[3];
        players[0] = new Player("1", currentPlayerIP, mock(Facility.class), "Joost", true);
        players[1] = new Player("1", "222", mock(Facility.class), "Henk", true);
        players[2] = new Player("1", "333", mock(Facility.class), "Piet", true);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[2], elected);
    }

    @Test
    public void secondBasicElectionTest() throws PlayerNotFoundException {
        Player[] players = new Player[3];
        players[0] = new Player("1", "222", mock(Facility.class), "Henk", true);
        players[1] = new Player("1", "333", mock(Facility.class), "Piet", true);
        players[2] = new Player("1", currentPlayerIP, mock(Facility.class), "Joost", true);
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }

    @Test
    public void singlePlayerElectionTest() throws PlayerNotFoundException {
        Player[] players = new Player[1];
        players[0] = new Player("1", currentPlayerIP, mock(Facility.class), "Joost", true);
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[0], elected);
    }

    @Test
    public void playerDisconnectDuringElectionTest() throws PlayerNotFoundException {
        Player[] players = new Player[3];
        players[0] = new Player("1", currentPlayerIP, mock(Facility.class), "Joost", true);
        players[1] = new Player("1", "222", mock(Facility.class), "Henk", true);
        players[2] = new Player("1", "333", mock(Facility.class), "Piet", false);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }

    @Test
    public void playerDisconnectAfterElection() throws PlayerNotFoundException {
        Player[] players = new Player[4];
        players[0] = new Player("2", "111", mock(Facility.class), "Klaas", true);
        players[1] = new Player("2", "222", mock(Facility.class), "Nameless", true);
        players[2] = new Player("2", "333", mock(Facility.class), "Mark",true);
        players[3] = new Player("2", "444", mock(Facility.class), "Peter", true);
        Player firstElected = communicationHelper.startElection(players);
        // Player 4 should win the algorithm
        players[3].setConnected(false);
        Player secondElected = communicationHelper.startElection(players);
        Assert.assertNotEquals(firstElected, secondElected);
    }

    @Test
    public void playerRejoinAfterElection() throws PlayerNotFoundException {
        Player[] players = new Player[4];
        players[0] = new Player("2", "111", mock(Facility.class), "Klaas", true);
        players[1] = new Player("2", "222", mock(Facility.class), "Nameless", true);
        players[2] = new Player("2", "333", mock(Facility.class), "Mark",true);
        players[3] = new Player("2", "444", mock(Facility.class), "Peter", false);
        Player firstElected = communicationHelper.startElection(players);
        // Player 4 should win the algorithm
        players[3].setConnected(true);
        Player secondElected = communicationHelper.startElection(players);
				Assert.assertNotEquals(firstElected, secondElected);
    }
}
