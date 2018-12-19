package org.han.ica.asd.c.faultdetection.messagetypes;

import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;

/**
 * <h2> Is used for asking a different node if they have received a ping from the Leader within a certain interval.</h2>
 * <p>It inherits the messageId from the super class 'FaultDetectionMessage'. <br>
 * This 'messageId' is then used at the 'FaultDetectionMessageReceiver' to determine which type of message was received.</p>
 *
 * @author Tarik
 * @see org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage
 * @see FaultDetectionMessageReceiver
 */
public class FaultMessageResponse extends FaultDetectionMessage {

    private Boolean isAlive;
    private String ipOfSubject;

    public FaultMessageResponse(Boolean isAlive, String ipOfSubject){
        super(2);
        this.isAlive = isAlive;
        this.ipOfSubject= ipOfSubject;
    }

    /**
     *
     * @return
     */
    public Boolean getAlive() {
        return isAlive;
    }

    /**
     *
     * @param alive
     */
    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    /**
     *
     * @return
     */
    public String getIpOfSubject() {
        return ipOfSubject;
    }

    /**
     *
     * @param ipOfSubject
     */
    public void setIpOfSubject(String ipOfSubject) {
        this.ipOfSubject = ipOfSubject;
    }
}