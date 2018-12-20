package org.han.ica.asd.c;

import org.han.ica.asd.c.messagehandler.exceptions.LeaderNotPresentException;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.han.ica.asd.c.socketrpc.IServerObserver;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.han.ica.asd.c.socketrpc.SocketServer;

import java.io.IOException;

public class Main implements IServerObserver {
    public static void  main(String[] args){
        Main main = new Main();
        new SocketServer(main).startThread();

        SocketClient sc = new SocketClient();

        WhoIsTheLeaderMessage whoIsTheLeaderMessage = new WhoIsTheLeaderMessage();

        try {
            WhoIsTheLeaderMessage message = sc.sendObjectWithResponseGeneric("145.74.218.103", whoIsTheLeaderMessage);
            System.out.println(message.getResponse().toString());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object serverObjectReceived(Object receivedObject, String senderIp) {
        WhoIsTheLeaderMessage returnDing = (WhoIsTheLeaderMessage) receivedObject;
//        returnDing.setResponse(new LeaderNotPresentException("KUTZOOI"));
        returnDing.setResponse("192.168.0.1");

        return returnDing;
    }
}
