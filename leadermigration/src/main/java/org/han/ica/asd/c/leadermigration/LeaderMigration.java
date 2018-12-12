package org.han.ica.asd.c.leadermigration;

public class LeaderMigration implements ILeaderMigration {

  private Player leader;
  private Player[] players;

  public LeaderMigration() {

  }

  public void startMigration(Player[] players) {
    this.players = players;
    ElectionHandler electionHandler = new ElectionHandler();
    electionHandler.setupAlgorithm(players);
    electionHandler.sendElectionMessage(players);
  }

  public ElectionModel receiveElectionMessage(ElectionModel electionModel){
    ElectionHandler electionHandler = new ElectionHandler();
    return electionHandler.sendAliveMessage(electionModel);
  }

  public void receiveVictoryMessage(ElectionModel electionModel) {

  }

}
