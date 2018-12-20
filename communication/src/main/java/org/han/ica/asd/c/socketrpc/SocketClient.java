package org.han.ica.asd.c.socketrpc;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClient {

    private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());

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
        try (Socket socket = new Socket(ip, SocketSettings.PORT)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T sendObjectWithResponseGeneric(String ip, T input) throws IOException, ClassNotFoundException {

        try (Socket socket = new Socket(ip, SocketSettings.PORT)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(input);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            Object object = objectInputStream.readObject();

            T result = null;
            if (object != null) {
                result = (T) object;
            }
            return result;
        }
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
            Thread t = new Thread(() -> {
                try {
                    Object response = sendObjectWithResponse(ip, object);
                    map.put(ip, response);
                } catch (IOException | ClassNotFoundException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    map.put(ip, e);
                }
                cdl.countDown();
            });
            t.setDaemon(false);
            t.start();
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }

        return map;
    }


}
