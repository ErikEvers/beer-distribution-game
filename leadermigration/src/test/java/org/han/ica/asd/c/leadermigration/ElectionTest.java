package org.han.ica.asd.c.leadermigration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.exceptions.PlayerNotFoundException;
import org.han.ica.asd.c.leadermigration.testutil.CommunicationHelper;
import org.han.ica.asd.c.leadermigration.testutil.PersistenceStub;
import org.han.ica.asd.c.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

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
    public void basicElectionTest() throws PlayerNotFoundException {
        Player[] players = new Player[3];
        players[0] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        players[1] = new Player("1", "2", "222", 2, "Henk", true);
        players[2] = new Player("1", "3", "333", 3, "Piet", true);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[2], elected);
    }

    @Test
    public void secondBasicElectionTest() throws PlayerNotFoundException {
        Player[] players = new Player[3];
        players[0] = new Player("1", "2", "222", 2, "Henk", true);
        players[1] = new Player("1", "3", "333", 3, "Piet", true);
        players[2] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }

    @Test
    public void singlePlayerElectionTest() throws PlayerNotFoundException {
        Player[] players = new Player[1];
        players[0] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[0], elected);
    }

    @Test
    public void playerDisconnectDuringElectionTest() throws PlayerNotFoundException {
        Player[] players = new Player[3];
        players[0] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        players[1] = new Player("1", "2", "222", 2, "Henk", true);
        players[2] = new Player("1", "3", "333", 3, "Piet", false);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[1], elected);
    }

    @Test(expected = AssertionError.class)
    public void playerNotElected() throws PlayerNotFoundException {
        Player[] players = new Player[3];
        players[0] = new Player("1","1", currentPlayerIP, 1, "Joost", true);
        players[1] = new Player("1", "2", "222", 2, "Henk", true);
        players[2] = new Player("1", "3", "333", 3, "Piet", true);

        Player elected = communicationHelper.startElection(players);
        Assert.assertEquals(players[0], elected);
    }

    @Test
    public void playerDisconnectAfterElection() throws PlayerNotFoundException {
        Player[] players = new Player[4];
        players[0] = new Player("2", "1", "111", 1, "Klaas", true);
        players[1] = new Player("2", "2", "222", 2, "Nameless", true);
        players[2] = new Player("2", "3", "333", 3, "Mark",true);
        players[3] = new Player("2", "4", "444", 4, "Peter", true);
        Player firstElected = communicationHelper.startElection(players);
        // Player 4 should win the algorithm
        players[3].setConnected(false);
        Player secondElected = communicationHelper.startElection(players);
        Assert.assertNotEquals(firstElected, secondElected);
    }

    @Test
    public void playerRejoinAfterElection() throws PlayerNotFoundException {
        Player[] players = new Player[4];
        players[0] = new Player("2", "1", "111", 1, "Klaas", true);
        players[1] = new Player("2", "2", "222", 2, "Nameless", true);
        players[2] = new Player("2", "3", "333", 3, "Mark",true);
        players[3] = new Player("2", "4", "444", 4, "Peter", false);
        Player firstElected = communicationHelper.startElection(players);
        // Player 4 should win the algorithm
        players[3].setConnected(true);
        Player secondElected = communicationHelper.startElection(players);
				Assert.assertNotEquals(firstElected, secondElected);
    }

    @Test
		public void handlerTest() throws PlayerNotFoundException, NoSuchFieldException, IllegalAccessException {
			ElectionHandler handler = new ElectionHandler();

			Field model = ElectionHandler.class.getDeclaredField("electionModel");
			model.setAccessible(true);
			model.set(handler, new ElectionModel());

			Field comm = ElectionHandler.class.getDeclaredField("communication");
			comm.setAccessible(true);
			comm.set(handler, mock(IConnectorForLeaderElection.class));


			Player[] players = new Player[4];
			players[0] = new Player("2", "1", "111", 1, "Klaas", true);
			players[1] = new Player("2", "2", "222", 2, "Nameless", true);
			players[2] = new Player("2", "3", "333", 3, "Mark",true);
			players[3] = new Player("2", "4", "444", 4, "Peter", false);
			handler.setupAlgorithm(players);
		}
}
