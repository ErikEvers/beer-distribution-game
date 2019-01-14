package org.han.ica.asd.c.socketrpc;


import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class SocketServer {

    @Inject
    @Named("MessageDirector")
    public static IServerObserver serverObserver;

    @Inject
    private static Logger logger;

    private static ServerSocket serverSocket;
    private boolean isRunning = false;

    public SocketServer() {
        //inject purposes
    }

    public void startThread() {
        if (serverSocket == null) {
            Thread serverThread = new Thread(this::start);
            serverThread.setDaemon(true);
            serverThread.start();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void start() {
        isRunning = true;
        try {
            serverSocket = new ServerSocket(4445);
            logger.log(Level.INFO, "SocketServer started");

            while (isRunning) {
                startListening(serverSocket);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "A server could not be started " + e);
        }
    }

    public void StopThread() {
        isRunning = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private void startListening(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();

            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            Object receivedObject = inStream.readObject();

            String senderIp = socket.getInetAddress().toString().substring(1);
            Object response = serverObserver.serverObjectReceived(receivedObject, senderIp);

            if (response != null) {
                new SocketClient().sendObject(socket, response);
            }
            else {
                socket.close();
            }
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
