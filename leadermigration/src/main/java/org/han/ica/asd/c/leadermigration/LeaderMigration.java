package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.observers.IConnectorObserver;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * singleton classss
 */
public class LeaderMigration implements ILeaderMigration, IConnectorObserver{

  private Player leader;
  private Player[] players;
  private List<ElectionModel> answeredPlayers;
  @Inject IConnectorForLeaderElection communicator;

  public LeaderMigration() {

  }

  /**
   * Start the bully algorithm to get new Leader of the network
   * @param players -> all the connected player
   */
  public void startMigration(Player[] players) {
    answeredPlayers = new ArrayList<>();
    this.players = players;
    ElectionHandler electionHandler = new ElectionHandler();
    electionHandler.setupAlgorithm(players);
    answeredPlayers = electionHandler.sendElectionMessage(players);
    int notElected = 0;
    for(ElectionModel election: answeredPlayers) {
      if(!election.isElected()) {
        ++notElected;
      }
    }
    if(notElected == 0) {
      //TODO niet helemaal zeker over hoe ik dit aan ga pakken.
      electionHandler.sendVictoryMessage(answeredPlayers.get(0), players);
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

  public void initialize() {
    communicator.addObserver(this);
  }

//  /**
//   * Receiving the Alive message of the receiving device
//   * @param electionModel -> The electionModel with the elected state.
//   */
//  public void receiveAliveMessage(ElectionModel electionModel) {
//    answeredPlayers.add(electionModel.getReceivingPlayer());
//
//  }

  /**
   * Receiving the player who won the bully algorithm and becomes the leader of the network.
   * @param electedPlayer -> The elected player.
   */
  public void receiveVictoryMessage(Player electedPlayer){
    
  }

}
