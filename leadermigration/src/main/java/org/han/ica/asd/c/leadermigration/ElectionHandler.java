package org.han.ica.asd.c.leadermigration;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElectionHandler {

  private static Logger LOGGER;
  @Inject private ElectionModel electionModel;
  @Inject private IConnectorForLeaderElection communication;
  @Inject private List<ElectionModel> elections;


  public ElectionHandler() {

  }

  /**
   * Setup the algorithm in de electionModel.
   * @param players -> All connected players to send a message.
   */
  public void setupAlgorithm(Player[] players) {
    electionModel.setCurrentPlayer(getPlayerBiIp(players));
    Player currentPlayer = electionModel.getCurrentPlayer();
    electionModel.setConcattedIp(currentPlayer.concatIpId());
  }

  /**
   * Send every player an election message to be elected as the leader of the network
   * @param players -> All the connected players (without the current player)
   */
  public List<ElectionModel> sendElectionMessage(Player[] players) {
    for(Player player: players) {
      // This if statement ensures to not send this to yourself.
      if(!electionModel.getCurrentPlayer().equals(player)) {
        electionModel.setReceivingPlayer(player);
        elections.add(communication.sendElectionMessage(this.electionModel, player));
      }
    }
    return elections;
  }

  /**
   * This function checks the concattedIp of both devices and returns an alive message when connected
   * If election.concattedIp is lexicographically less than receivingPlayer.concattedIp, the output is a negative number
   *  and returns false.
   * If election.concattedIp is lexicographically bigger than receivingPlayer.concattedIp, the output is a
   *  positive number and returns true.
   * @param receivedModel -> The electionModel of the sending player.
   */
  public ElectionModel sendAliveMessage(ElectionModel receivedModel) {
    Player receivingPlayer = receivedModel.getReceivingPlayer();
    if (electionModel.getConcattedIp().compareTo(receivingPlayer.concatIpId()) > 0) {
      electionModel.setElected(true);
    }
    return this.electionModel;
  }

  /**
   * This message will be sent by the device who has won the election.
   * @param winningModel -> This instance of electionModel has the winner of the bully algorithm
   * @param players -> To send it to all players.
   */
  public void sendVictoryMessage(ElectionModel winningModel, Player[] players) {
    for(Player player: players) {
      // This if statement ensures to not this to yourself
      if(!electionModel.getCurrentPlayer().equals(player)) {
        communication.sendVictoryMessage(winningModel.getCurrentPlayer(), player);
      }
    }
  }

  /**
   * Get own ip address
   * @return -> ipAddress as String, when not found return NULL.
   */
  private String getOwnIpAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return null;
  }

  /**
   * Get current player by its ip address
   * @param players -> All players in an array
   * @return the current player
   */
  private Player getPlayerBiIp(Player[] players) {
    for(Player player: players) {
      if(player.getIpAddress().equals(this.getOwnIpAddress())) {
        return player;
      }
    }
    return null;
  }


}
