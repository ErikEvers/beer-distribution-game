package org.han.ica.asd.c.socketrpc;


import javax.inject.Inject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClient {

    @Inject
    private static Logger logger;

    /**
     * Tries to make a connection with the specified ipAddress.
     * It will try to send an empty object to make sure it can reach the specified ipAddress within a given time.
     * If it can't reach an ipAddress it will throw an exception.
     * This method is used in the FaultDetector Component and the Discovery Component.
     *
     * @param ipAddress The ip address with which a connection will be made.
     * @throws IOException This exception is thrown when an ipAddress can't be reached.
     * @author Tarik
     */
    public void makeConnection(String ipAddress, Object object) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ipAddress, SocketSettings.PORT), 1000);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(object);
        }
    }

    /**
     * This method tries to make a new socket with the given IP, sends an object and expects an object back, which will be returned.
     * No need for socket.close() because of the use of 'try-with'
     *
     * @param ip
     * @param object
     * @return Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object sendObjectWithResponse(String ip, Object object) throws IOException, ClassNotFoundException {
        if (ip.equals("25.20.76.41")) {
            return SocketServer.serverObserver.serverObjectReceived(object, ip);
        }

        try (Socket socket = new Socket(ip, SocketSettings.PORT)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        }
    }

    /**
     * This generic version of sendObjectWithResponse enforces the rule the kind of object that is returned.
     * This way you dont need a switch to determine which object was returned.
     *
     * @param ip    The ip to send to.
     * @param input The message object that needs to be send.
     * @param <T>   The type of the object that was send, and has to be returned.
     * @return The type of message that was send with either the exception or the desired response filled in.
     * @throws IOException            When it is unable to send the object to the specified IP.
     * @throws ClassNotFoundException When it is unable to find the specific class.
     * @author Oscar
     * @see org.han.ica.asd.c.messagehandler.sending.GameMessageClient
     */
    @SuppressWarnings("unchecked")
    public <T> T sendObjectWithResponseGeneric(String ip, T input) throws IOException, ClassNotFoundException {
        Object object = sendObjectWithResponse(ip, input);

        T result = null;
        if (object != null) {
            result = (T) object;
        }
        return result;
    }


    /**
     * This method tried to make a new socket with the given IP and sends an object. This method, however, does not expect something back.
     * No need for socket.close() because of the use of 'try-with'
     *
     * @param ip
     * @param object
     * @throws IOException
     */
    public void sendObject(String ip, Object object) throws IOException {
        try (Socket socket = new Socket(ip, SocketSettings.PORT)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);
        }
    }

    /**
     * This method sends an object with a given Socket.
     *
     * @param socket
     * @param object
     * @throws IOException
     */
    public void sendObject(Socket socket, Object object) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(object);
    }

    /**
     * Sends an object to multiple ips.
     *
     * @param ips
     * @param object
     * @return When all threads are finished, this returns a map with all ips and for each ip a result. The result is either the response object or an exception.
     */
    public Map<String, Object> sendToAll(String[] ips, Object object) {

        CountDownLatch cdl = new CountDownLatch(ips.length);
        Map<String, Object> map = new HashMap<>();

        for (String ip : ips) {
            if (ip.equals("25.20.76.41")) {
                Object response = SocketServer.serverObserver.serverObjectReceived(object, ip);
                map.put(ip, response);
                cdl.countDown();
            } else {
                Thread t = new Thread(() -> {
                    try {
                        Object response = sendObjectWithResponse(ip, object);
                        map.put(ip, response);
                    } catch (IOException | ClassNotFoundException e) {
                        logger.log(Level.SEVERE, e.getMessage(), e);
                        map.put(ip, e);
                    }
                    cdl.countDown();
                });
                t.setDaemon(false);
                t.start();
            }
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return map;
    }
}
