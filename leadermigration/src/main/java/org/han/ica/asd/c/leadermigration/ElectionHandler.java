package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.exceptions.PlayerNotFoundException;
import org.han.ica.asd.c.leadermigration.componentInterfaces.IConnectorForLeaderElection;
import org.han.ica.asd.c.model.dao_model.Player;
import javax.inject.Inject;
import javax.inject.Provider;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElectionHandler {

	private List<Player> receivedPlayers;
  @Inject private static Logger logger;
  @Inject private ElectionModel electionModel;
  @Inject private IConnectorForLeaderElection communication;
  @Inject private IpHandler ipHandler;

  private final Provider<ElectionMessageRunnable> electionMessageRunnableProvider;

  @Inject
  public ElectionHandler(Provider<ElectionMessageRunnable> electionMessageRunnableProvider) {
  	this.electionMessageRunnableProvider = electionMessageRunnableProvider;
	}

  /**
   * Setup the algorithm in de electionModel.
   * @param players -> All connected players to send a message.
   */
  public Player setupAlgorithm(Player[] players) throws PlayerNotFoundException {
    electionModel.setCurrentPlayer(getPlayerByIp(players));
		receivedPlayers = new ArrayList<>();
    return electionModel.getCurrentPlayer();
  }

  /**
   * Send every player an election message to be elected as the leader of the network
   * @param players -> All the connected players (without the current player)
   */
  public List<Player> sendElectionMessage(Player[] players) {
  	List<ElectionMessageRunnable> runnableList = new ArrayList<>();
  	Object lock = new Object();
    for(Player player: players) {
			ElectionMessageRunnable runnable = electionMessageRunnableProvider.get();
			runnable.start(lock, receivedPlayers, electionModel, player);
			runnableList.add(runnable);
    }
		for (ElectionMessageRunnable runnable : runnableList) {
			try {
				runnable.join();
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
    return receivedPlayers;
  }

  /**
   * This function receives and sends the electionmodel as a sign of being alive and a part of the election.
   * @param receivedModel -> The electionModel of the sending player.
   */
  public ElectionModel sendAliveMessage(ElectionModel receivedModel) {
    receivedModel.setAlive(true);
    return receivedModel;
  }

	/**
	 * Compare the currentPlayer to the other players and return it if it has the biggest ID in the list.
	 * If it is the biggest in the list, the currentPlayer gets returned.
	 * If a bigger player is encountered in the list, the method is called recursively to determine if there are other
	 * 	even bigger players in the list.
	 * @param currentPlayer -> the player to compare to the list.
	 * @param players -> the list of players to go through.
	 * @return the player with the highest ID
	 */
  public Player determineWinner(Player currentPlayer, List<Player> players) {
		for(Player player : players) {
			if (currentPlayer.concatIpId().compareTo(player.concatIpId()) < 0) {
				players.remove(player);
				return determineWinner(player, players);
			}
		}
		return currentPlayer;
	}

  /**
   * This message will be sent by the device who has won the election.
   * @param winner -> The player that was the winner of the bully algorithm
   * @param players -> To send it to all players.
   */
  public void sendVictoryMessage(Player winner, Player[] players) {
    for(Player player: players) {
      // This if statement ensures to not this to yourself
      if(!electionModel.getCurrentPlayer().equals(player)) {
        communication.sendVictoryMessage(winner, player);
      }
    }
  }

  /**
   * Get current player by its ip address
   * @param players -> All players in an array
   * @return the current player
   */
  private Player getPlayerByIp(Player[] players) throws PlayerNotFoundException{
		String localIp;
		try {
  		localIp = ipHandler.getLocalIp();
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, "The local IP could not be retrieved", e);
			throw new PlayerNotFoundException("The local IP could not be retrieved");
		}
		if (localIp == null || localIp.isEmpty()) {
			throw new PlayerNotFoundException("The retrieved local IP is not usable");
		}
		for(Player player: players) {
			if(player.getIpAddress().equals(localIp)) {
				return player;
			}
		}
    throw new PlayerNotFoundException("Something went wrong while finding the local player");
  }
}
