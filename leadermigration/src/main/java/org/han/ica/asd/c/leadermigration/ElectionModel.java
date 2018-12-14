package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.model.Player;

import javax.inject.Inject;

public class ElectionModel {

  private String concattedIp;
  private boolean elected;
  @Inject private Player currentPlayer;
  @Inject private Player receivingPlayer;


  public ElectionModel() {
    this.elected = false;
  }
  /**
   * Ensures that every Player has a unique ID for the bully algorithm
   * @return -> String, a concatenation of playerID and Ip-Address.
   */
  public String getConcattedIp() {
    return concattedIp;
  }

  public void setConcattedIp(String concattedIp) {
    this.concattedIp = concattedIp;
  }

  public boolean isElected() {
    return elected;
  }

  public void setElected(boolean elected) {
    this.elected = elected;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public Player getReceivingPlayer() {
    return receivingPlayer;
  }

  public void setReceivingPlayer(Player receivingPlayer) {
    this.receivingPlayer = receivingPlayer;
  }
}
