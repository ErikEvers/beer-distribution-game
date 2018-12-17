package org.han.ica.asd.c.socketrpc;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer {

    private IServerObserver serverObserver;

    private static final Logger LOGGER = Logger.getLogger(SocketServer.class.getName());

    public SocketServer(IServerObserver serverObserver) {
        this.serverObserver = serverObserver;
    }


    public void startThread() {
        Thread serverThread = new Thread(this::start);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private boolean isRunning = true;

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(4445)) {
            LOGGER.log(Level.INFO, "SocketServer started");

            while (isRunning) {
                startServer(serverSocket);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong " + e);
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
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong " + e);
        }
    }
}
