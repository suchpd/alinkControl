package com.alink.control.websocket;

import com.alink.control.services.TagPositionService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

public class SensorWebSocketClient extends WebSocketClient {

    public SensorWebSocketClient(URI serverUri , Draft draft ) {
        super( serverUri, draft );
    }

    public SensorWebSocketClient(URI serverURI ) {
        super( serverURI );
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println( "opened connection" );
    }

    @Override
    public void onMessage(String s) {
        TagPositionService.calculateTheCoordinates(s);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
