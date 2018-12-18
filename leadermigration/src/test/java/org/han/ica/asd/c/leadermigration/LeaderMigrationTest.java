package org.han.ica.asd.c.leadermigration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.exceptions.PlayerNotFoundException;
import org.han.ica.asd.c.leadermigration.testutil.IpHandlerStub;
import org.han.ica.asd.c.leadermigration.testutil.PersistenceStub;
import org.han.ica.asd.c.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class LeaderMigrationTest {

  private ILeaderMigration leaderMigration;
  private IConnectorForLeaderElection connector;

  @Before
  public void setup() {
    connector = mock(IConnectorForLeaderElection.class);
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(IConnectorForLeaderElection.class).toInstance(connector);
        bind(IPersistenceLeaderMigration.class).to(PersistenceStub.class);
        bind(IpHandler.class).to(IpHandlerStub.class);
        requestStaticInjection(ElectionHandler.class);
      }
    });
    leaderMigration = injector.getInstance(LeaderMigration.class);
  }

  @Test
  public void testAddToObserver() {
    leaderMigration.initialize();
    Mockito.verify(connector).addObserver(any(LeaderMigration.class));
  }

  @Test(expected = PlayerNotFoundException.class)
  public void testDifferentIpThrowsException() throws Exception {
    Player[] players = new Player[1];
    IpHandlerStub.setIpString("1");
    players[0] = new Player("0","0", "0", 0, "Joost", true);
    leaderMigration.startMigration(players);
  }

  @Test(expected = PlayerNotFoundException.class)
  public void testNoIpThrowsException() throws Exception {
    Player[] players = new Player[1];
    IpHandlerStub.setIpString(null);
    players[0] = new Player("0","0", "0", 0, "Joost", true);
    leaderMigration.startMigration(players);
  }
}
