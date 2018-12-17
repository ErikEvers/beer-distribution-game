package org.han.ica.asd.c.leadermigration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.leadermigration.testutil.PersistenceStub;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static jdk.nashorn.internal.objects.NativeFunction.bind;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
