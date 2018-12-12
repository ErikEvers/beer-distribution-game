package org.han.ica.asd.c.leadermigration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElectionHandler {

  private ElectionModel electionModel;
  private static Logger LOGGER;
  private iCommunication communication;


  public ElectionHandler() {

  }

  public void setupAlgorithm(Player[] players) {
    electionModel = new ElectionModel();
    electionModel.setCurrentPlayer(getPlayerBiIp(players));
    Player currentPlayer = electionModel.getCurrentPlayer();
    electionModel.setConcattedIp(currentPlayer.concatIpId());
  }

  /**
   * Send every player an election message to be elected as the leader of the network
   * @param players -> All the connected players (without the current player)
   */
  public void sendElectionMessage(Player[] players) {
    for(Player player: players) {
      electionModel.setReceivingPlayer(player);
      communication.sendMessage(this.electionModel, player);
    }
  }

  /**
   * This function checks the concattedIp of both devices and returns an alive message when connected
   * If election.concattedIp is lexicographically less than receivingPlayer.concattedIp, the output is a negative number
   *  and returns false.
   * If election.concattedIp is lexicographically bigger than receivingPlayer.concattedIp, the output is a
   *  positive number and returns true.
   * @param receivedModel -> The electionModel of the sending player.
   */
  public void sendAliveMessage(ElectionModel receivedModel) {
    Player receivingPlayer = receivedModel.getReceivingPlayer();
    if (electionModel.getConcattedIp().compareTo(receivingPlayer.concatIpId()) > 0) {
      electionModel.setElected(true);
    }
    communication.sendMessage(electionModel, electionModel.getCurrentPlayer());
  }

  public void sendVictoryMessage(ElectionModel winningModel, Player[] players) {
//    if (electionModel.isVictory()) {
//      for(Player player: players) {
//      }
//    }
  }

  /**
   * To get own ip-address
   * @return -> ipAddress as String, when not found return NULL.
   */
  private String getOwnIpAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
    return null;
  }

  private Player getPlayerBiIp(Player[] players) {
    for(Player player: players) {
      if(player.getIpAddress().equals(this.getOwnIpAddress())) {
        return player;
      }
    }
    return null;
  }

}
