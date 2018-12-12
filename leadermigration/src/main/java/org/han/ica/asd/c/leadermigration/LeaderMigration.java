package org.han.ica.asd.c.leadermigration;

public class LeaderMigration implements iLeaderMigration{

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

  public void receiveElectionMessage(ElectionModel electionModel){
    ElectionHandler electionHandler = new ElectionHandler();
    electionHandler.sendAliveMessage(electionModel);
  }


}
