package communicationcomponent.messagehandler;

import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageFilterer;
import domainobjects.TurnModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GameMessageFiltererTest {

    private GameMessageFilterer gameMessageFilterer;


    private static final long MAX_DURATION = MILLISECONDS.convert(5, MINUTES);

    private HashMap<UUID, Date> messageIds;

    @Mock
    private GameMessage gameMessage;

    @BeforeEach
    void init() {
        messageIds = new HashMap<>();
        gameMessageFilterer = new GameMessageFilterer();
    }

    @Test
    void mapShouldNotHaveEntriesOlderThan5Minutes() {
        Iterator<Map.Entry<UUID, Date>> entryIt = gameMessageFilterer.getMessageIds().entrySet().iterator();
        Date currentDate = new Date();

        boolean noOlders = true;

        Date date = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(50));

        messageIds.put(UUID.randomUUID(), date);
        messageIds.put(UUID.randomUUID(), currentDate);
        gameMessageFilterer.setMessageIds(messageIds);

        gameMessageFilterer.isUnique(gameMessage);

        while (entryIt.hasNext()) {
            Map.Entry<UUID, Date> entry = entryIt.next();
            if (currentDate.getTime() - entry.getValue().getTime() >= MAX_DURATION) {
                noOlders = false;
            }
        }

        assertTrue(noOlders);
    }

    @Test
    void mapShouldnotHaveEntriesExactly5Minutes() {
        Iterator<Map.Entry<UUID, Date>> entryIt = gameMessageFilterer.getMessageIds().entrySet().iterator();
        Date currentDate = new Date();

        boolean noOlders = true;

        Date date = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5));

        messageIds.put(UUID.randomUUID(), date);
        messageIds.put(UUID.randomUUID(), currentDate);
        gameMessageFilterer.setMessageIds(messageIds);

        gameMessageFilterer.isUnique(gameMessage);

        while (entryIt.hasNext()) {
            Map.Entry<UUID, Date> entry = entryIt.next();
            if (currentDate.getTime() - entry.getValue().getTime() >= MAX_DURATION) {
                noOlders = false;
            }
        }

        assertTrue(noOlders);
    }

    @Test
    void mapShouldHaveEntriesYoungerThan5Minutes() {
        gameMessageFilterer.isUnique(gameMessage);
        assertFalse(gameMessageFilterer.getMessageIds().isEmpty());
    }

    @Test
    void shouldReturnFalseForNotUniqueGameMessage() {
        UUID uuid = UUID.randomUUID();

        GameMessage message1 = new TurnModelMessage(new TurnModel(10));
        message1.setMessageId(uuid);

        GameMessage message2 = new TurnModelMessage(new TurnModel(10));
        message2.setMessageId(uuid);

        gameMessageFilterer.isUnique(message1);

        assertFalse(gameMessageFilterer.isUnique(message2));
    }

    @Test
    void shouldReturnTrueForUniqueGameMessage() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        GameMessage message1 = new TurnModelMessage(new TurnModel(10));
        message1.setMessageId(uuid1);

        GameMessage message2 = new TurnModelMessage(new TurnModel(10));
        message2.setMessageId(uuid2);

        gameMessageFilterer.isUnique(message1);

        assertTrue(gameMessageFilterer.isUnique(message2));
    }

}
