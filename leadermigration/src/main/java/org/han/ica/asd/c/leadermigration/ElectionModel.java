package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.model.Player;

import javax.inject.Inject;

public class ElectionModel {

  private String concattedIp;
  private boolean elected;
  private Player currentPlayer;
  private Player receivingPlayer;


  public ElectionModel() {
    this.elected = false;
  }

  public ElectionModel(ElectionModel old) {
  	this.setConcattedIp(old.getConcattedIp());
  	this.setElected(old.isElected());
		this.setCurrentPlayer(old.getCurrentPlayer());
		this.setReceivingPlayer(old.getReceivingPlayer());
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
