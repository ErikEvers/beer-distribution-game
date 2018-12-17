package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.exceptions.PlayerNotFoundException;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.observers.IConnectorObserver;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeaderMigration implements ILeaderMigration, IConnectorObserver{

	@Inject private static Logger logger;
  @Inject IConnectorForLeaderElection communicator;
  @Inject IPersistenceLeaderMigration persistence;
  @Inject ElectionHandler electionHandler;

  /**
   * Start the bully algorithm to get new Leader of the network
   * @param players -> all the connected player
   */
  public Player startMigration(Player[] players) {
    Player currentPlayer = null;
    try {
      currentPlayer = electionHandler.setupAlgorithm(players);
    } catch (PlayerNotFoundException e) {
			logger.log(Level.SEVERE, "Local player could not be found", e);
    }
    Player winner = currentPlayer;

		List<Player> answeredPlayers = electionHandler.sendElectionMessage(players);

    if(!answeredPlayers.isEmpty()) {
			winner = electionHandler.determineWinner(currentPlayer, answeredPlayers);
		}

		electionHandler.sendVictoryMessage(winner, players);
		persistence.saveNewLeader(winner);
		return winner;
  }

  /**
   * Receiving the electionMessage and handle it
   * @param electionModel -> The electionModel of the sending device
   */
  public ElectionModel receiveElectionMessage(ElectionModel electionModel){
    return electionHandler.sendAliveMessage(electionModel);
  }

  /**
   * Register this object as an observer of the communication component.
   */
	//TODO integrate with comm
  public void initialize() {
    communicator.addObserver(this);
  }

  /**
   * Receiving the player who won the bully algorithm and calls the database.
   * @param electedPlayer -> The elected player.
   */
  public void receiveVictoryMessage(Player electedPlayer){
		persistence.saveNewLeader(electedPlayer);
  }

}
