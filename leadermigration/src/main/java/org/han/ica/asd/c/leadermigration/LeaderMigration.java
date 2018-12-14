package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.observers.IConnectorObserver;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * singleton classss
 */
public class LeaderMigration implements ILeaderMigration, IConnectorObserver{

  private List<ElectionModel> answeredPlayers;
  @Inject IConnectorForLeaderElection communicator;
  @Inject IPersistenceLeaderMigration iPersistenceLeaderMigration;
  @Inject ElectionHandler electionHandler;

  public LeaderMigration() {

  }

  /**
   * Start the bully algorithm to get new Leader of the network
   * @param players -> all the connected player
   */
  public void startMigration(Player[] players) {
    answeredPlayers = new ArrayList<>();
    electionHandler.setupAlgorithm(players);
    answeredPlayers = electionHandler.sendElectionMessage(players);
    if(isWinner(answeredPlayers)){
      electionHandler.sendVictoryMessage(answeredPlayers.get(0), players);
      iPersistenceLeaderMigration.saveNewLeader(answeredPlayers.get(0).getCurrentPlayer());
    }
  }

  /**
   * Receiving the electionMessage and handle it
   * @param electionModel -> The electionModel of the sending device
   */
  public ElectionModel receiveElectionMessage(ElectionModel electionModel){
    ElectionHandler electionHandler = new ElectionHandler();
    return electionHandler.sendAliveMessage(electionModel);
  }

  /**
   * Add this object to Observer.
   */
  public void initialize() {
    communicator.addObserver(this);
  }

  /**
   * Checks if this device is the winner.
   * @param players -> checks all returned electionModels
   * @return -> true if all elected booleans are true
   *         -> false if one of the elected booleans is false
   */
  private boolean isWinner(List<ElectionModel> players) {
    for(ElectionModel model: players) {
      if(!model.isElected()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Receiving the player who won the bully algorithm and calls the database.
   * @param electedPlayer -> The elected player.
   */
  public void receiveVictoryMessage(Player electedPlayer){
    iPersistenceLeaderMigration.saveNewLeader(electedPlayer);
  }

}
