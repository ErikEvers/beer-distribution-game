package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.leadermigration.testutil.CommunicationHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest( ElectionHandler.class )
public class ElectionTest {
    private static final Logger LOGGER = Logger.getLogger(ElectionTest.class.getName());

    private CommunicationHelper communicationHelper;

    @Before
    public void testSetup() {
        communicationHelper = new CommunicationHelper();

        try {
            ElectionHandler elHandler = new ElectionHandler();
            Field communicationField = elHandler.getClass().getDeclaredField("communication");
            communicationField.setAccessible(true);
            communicationField.set(elHandler, communicationHelper);

            whenNew(ElectionHandler.class).withNoArguments().thenReturn(elHandler);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Something went wrong while setting up the LeaderElection test");
            e.printStackTrace();
        }
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
}
