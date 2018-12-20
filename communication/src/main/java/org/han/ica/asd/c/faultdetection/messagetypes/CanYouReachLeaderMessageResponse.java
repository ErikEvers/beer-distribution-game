package org.han.ica.asd.c.faultdetection.messagetypes;

public class ICanReachLeaderMessage extends FaultDetectionMessage {
    public boolean getLeaderState() {
        return leaderState;
    }

    private boolean leaderState;

    public ICanReachLeaderMessage(boolean leaderState) {
        super(5);
        this.leaderState = leaderState;
    }


}