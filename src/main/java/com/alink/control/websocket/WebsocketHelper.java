package com.alink.control.websocket;

import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketHelper {

    public static void reconnect() throws URISyntaxException, InterruptedException{
        SensorWebSocketClient c = new SensorWebSocketClient( new URI( "ws://127.0.0.1:8081/websocket" ) );
        c.connectBlocking();

        new Thread(() -> System.out.println("Runnable running..")) {
            public void run() {
                while (true){
                    try{
                        Thread.sleep(3000);
                        c.send("");
                    }catch (Exception e){
                        c.reconnect();
                    }
                }
            };
        }.start();
    }
}
