package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.model.Player;

public class ElectionModel {

  private boolean alive;
  private Player currentPlayer;
  private Player receivingPlayer;


  public ElectionModel() {
    this.alive = false;
  }

  public boolean isAlive() {
    return alive;
  }

  public void setAlive(boolean alive) {
    this.alive = alive;
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
