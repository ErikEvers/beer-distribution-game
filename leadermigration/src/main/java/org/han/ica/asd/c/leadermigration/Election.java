package org.han.ica.asd.c.leadermigration;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class Election {

  private String ipAddress;
  private String concattedIp;
  private boolean elected;
  @Inject
  private Player newLeader;
  @Inject
  private Player currentPlayer;
  private static Logger logger;
  @Inject
  private iCommunication communication;

  public Election() {

  }

  public Player sendElectionMessage(Player[] players) {
    concattedIp = currentPlayer.concatIpId();
    for(int i = 0; i < players.length; ++i) {

    }
    return null;
  }

  public Player sendAliveMessage() {
    return null;
  }

  public void sendVictoryMessage() {

  }

  public void receiveData() {

  }

  private String getOwnIpAddress() {
    try {
      ipAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
//      logger.log('erreur');
    }
    return ipAddress;
  }

  private void getPlayerByIp(Player[] players) {
    for(int i = 0; i < players.length; ++i) {
      if(players[i].getIpAddress().equals(this.ipAddress)) {
        currentPlayer = players[i];
      }
    }
  }

}
