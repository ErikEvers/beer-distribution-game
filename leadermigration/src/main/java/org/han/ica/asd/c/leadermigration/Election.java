package org.han.ica.asd.c.leadermigration;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Election {

  private String concattedIp;
  private boolean elected = false;
  private boolean victory = false;
  @Inject
  private Player newLeader;
  @Inject
  private Player currentPlayer;
  private static Logger LOGGER;
  @Inject
  private iCommunication communication;
  private Election receivingPlayer;
  private Player[] players;

  public Election() {

  }

  public Election(Player[] players){
    this.players = players;
  }

  /**
   * Send every Player an election message to be elected as leader of the network
   * @param players -> All the connected players (Remove currentPlayer from array?)
   */
  public void sendElectionMessage(Player[] players) {
    concattedIp = currentPlayer.concatIpId();
    for(Player player: players){
      communication.sendMessage(this, player);
    }
  }

  /**
   * This function checks the concattedIp of both devices and returns an alive message with a boolean if a
   * device is elected or not.
   * if sender.concattedIp is lexicographically less than this.concattedIp, the output is a negative number.
   * if sender.concattedIp is lexicographically bigger than this.concattedIp, the output is a positive number.
   * @param sender -> content of the player for the leader election
   */
  public void sendAliveMessage(Election sender) {
    if (sender.concattedIp.compareTo(this.concattedIp) > 0) {
      this.elected = true;
    }
    communication.sendMessage(this, sender.currentPlayer);
  }

  /**
   * this function sends the victory message. This function will be executed by the device of the player which
   * wins the election.
   */
  public void sendVictoryMessage() {
    if(this.elected) {
      this.victory = true;
      for(Player player: players) {
        communication.sendMessage(this, player);
      }
    }
  }

  /**
   * This function is called by the interface. This is data that's coming back from other devices
   * @param election: The election class of another device.
   */
  public void receiveData(Election election) {
    if(election.newLeader == null) {
      if(election.elected) {
        this.elected = false;
      } else {
        this.elected = true;
      }
    } else {

    }
  }

  /**
   * To get own ip address
   * @return -> ipAddress as String
   */
  private String getOwnIpAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      LOGGER.log(Level.SEVERE, "Error with finding ip-address");
    }
    return null;
  }

  /**
   * Get the currentPlayer in this game.
   * @param players -> All the connected players.
   */
  private void getPlayerByIp(Player[] players) {
    for(Player player: players) {
      if (player.getIpAddress().equals(this.getOwnIpAddress())) {
        currentPlayer = player;
      }
    }
  }

}
