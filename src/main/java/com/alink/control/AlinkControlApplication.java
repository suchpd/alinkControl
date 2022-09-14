package com.alink.control;

import com.alink.control.websocket.WebsocketHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.net.URISyntaxException;

@SpringBootApplication(scanBasePackages  = {"com.alink.control"})
public class AlinkControlApplication {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        SpringApplication.run(AlinkControlApplication.class, args);
        WebsocketHelper.reconnect();
    }

}
