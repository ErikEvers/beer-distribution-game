package org.han.ica.asd.c.faultdetection;

public class FaultHandlerPlayer {
    private int amountOfConnectionsWithLeader;
    private int amountOfFailingIps;
    private int amountOfActiveIps;

    int getAmountOfActiveIps() {
        return amountOfActiveIps;
    }

    int getAmountOfFailingIps() {
        return amountOfFailingIps;
    }

    int getAmountOfConnectionsWithLeader() {
        return amountOfConnectionsWithLeader;
    }

    FaultHandlerPlayer() {
        amountOfConnectionsWithLeader = 0;
    }

    public void reset() {
        resetAmountOfFailingIps();
        resetAmountOfConnectionsWithLeader();
    }

    public void resetAmountOfConnectionsWithLeader() {
        amountOfConnectionsWithLeader = 0;
    }

    public void incrementAmountOfConnectionsWithLeader() {
        amountOfConnectionsWithLeader++;
    }

    public void resetAmountOfFailingIps() {
        amountOfFailingIps = 0;
    }

    public void incrementAmountOfFailingIps() {
        amountOfFailingIps++;
    }

    public String whoIsDead() {
        if (amountOfFailingIps == amountOfActiveIps) {
            return "imDead";
        } else {
            return "leaderIsDead";
        }
    }

    void setAmountOfActiveIps(int amountOfActiveIps) {
        this.amountOfActiveIps = amountOfActiveIps;
    }

    void setAmountOfFailingIps(int amountOfFailingIps) {
        this.amountOfFailingIps = amountOfFailingIps;
    }

}
