package org.han.ica.asd.c.leadermigration;

import javax.inject.Inject;

public class ElectionModel {

  private String concattedIp;
  private boolean elected = false;
  @Inject private Player currentPlayer;
  @Inject private Player receivingPlayer;


  /**
   * Ensures that every Player has a unique ID for the bully algorithm
   * @return
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
