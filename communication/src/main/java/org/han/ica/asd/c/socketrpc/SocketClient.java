package org.han.ica.asd.c.socketrpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient {

    /**
     * This method tries to make a new socket with the given IP, sends an object and expects an object back, which will be returned.
     * No need for socket.close() because of the use of 'try-with'
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

    /**
     * This method tried to make a new socket with the given IP and sends an object. This method, however, does not expect something back.
     * No need for socket.close() because of the use of 'try-with'
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
     * @param socket
     * @param object
     * @throws IOException
     */
    public void sendObject(Socket socket, Object object) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(object);
    }
}
