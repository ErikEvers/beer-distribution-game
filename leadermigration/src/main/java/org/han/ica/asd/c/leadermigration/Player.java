package org.han.ica.asd.c.leadermigration;

public class Player {

  private String playerId;
  private String ipAddress;
  private boolean isElected;
  private boolean isConnected;
  private boolean isLeader;

  public Player(){

  }

  public Player(String playerId, String ipAddress) {
    this.playerId = playerId;
    this.ipAddress = ipAddress;
    isElected = false;
  }

  public String concatIpId() {
    return playerId.concat(ipAddress);
  }

  public String getPlayerId() {
    return playerId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public boolean isElected() {
    return isElected;
  }

  public void setElected(boolean elected) {
    isElected = elected;
  }

  public boolean isConnected() {
    return isConnected;
  }

  public void setConnected(boolean connected) {
    isConnected = connected;
  }

  public boolean isLeader() {
    return isLeader;
  }

  public void setLeader(boolean leader) {
    isLeader = leader;
  }


}
