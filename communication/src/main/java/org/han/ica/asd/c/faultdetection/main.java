package org.han.ica.asd.c.faultdetection;

import com.google.inject.AbstractModule;

import com.google.inject.Guice;

import com.google.inject.Injector;

import com.google.inject.name.Names;

import org.han.ica.asd.c.Connector;

import org.han.ica.asd.c.MessageDirector;

import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;

import org.han.ica.asd.c.messagehandler.sending.SendInTransaction;

import org.han.ica.asd.c.socketrpc.IServerObserver;

import org.han.ica.asd.c.socketrpc.SocketClient;

import org.han.ica.asd.c.socketrpc.SocketServer;

public class main {
    private static Connector connector;
    public static void main(String[] args) {
        Injector inject = Guice.createInjector(new AbstractModule() {

            @Override

            protected void configure() {

                bind(IServerObserver.class).annotatedWith(Names.named("MessageDirector")).to(MessageDirector.class);
                requestStaticInjection(SocketClient.class);

                requestStaticInjection(SocketServer.class);

                requestStaticInjection(FailLog.class);

                requestStaticInjection(FaultDetectionClient.class);

                requestStaticInjection(FaultDetectorLeader.class);

                requestStaticInjection(GameMessageClient.class);

                requestStaticInjection(SendInTransaction.class);
                requestStaticInjection(FaultResponder.class);

            }

        });

        connector = inject.getInstance(Connector.class);
        connector.start();

        aanmeldenSpel();

        //startenSpel();

        while (true) {
        }

    }

    public static void aanmeldenSpel() {
        connector.addToNodeInfoList("169.254.156.128");
       // connector.addToNodeInfoList("169.254.231.222");
       // connector.addToNodeInfoList("169.254.231.242");
        connector.setJoiner();

    }

    public static void startenSpel() {

        connector.addToNodeInfoList("169.254.156.128");

        connector.addToNodeInfoList("169.254.153.134");
        connector.setLeader();

    }
}