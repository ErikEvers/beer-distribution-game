package org.han.ica.asd.c.messagehandler.messagetypes;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.WHO_IS_THE_LEADER_MESSAGE;

public class WhoIsTheLeaderMessage extends GameMessage {
    private String response;

    public WhoIsTheLeaderMessage() {
        super(WHO_IS_THE_LEADER_MESSAGE);
    }

    /**
     * Gets response.
     *
     * @return Value of response.
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets new response.
     *
     * @param response New value of response.
     */
    public void setResponse(String response) {
        this.response = response;
    }
}
