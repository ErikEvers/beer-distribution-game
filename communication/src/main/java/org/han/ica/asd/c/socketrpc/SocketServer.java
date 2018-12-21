package org.han.ica.asd.c.socketrpc;


import org.han.ica.asd.c.MessageDirector;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer {

    @Inject
    @Named("MessageDirector")
    private IServerObserver serverObserver;

    @Inject
    private static Logger logger;

    private boolean isRunning = true;

    public SocketServer() {
        //inject purposes
    }

    public void startThread() {
        Thread serverThread = new Thread(this::start);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(4445)) {
            logger.log(Level.INFO, "SocketServer started");
            while (isRunning) {
                startServer(serverSocket);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "A server could not be started " + e);
        }
    }

    private void startServer(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();

            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            Object receivedObject = inStream.readObject();

            String senderIp = socket.getInetAddress().toString().substring(1);
            Object response = serverObserver.serverObjectReceived(receivedObject, senderIp);

            if (response != null) {
                new SocketClient().sendObject(socket, response);
            }
            socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Something went wrong with the connection " + e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Something went wrong when trying to get an object " + e);
        }
    }

    public void setServerObserver(IServerObserver serverObserver) {
        this.serverObserver = serverObserver;
    }
}
