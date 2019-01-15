package org.han.ica.asd.c.leadermigration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.exceptions.leadermigration.PlayerNotFoundException;
import org.han.ica.asd.c.interfaces.leadermigration.IConnectorForLeaderElection;
import org.han.ica.asd.c.interfaces.communication.ILeaderMigration;
import org.han.ica.asd.c.interfaces.leadermigration.IPersistenceLeaderMigration;
import org.han.ica.asd.c.leadermigration.testutil.IpHandlerStub;
import org.han.ica.asd.c.leadermigration.testutil.PersistenceStub;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
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
}
