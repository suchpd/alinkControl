package com.alink.control.websocket;

import com.alink.control.services.TagPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class WebsocketHelper implements ApplicationRunner {
    @Autowired
    private TagPositionService tagPositionService;

    @Value("${alink.websocket.Url}")
    private String websocketUrl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        reconnect();
    }

    private void reconnect() throws URISyntaxException, InterruptedException{
        SensorWebSocketClient c = new SensorWebSocketClient( new URI( websocketUrl ),tagPositionService );
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
