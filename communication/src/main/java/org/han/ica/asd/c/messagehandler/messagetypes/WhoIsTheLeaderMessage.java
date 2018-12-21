package org.han.ica.asd.c.messagehandler.messagetypes;

public class WhoIsTheLeaderMessage extends GameMessage {
    private String response;

    public WhoIsTheLeaderMessage() {
        super(4);
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
