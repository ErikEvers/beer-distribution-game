package org.han.ica.asd.c.leadermigration;

import javax.inject.Inject;

public class LeaderMigration implements ILeaderMigration {

  private Player leader;
  private Player[] players;

  @Inject
  private ElectionHandler electionHandler;

  public LeaderMigration() {

  }

  public void startMigration(Player[] players) {
    this.players = players;
    electionHandler.setupAlgorithm(players);
    electionHandler.sendElectionMessage(players);
  }

  public ElectionModel receiveElectionMessage(ElectionModel electionModel){
    return electionHandler.sendAliveMessage(electionModel);
  }

  public void receiveVictoryMessage(ElectionModel electionModel) {

  }

}
