package org.han.ica.asd.c.leadermigration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.exceptions.PlayerNotFoundException;
import org.han.ica.asd.c.leadermigration.testutil.PersistenceStub;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static jdk.nashorn.internal.objects.NativeFunction.bind;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ElectionHandler.class)
public class LeaderMigrationTest {

  private ILeaderMigration leaderMigration;
  private IConnectorForLeaderElection connector;
  private Injector injector;

  @Before
  public void setup() {
    connector = mock(IConnectorForLeaderElection.class);
    injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(IConnectorForLeaderElection.class).toInstance(connector);
        bind(IPersistenceLeaderMigration.class).to(PersistenceStub.class);
        requestStaticInjection(ElectionHandler.class);
        requestStaticInjection(LeaderMigration.class);
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
    PowerMockito.spy(ElectionHandler.class);
    PowerMockito.doReturn("1").when(ElectionHandler.class, "getLocalIp");

    Player[] players = new Player[1];
    players[0] = new Player("0","0", "0", 0, "Joost", true);
    leaderMigration.startMigration(players);
  }

  @Test(expected = PlayerNotFoundException.class)
  public void testNoIpThrowsException() throws Exception {
    PowerMockito.spy(ElectionHandler.class);
    PowerMockito.doReturn(null).when(ElectionHandler.class, "getLocalIp");

    Player[] players = new Player[1];
    players[0] = new Player("0","0", "0", 0, "Joost", true);
    leaderMigration.startMigration(players);
  }
}
