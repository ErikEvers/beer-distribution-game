package org.han.ica.asd.c.leadermigration;

import javax.inject.Inject;

public class ElectionModel {

  private String concattedIp;
  private boolean elected = false;
  private boolean victory = false;
  @Inject private Player currentPlayer;
  @Inject private Player receivingPlayer;
  @Inject private Player newLeader;
  @Inject private Player[] players;


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

  public boolean isVictory() {
    return victory;
  }

  public void setVictory(boolean victory) {
    this.victory = victory;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public Player getNewLeader() {
    return newLeader;
  }

  public void setNewLeader(Player newLeader) {
    this.newLeader = newLeader;
  }

  public Player[] getPlayers() {
    return players;
  }

  public void setPlayers(Player[] players) {
    this.players = players;
  }

  public Player getReceivingPlayer() {
    return receivingPlayer;
  }

  public void setReceivingPlayer(Player receivingPlayer) {
    this.receivingPlayer = receivingPlayer;
  }
}
