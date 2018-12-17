package org.han.ica.asd.c.socketrpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient {

    public Object sendObjectWithResponse(String ip, Object object) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(ip, SocketSettings.PORT)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        }//No need for socket.close() because of the use of 'try-with'
    }

    public void sendObject(String ip, Object object) throws IOException {
        try (Socket socket = new Socket(ip, SocketSettings.PORT)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);
        }//No need for socket.close() because of the use of 'try-with'
    }

    public void sendObject(Socket socket, Object object) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(object);
    }
}
