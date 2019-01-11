package org.han.ica.asd.c.leadermigration.integration;

import org.han.ica.asd.c.socketrpc.SocketClient;
import org.han.ica.asd.c.socketrpc.SocketServer;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;

public class LeaderMigrationIntegrationTest {

    @Inject
    private Sock

    @BeforeEach
    void setUp() {
        SocketClient socketClient = new SocketClient();

        SocketServer socketServer = new SocketServer();

        socketServer.setServerObserver();

        socketClient.makeConnection();
    }
}