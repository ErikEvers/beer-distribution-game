package org.han.ica.asd.c.messagehandler.receiving;

import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class GameMessageFilterer {
    private HashMap<UUID, Date> messageIds = new HashMap<>();
    private static final long MAX_DURATION = MILLISECONDS.convert(5, MINUTES);

    /**
     * Checks if a GameMessage is in the HashMap messageIds
     * @param gameMessage
     * @return boolean
     */
    public boolean isUnique(GameMessage gameMessage) {
        clearMap();

        if (messageIds.containsKey(gameMessage.getMessageId())) {
            return false;
        } else {
            messageIds.put(gameMessage.getMessageId(), new Date());
            return true;
        }
    }

    /**
     * Removes all the entries from the HashMap messageIds which are older than 5 minutes
     */
    private void clearMap() {
        Set<Map.Entry<UUID, Date>> entrySet = messageIds.entrySet();
        Iterator<Map.Entry<UUID, Date>> entryIt = entrySet.iterator();

        Date currentDate = new Date();

        while (entryIt.hasNext()) {
            Map.Entry<UUID, Date> entry = entryIt.next();

            long currentDateTime = currentDate.getTime();
            long time = entry.getValue().getTime();

            if (currentDateTime - time >= MAX_DURATION) {
                entryIt.remove();
            }
        }
    }

    public void setMessageIds(Map<UUID, Date> messageIds) {
        this.messageIds = (HashMap<UUID, Date>) messageIds;
    }

    public Map<UUID, Date> getMessageIds() {
        return messageIds;
    }
}
