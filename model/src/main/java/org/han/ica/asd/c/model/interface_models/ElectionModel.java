package org.han.ica.asd.c.model.interface_models;

import org.han.ica.asd.c.model.domain_objects.Player;

/**
 * Object that gets exchanged between players during leader election
 * 	to indicate if a player wants to participate in the election.
 */
public class ElectionModel {

  private boolean alive;
  private Player currentPlayer;
  private Player receivingPlayer;


  /**
   * Default constructor.
	 * Sets alive to false.
   */
  public ElectionModel() {
    this.alive = false;
  }

	/**
	 * Retrieves whether the responding player is alive or not
	 * @return boolean	-> true when alive
	 * 									-> false when not
	 */
  public boolean isAlive() {
    return alive;
  }

	/**
	 * Set the status of the responding player.
	 * @param alive boolean	-> true when alive
	 *              				-> false when not
	 */
  public void setAlive(boolean alive) {
    this.alive = alive;
  }

	/**
	 * Retrieve the player doing the election.
	 * @return Player instance.
	 */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

	/**
	 * Set the player running the election.
	 * @param currentPlayer Player instance.
	 */
  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

	/**
	 * Retrieve the player receiving the election message.
	 * @return Player instance.
	 */
  public Player getReceivingPlayer() {
    return receivingPlayer;
  }

	/**
	 * Set the player receiving the election message.
	 * @param receivingPlayer Player instance.
	 */
  public void setReceivingPlayer(Player receivingPlayer) {
    this.receivingPlayer = receivingPlayer;
  }
}
