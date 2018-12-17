package org.han.ica.asd.c.messagehandler.receiving;

import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;

import java.util.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class GameMessageFilterer {

    private HashMap<UUID, Date> messageIds = new HashMap<>();

    private static final long MAX_DURATION = MILLISECONDS.convert(5, MINUTES);


    public boolean isUnique(GameMessage gameMessage) {
        clearMap();

        if (messageIds.containsKey(gameMessage.getMessageId())) {
            return false;
        } else {
            messageIds.put(gameMessage.getMessageId(), new Date());
            return true;
        }
    }

    private void clearMap() {
        Iterator<Map.Entry<UUID, Date>> entryIt = messageIds.entrySet().iterator();
        Date currentDate = new Date();

        while (entryIt.hasNext()) {
            Map.Entry<UUID, Date> entry = entryIt.next();
            if (currentDate.getTime() - entry.getValue().getTime() >= MAX_DURATION) {
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
