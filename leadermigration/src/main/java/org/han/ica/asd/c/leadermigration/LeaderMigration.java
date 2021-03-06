package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.exceptions.PlayerNotFoundException;
import org.han.ica.asd.c.leadermigration.componentInterfaces.IConnectorForLeaderElection;
import org.han.ica.asd.c.leadermigration.componentInterfaces.ILeaderMigration;
import org.han.ica.asd.c.leadermigration.componentInterfaces.IPersistenceLeaderMigration;
import org.han.ica.asd.c.model.dao_model.Player;
import org.han.ica.asd.c.observers.IConnectorObserver;
import javax.inject.Inject;
import java.util.List;

public class LeaderMigration implements ILeaderMigration, IConnectorObserver{

  @Inject IConnectorForLeaderElection communicator;
  @Inject IPersistenceLeaderMigration persistence;
  @Inject ElectionHandler electionHandler;

  /**
   * Start the bully algorithm to get new Leader of the network
   * @param players -> all the connected player
   */
  public Player startMigration(Player[] players) throws PlayerNotFoundException {
    Player currentPlayer = electionHandler.setupAlgorithm(players);
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
